package com.outbrain.ci.friendly.flatten.maven.plugin.visitor;


import com.outbrain.ci.friendly.flatten.maven.plugin.VersionUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VersionPrefixTest {

  @Test
  public void testVersionMatcher1() {

    final String version = VersionUtil.getVersion("abc-dsffsd-44.0.0.0", "-");
    assertEquals("44.0.0.0", version);
  }

  @Test
  public void testVersionMatcher2() {

    final String version = VersionUtil.getVersion("abc-4.0.0.0.0.0.0", "-");
    assertEquals("4.0.0.0.0.0.0", version);
  }


}