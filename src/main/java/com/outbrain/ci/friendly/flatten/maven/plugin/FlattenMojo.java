package com.outbrain.ci.friendly.flatten.maven.plugin;


import com.outbrain.ci.friendly.flatten.maven.plugin.visitor.PomVisitorImpl;
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


// CHECKSTYLE_OFF: LineLength
@Mojo(name = "flatten", requiresProject = true, requiresDirectInvocation = false, executionStrategy = "once-per-session",
    requiresDependencyCollection = ResolutionScope.RUNTIME, threadSafe = true, defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
// CHECKSTYLE_ON: LineLength
public class FlattenMojo extends AbstractCiFriendlyMojo {
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

  @Parameter(property = "sha1")
  private String sha1;

  @Parameter(property = "changelist")
  private String changeList;

  @Parameter(property = "removeRelativePath", defaultValue = "false")
  private String removeRelativePath;

  private final PomVisitorImpl pomVisitor = new PomVisitorImpl();

  /**
   * {@inheritDoc}
   */
  public void execute() throws MojoExecutionException {
    final String originalPom = readPom();
    final String revision = getRevision();
    final String modifiedPom = pomVisitor.visit(originalPom, revision, sha1, changeList, Boolean.valueOf(removeRelativePath));
    if (originalPom.equals(modifiedPom)) {
      getLog().info("Pom does not have any CI friendly properties");
    } else {
      getLog().info("Replacing CI friendly properties for project " + this.project.getId() + "...");
      final File ciFriendlyPomFile = writePom(modifiedPom);
      this.project.setPomFile(ciFriendlyPomFile);
    }
  }

  private File writePom(final String content) throws MojoExecutionException {
    final File flattenedPomFile = getCiFriendlyPomFile();

    final File parentFile = flattenedPomFile.getParentFile();
    if (!parentFile.exists()) {
      boolean success = parentFile.mkdirs();
      if (!success) {
        throw new MojoExecutionException("Failed to create directory " + flattenedPomFile.getParent());
      }
    }

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
    } else if (this.project.getProperties().containsKey("revision")) {
      return this.project.getProperties().getProperty("revision");
    } else {
      return this.project.getVersion();
    }
  }
}
