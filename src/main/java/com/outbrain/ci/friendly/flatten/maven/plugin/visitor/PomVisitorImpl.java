package com.outbrain.ci.friendly.flatten.maven.plugin.visitor;

public class PomVisitorImpl {

  public String visit(final String originalPom, final String revision, final String sha1, final String changeList){
    String modified;
    modified = originalPom.replace("${revision}", revision);
    if (modified.contains("${sha1}")) {
      modified = modified.replace("${sha1}", sha1 != null ? sha1 : "");
    }

    if (modified.contains("${changelist}")) {
      modified = modified.replace("${changelist}", changeList != null ? changeList : "");
    }

    return modified;
  }
}
