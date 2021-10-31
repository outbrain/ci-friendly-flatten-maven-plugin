package com.outbrain.ci.friendly.flatten.maven.plugin;

import org.apache.maven.plugin.MojoExecutionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionUtil {

  public static String getVersion(final String prefixedVersion, final String prefixRegex) throws MojoExecutionException {
    final String regex = prefixRegex + "(.+)";
    final Matcher matcher = Pattern.compile(regex).matcher(prefixedVersion);
    if (!matcher.find()) {
      throw new MojoExecutionException("Cannot extract version from " + prefixedVersion + " with prefix regex " + prefixRegex);
    }
    return matcher.group(1);
  }

}
