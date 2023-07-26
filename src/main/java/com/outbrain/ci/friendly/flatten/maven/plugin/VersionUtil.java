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

    public static String incrementRevision(String revision, SemanticVersion semanticVersion) {
        String[] splitRevision = revision.split("\\.");
        int index = splitRevision.length > semanticVersion.index ? semanticVersion.index : splitRevision.length - 1;
        int incremented = Integer.parseInt(splitRevision[index]) + 1;

        boolean resetNextParts = false;
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < splitRevision.length; i++) {
            final String number;
            if (index == i) {
                number = Integer.toString(incremented);
                resetNextParts = true;
            } else if (resetNextParts) {
                number = "0";
            } else {
                number = splitRevision[i];
            }

            builder
                    .append(number)
                    .append(i == splitRevision.length - 1 ? "" : ".");
        }

        return builder.toString();
    }
}
