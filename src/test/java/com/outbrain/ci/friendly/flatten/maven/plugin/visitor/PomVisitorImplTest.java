package com.outbrain.ci.friendly.flatten.maven.plugin.visitor;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class PomVisitorImplTest {
  private PomVisitorImpl visitor;

  @Before
  public void before() {
    visitor = new PomVisitorImpl();
  }

  @Test
  public void testFullModifyPom() throws Exception {
    final File originalPomFile = new File("src/test/resources/all/original-test-pom.xml");
    final String originalPom = readPom(originalPomFile);

    final File resultPomFile = new File("src/test/resources/all/result-test-pom.xml");
    final String resultPom = readPom(resultPomFile);

    final String modifiedPom = visitor.visit(originalPom, "test-revision", "test-sha1", "test-changelist", false);
    
    assertEquals(resultPom, modifiedPom);
    
  }
  
  @Test
  public void testFullModifyNoPathPom() throws Exception {
	final File originalPomFile = new File("src/test/resources/all/original-removepath-test-pom.xml");
	final String originalPom = readPom(originalPomFile);

	final File resultPomFile = new File("src/test/resources/all/result-removepath-test-pom.xml");
	final String resultPom = readPom(resultPomFile);

	final String modifiedPom = visitor.visit(originalPom, "test-revision", "test-sha1", "test-changelist", true);
	
	assertEquals(resultPom, modifiedPom);
	    
  }

  @Test
  public void testOnlyRevisionPom() throws Exception {
    final File originalPomFile = new File("src/test/resources/revision/original-test-pom.xml");
    final String originalPom = readPom(originalPomFile);

    final File resultPomFile = new File("src/test/resources/revision/result-test-pom.xml");
    final String resultPom = readPom(resultPomFile);

    final String modifiedPom = visitor.visit(originalPom, "test-revision", null, null, false);

    assertEquals(resultPom, modifiedPom);
  }

  @Test
  public void testRevisionReplacedSha1DefaultReplacePom() throws Exception {
    final File originalPomFile = new File("src/test/resources/revision.and.sha1/original-test-pom.xml");
    final String originalPom = readPom(originalPomFile);

    final File resultPomFile = new File("src/test/resources/revision.and.sha1/result-test-pom.xml");
    final String resultPom = readPom(resultPomFile);

    final String modifiedPom = visitor.visit(originalPom, "test-revision", null, null, false);

    assertEquals(resultPom, modifiedPom);
  }

  @Test
  public void testRevisionReplacedChangeListDefaultReplacePom() throws Exception {
    final File originalPomFile = new File("src/test/resources/revision.and.changelist/original-test-pom.xml");
    final String originalPom = readPom(originalPomFile);

    final File resultPomFile = new File("src/test/resources/revision.and.changelist/result-test-pom.xml");
    final String resultPom = readPom(resultPomFile);

    final String modifiedPom = visitor.visit(originalPom, "test-revision", null, null, false);

    assertEquals(resultPom, modifiedPom);
  }

  private String readPom(final File pomFile) throws Exception {
    try {
      return FileUtils.readFileToString(pomFile);
    } catch (IOException e) {
      final String message = e.getMessage();
      throw new Exception(message);
    }
  }
}
