package com.outbrain.ci.friendly.flatten.maven.plugin.visitor;

import com.outbrain.ci.friendly.flatten.maven.plugin.VersionUtil;
import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import static com.outbrain.ci.friendly.flatten.maven.plugin.VersionMojo.DEFAULT_VERSION_REGEX;
import static org.junit.Assert.assertEquals;


public class VersionUtilTest {

  public static final String V_PREFIX_VERSION_PATTERN = "v"+DEFAULT_VERSION_REGEX;

  @Test
  public void testVersionMatcher1() throws Exception {
    final String version = VersionUtil.getVersion("abc-dsffsd-44.0.0.0", DEFAULT_VERSION_REGEX);
    assertEquals("44.0.0.0", version);
  }

  @Test
  public void testVersionMatcher2() throws Exception {
    final String version = VersionUtil.getVersion("abc-4.0.0.0.0.0.0", DEFAULT_VERSION_REGEX);
    assertEquals("4.0.0.0.0.0.0", version);
  }


  @Test
  public void testVersionMatcher3() throws Exception {
    final String version = VersionUtil.getVersion("v4.0.0.0", DEFAULT_VERSION_REGEX);
    assertEquals("4.0.0.0", version);
  }

  @Test
  public void testVersionMatcher4() throws Exception {
    final String version = VersionUtil.getVersion("v4.0.0.1855", V_PREFIX_VERSION_PATTERN);
    assertEquals("v4.0.0.1855", version);
  }

  @Test(expected = MojoExecutionException.class)
  public void testVersionMatcherNoMatch() throws Exception {
    final String version = VersionUtil.getVersion("blahblah", DEFAULT_VERSION_REGEX);
  }

  @Test
  public void testVersionMatcher5() throws Exception {
    final String version = VersionUtil.getVersion("99", DEFAULT_VERSION_REGEX);
    assertEquals("99", version);

  }
  @Test
  public void testVersionMatcher6() throws Exception {
    final String version = VersionUtil.getVersion("9.0", DEFAULT_VERSION_REGEX);
    assertEquals("9.0", version);

  }
  @Test
  public void testVersionMatcher7() throws Exception {
    final String version = VersionUtil.getVersion("9", DEFAULT_VERSION_REGEX);
    assertEquals("9", version);

  }
}
