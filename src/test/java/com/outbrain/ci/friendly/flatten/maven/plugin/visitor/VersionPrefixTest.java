package com.outbrain.ci.friendly.flatten.maven.plugin.visitor;


import com.outbrain.ci.friendly.flatten.maven.plugin.VersionUtil;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VersionPrefixTest {

  @Test
  public void testVersionMatcherSingleHyphen() throws MojoExecutionException {
    final String version = VersionUtil.getVersion("abc-4.0.0.0.0.0.0", ".*-");
    assertEquals("4.0.0.0.0.0.0", version);
  }

  @Test
  public void testVersionMatcherTwoHyphens() throws MojoExecutionException {
    final String version = VersionUtil.getVersion("abc-dsffsd-44.0.0.0", ".*-");
    assertEquals("44.0.0.0", version);
  }

  @Test
  public void testVersionMatcherSingleLetter() throws MojoExecutionException {
    final String version = VersionUtil.getVersion("V4.0.0", "V");
    assertEquals("4.0.0", version);
  }

  @Test
  public void testVersionMatcherUnmatched() {
    assertThrows(MojoExecutionException.class, () -> VersionUtil.getVersion("V4.0.0", "Z"));
  }


}