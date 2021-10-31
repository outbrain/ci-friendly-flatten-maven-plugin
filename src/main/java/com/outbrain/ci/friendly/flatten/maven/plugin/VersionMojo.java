package com.outbrain.ci.friendly.flatten.maven.plugin;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.plugin.AbstractScmMojo;

import javax.inject.Inject;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;


@Mojo(name = "version", aggregator = true, requiresProject = true, requiresDirectInvocation = true,
    executionStrategy = "once-per-session", threadSafe = true, defaultPhase = LifecyclePhase.VALIDATE)
public class VersionMojo extends AbstractScmMojo {
  /**
   * The Maven Project.
   */
  @Parameter(defaultValue = "${project}", readonly = true, required = true)
  private MavenProject project;

  /**
   * The {@link MavenSession} used to get user properties.
   */
  @Parameter(defaultValue = "${session}", readonly = true, required = true)
  private MavenSession session;

  @Parameter(property = "tag.fetch", defaultValue = "true")
  private Boolean tagFetch;

  @Parameter(property = "command", defaultValue = "git describe --abbrev=0 --tags")
  private String command;

  @Inject
  private VersionProvider versionProvider;

  @Parameter(defaultValue = "${reactorProjects}", required = true)
  private List<MavenProject> reactorProjects;

  @Parameter(property = "file.name", defaultValue = "revision.txt")
  private String fileName;


  @Parameter(property = "default.tag", defaultValue = "${project.artifactId}-0.0.0.1")
  private String defaultTag;

  @Parameter(property = "tag.prefix", defaultValue = "-")
  private String prefixTag;

  /**
   * {@inheritDoc}
   */
  public void execute()
      throws MojoExecutionException {
    super.execute();
    String version = versionProvider.getVersion(command);

    if (version == null || version.equals("")) {
      getLog().info("Failed to retrieve version tag, continuing with default tag:" + defaultTag);
      version = defaultTag;
    }

    getLog().info("Current version:" + version);
    // define a new property in the Maven Project
    String revision = removePrefix(version);
    getLog().info("revision without prefix:" + revision);

    String nextRevision = incrementRevision(revision);

    project.getProperties().put("internal.revision", nextRevision);

    writeVersionToFile(nextRevision);

    // Maven Plugins have built in logging too
    getLog().info("Next revision: " + nextRevision);

  }

  private void writeVersionToFile(String nextRevision) throws MojoExecutionException {
    final File file = new File(fileName);

    try {
      if (!file.createNewFile()) {
        getLog().info("Overwrite file: " + file.getAbsolutePath());
      } else {
        getLog().info("Write to new file: " + file.getAbsolutePath());
      }
      Files.write(file.toPath(), Collections.singletonList(nextRevision), StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

  private String incrementRevision(String revision) {
    String[] splitRevision = revision.split("\\.");

    Integer incremented = Integer.parseInt(splitRevision[splitRevision.length - 1]) + 1;

    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < splitRevision.length - 1; i++) {
      builder
          .append(splitRevision[i])
          .append(".");
    }
    builder.append(incremented);

    return builder.toString();
  }

  private String removePrefix(String version) {
    if (prefixTag != null){
      return version.substring(version.lastIndexOf(prefixTag) + 1);
    }
    return version.substring(version.lastIndexOf("-") + 1);
  }
}
