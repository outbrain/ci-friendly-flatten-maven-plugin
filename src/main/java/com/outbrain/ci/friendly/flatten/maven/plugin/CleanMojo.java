package com.outbrain.ci.friendly.flatten.maven.plugin;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;


//CHECKSTYLE_OFF: LineLength
@Mojo(name = "clean", requiresProject = true, requiresDirectInvocation = false, executionStrategy = "once-per-session", threadSafe = true,
    defaultPhase = LifecyclePhase.CLEAN)
//CHECKSTYLE_ON: LineLength
public class CleanMojo extends AbstractCiFriendlyMojo {
  /**
   * The constructor.
   */
  public CleanMojo() {
    super();
  }

  public void execute() throws MojoFailureException {
    File flattenedPomFile = getCiFriendlyPomFile();
    if (flattenedPomFile.isFile()) {
      getLog().info("Deleting " + flattenedPomFile.getPath());
      boolean deleted = flattenedPomFile.delete();
      if (!deleted) {
        throw new MojoFailureException("Could not delete " + flattenedPomFile.getAbsolutePath());
      }
    }
  }

}
