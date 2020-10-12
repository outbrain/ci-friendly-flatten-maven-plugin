package com.outbrain.ci.friendly.flatten.maven.plugin;


import org.apache.commons.io.FileUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;


@SuppressWarnings("deprecation")
// CHECKSTYLE_OFF: LineLength
@Mojo(name = "ci-friendly", requiresProject = true, requiresDirectInvocation = false, executionStrategy = "once-per-session",
    requiresDependencyCollection = ResolutionScope.RUNTIME, threadSafe = true, defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
// CHECKSTYLE_ON: LineLength
public class CiFriendlyMojo extends AbstractCiFriendlyMojo {
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

  @Parameter(property = "tagPrefix", defaultValue = "")
  private String tagPrefix;

  @Parameter(property = "sha1")
  private String sha1;

  @Parameter(property = "changelist")
  private String changeList;

  /**
   * {@inheritDoc}
   */
  public void execute() throws MojoExecutionException {
    final String originalPom = readPom();
    final String revision = getRevision();
    final String modifiedPom = replacePlaceHolders(originalPom, revision, sha1, changeList);
    if (originalPom.equals(modifiedPom)) {
      getLog().info("POM is not CI friendly");
    } else {
      getLog().info("Replacing CI friendly properties for project " + this.project.getId() + "...");
      final File ciFriendlyPomFile = writePom(modifiedPom);
      this.project.setPomFile(ciFriendlyPomFile);
    }
  }

  private File writePom(final String content) throws MojoExecutionException {
    final File flattenedPomFile = getCiFriendlyPomFile();
    try (FileWriter writer = new FileWriter(flattenedPomFile)) {
      writer.write(content);
      getLog().info("Successfully wrote to the file.");
    } catch (IOException e) {
      getLog().error("An error occurred while writing " + flattenedPomFile, e);
      String message = e.getMessage();
      throw new MojoExecutionException(message);
    }
    return flattenedPomFile;
  }

  private String replacePlaceHolders(final String originalPom, final String revision, String sha1, String changeList) {

    return originalPom.replace("${revision}", revision)
        .replace("${tagPrefix}", tagPrefix != null ? tagPrefix : "")
        .replace("${sha1}", sha1 != null ? sha1 : "")
        .replace("${changelist}", changeList != null ? changeList : "");
  }

  private String readPom() throws MojoExecutionException {
    final File originalPomFile = this.project.getFile();
    try {
      return FileUtils.readFileToString(originalPomFile);
    } catch (IOException e) {
      getLog().error("An error occurred while reading " + originalPomFile, e);
      String message = e.getMessage();
      throw new MojoExecutionException(message);
    }
  }

  private String getRevision() {
    if (this.project.getProperties().containsKey("internal.revision")) {
      return this.project.getProperties().getProperty("internal.revision");
    }
    final Properties systemProperties = this.session.getSystemProperties();
    if (systemProperties.containsKey("revision")) {
      return systemProperties.getProperty("revision");
    } else {
      return this.project.getVersion();
    }
  }
}
