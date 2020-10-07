package com.outbrain.ci.friendly.maven.plugin;

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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.plugin.AbstractScmMojo;
import org.apache.maven.scm.provider.ScmProvider;
import org.apache.maven.scm.repository.ScmRepository;
import org.codehaus.plexus.util.StringUtils;

import java.io.IOException;


@Mojo(name = "scmTag", aggregator = true, requiresProject = true, requiresDirectInvocation = true, executionStrategy = "once-per-session", threadSafe = true)
public class TagMojo extends AbstractScmMojo {
  /**
   * The tag name.
   */
  @Parameter(property = "tag", defaultValue = "${project.artifactId}-${project.version}")
  private String tag;

  /**
   * The revision name.
   */
  @Parameter(property = "revision")
  private String revision;

  /**
   * The message applied to the tag creation.
   */
  @Parameter(property = "message")
  private String message;

  /**
   * currently only implemented with svn scm. Enable a workaround to prevent issue
   * due to svn client > 1.5.0 (https://issues.apache.org/jira/browse/SCM-406)
   *
   * @since 1.2
   */
  @Parameter(property = "remoteTagging", defaultValue = "true")
  private boolean remoteTagging;

  /**
   * Currently only implemented with Subversion. Enable the "--pin-externals"
   * option in svn copy commands which is new in Subversion 1.9.
   *
   * @see https://subversion.apache.org/docs/release-notes/1.9.html
   * @since 1.11.0
   */
  @Parameter(property = "pinExternals", defaultValue = "false")
  private boolean pinExternals;

  /**
   * Enable the "--sign" in Git
   *
   * @since 1.11.0
   */
  @Parameter(property = "sign", defaultValue = "false")
  private boolean sign;

  /**
   * {@inheritDoc}
   */
  public void execute()
      throws MojoExecutionException {
    super.execute();

    try {
      String finalTag = tag;
      if (StringUtils.isEmpty(tag)) {
        throw new NullPointerException("You need to define a tag parameter in scm or pass it as arg");
      }

      ScmRepository repository = getScmRepository();
      ScmProvider provider = getScmManager().getProviderByRepository(repository);

      finalTag = provider.sanitizeTagName(finalTag);
      getLog().info("Final Tag Name: '" + finalTag + "'");

      ScmTagParameters scmTagParameters = new ScmTagParameters(message);
      scmTagParameters.setRemoteTagging(remoteTagging);
      scmTagParameters.setPinExternals(pinExternals);
      scmTagParameters.setSign(sign);

      TagScmResult result = provider.tag(repository, getFileSet(), finalTag, scmTagParameters);

      checkResult(result);
    } catch (IOException e) {
      throw new MojoExecutionException("Cannot run tag command : ", e);
    } catch (ScmException e) {
      throw new MojoExecutionException("Cannot run tag command : ", e);
    }
  }
}
