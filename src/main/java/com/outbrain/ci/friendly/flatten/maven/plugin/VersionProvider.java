package com.outbrain.ci.friendly.flatten.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;

public interface VersionProvider {
   String getVersion(String command) throws MojoExecutionException;
}
