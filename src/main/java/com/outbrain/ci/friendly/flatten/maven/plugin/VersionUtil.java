package com.outbrain.ci.friendly.flatten.maven.plugin;

public class VersionUtil {

  public static String getVersion(String version, String prefix) {
    return version.substring(version.lastIndexOf(prefix) + 1);
  }

}
