package com.outbrain.ci.friendly.flatten.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionUtil {

  public static String getVersion(String version, String versionRegex) throws MojoExecutionException {
    Pattern pattern = Pattern.compile(versionRegex);
    final Matcher matcher = pattern.matcher(version);
    if (matcher.find()) {
      return matcher.group(0);
    }
    throw new MojoExecutionException("Couldn't match version pattern:" + versionRegex + " on version" + version);
  }
}
