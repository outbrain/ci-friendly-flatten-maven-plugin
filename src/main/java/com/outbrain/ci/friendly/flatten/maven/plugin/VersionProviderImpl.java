package com.outbrain.ci.friendly.flatten.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Named
@Singleton
public class VersionProviderImpl implements VersionProvider {

  public String getVersion(String command) throws MojoExecutionException {
    try {
      StringBuilder builder = new StringBuilder();
      Process process = Runtime.getRuntime().exec(command);

      try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line;
        while ((line = input.readLine()) != null) {
          builder.append(line);
        }
      }
      // return the output
      return builder.toString();

    } catch (IOException e) {
      throw new MojoExecutionException("Execution of command '" + command
          + "' failed", e);
    }
  }
}
