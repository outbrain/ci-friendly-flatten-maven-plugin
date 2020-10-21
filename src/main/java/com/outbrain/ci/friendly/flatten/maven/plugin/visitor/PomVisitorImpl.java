package com.outbrain.ci.friendly.flatten.maven.plugin.visitor;

import javax.inject.Named;


public class PomVisitorImpl implements PomVisitor {
  @Override
  public String visit(String originalPom, String revision, String sha1, String changeList) {
    String modified = originalPom.replace("${revision}", revision);
    if (originalPom.contains("${sha1}")) {
      modified = modified.replace("${sha1}", sha1 != null ? sha1 : "");
    }

    if (originalPom.contains("${changelist}")) {
      modified = modified.replace("${changelist}", changeList != null ? changeList : "");
    }

    return modified;
  }
}
