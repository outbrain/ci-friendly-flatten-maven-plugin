package com.outbrain.ci.friendly.flatten.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

public abstract class AbstractCiFriendlyMojo extends AbstractMojo {

  /**
   * The directory where the generated ci friendly POM file will be written to.
   */
  @Parameter(defaultValue = "${project.basedir}")
  private File outputDirectory;

  /**
   * The filename of the generated ci friendly POM file.
   */
  @Parameter(property = "ciFriendlyPomFilename", defaultValue = ".ci-friendly-pom.xml")
  private String ciFriendlyPomFilename;

  /**
   * @return the filename of the generated ci friendly POM file.
   */
  public String getCiFriendlyPomFilename() {
    return this.ciFriendlyPomFilename;
  }

  /**
   * @return the directory where the generated ci friendly POM file will be written to.
   */
  public File getOutputDirectory() {
    return this.outputDirectory;
  }

  /**
   * @return a {@link File} instance pointing to the modified POM.
   */
  protected File getCiFriendlyPomFile() {
    return new File(getOutputDirectory(), getCiFriendlyPomFilename());
  }

}
