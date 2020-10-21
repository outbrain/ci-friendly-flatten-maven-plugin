package com.outbrain.ci.friendly.flatten.maven.plugin.visitor;

public interface PomVisitor {
  String visit(final String originalPom, final String revision, String sha1, String changeList);
}
