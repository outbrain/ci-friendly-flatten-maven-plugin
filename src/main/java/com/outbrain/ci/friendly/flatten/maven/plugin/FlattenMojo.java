package com.outbrain.ci.friendly.flatten.maven.plugin;


import com.outbrain.ci.friendly.flatten.maven.plugin.visitor.PomVisitorImpl;
import org.apache.commons.io.FileUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
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

  /**
   * Should automatically rewrite CI friendly POM or only when there is a change.
   */
  @Parameter(property = "autoRewriteCiFriendlyPoms", defaultValue = "true")
  private boolean autoRewriteCiFriendlyPoms;

  private final PomVisitorImpl pomVisitor = new PomVisitorImpl();

  /**
   * {@inheritDoc}
   */
  public void execute() throws MojoExecutionException {
    final String originalPom = readPom();
    final String revision = getRevision();
    final String modifiedPom = pomVisitor.visit(originalPom, revision, sha1, changeList);
    if (originalPom.equals(modifiedPom)) {
      getLog().info("Pom does not have any CI friendly properties");
    } else {
      final File ciFriendlyPomFile = isUpdateNeeded(modifiedPom) ? writePom(modifiedPom) : getCiFriendlyPomFile();
      this.project.setPomFile(ciFriendlyPomFile);
      this.project.setOriginalModel(getModel(ciFriendlyPomFile));
    }
  }

  private boolean isUpdateNeeded(String content) throws MojoExecutionException {
    if (autoRewriteCiFriendlyPoms) {
      return true;
    }
    File flattenedPomFile = getCiFriendlyPomFile();
    if (flattenedPomFile.exists()) {
      String contentOfFlattenedPomFileBeforeUpdate = readFile(flattenedPomFile);
      if(contentOfFlattenedPomFileBeforeUpdate.equals(content)){
        getLog().info( "Skipping writing CI friendly properties for project " + this.project.getId() +
                " as it is already up to date");
        return false;
      }
    }
    return true;
  }

  private Model getModel(final File file) throws MojoExecutionException {
    MavenXpp3Reader reader = new MavenXpp3Reader();
    try {
      return reader.read(Files.newInputStream(file.toPath()));
    } catch (final Exception e) {
      throw new MojoExecutionException("Error reading raw model.", e);
    }
  }

  private File writePom(final String content) throws MojoExecutionException {
    getLog().info("Replacing CI friendly properties for project " + this.project.getId() + "...");
    final File flattenedPomFile = getCiFriendlyPomFile();

    final File parentFile = flattenedPomFile.getParentFile();
    if (!parentFile.exists()) {
      final boolean success = parentFile.mkdirs();
      if (!success) {
        throw new MojoExecutionException("Failed to create directory " + flattenedPomFile.getParent());
      }
    }

    try (final FileWriter writer = new FileWriter(flattenedPomFile)) {
      writer.write(content);
      getLog().info("Successfully wrote to " + flattenedPomFile);
    } catch (final Exception e) {
      getLog().error("An error occurred while writing " + flattenedPomFile, e);
      final String message = e.getMessage();
      throw new MojoExecutionException(message);
    }
    return flattenedPomFile;
  }

  private String readPom() throws MojoExecutionException {
    final File originalPomFile = this.project.getFile();
    return readFile(originalPomFile);
  }

  private String readFile(File file) throws MojoExecutionException {
    try {
      return FileUtils.readFileToString(file, Charset.defaultCharset());
    } catch (final IOException e) {
      getLog().error("An error occurred while reading " + file, e);
      final String message = e.getMessage();
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
