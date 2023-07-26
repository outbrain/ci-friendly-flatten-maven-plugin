package com.outbrain.ci.friendly.flatten.maven.plugin;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VersionUtilTest {

    @Test
    void should_increment_major_version() {
        // given
        String givenRevision = "1.1.1";
        SemanticVersion givenSemanticVersion = SemanticVersion.MAJOR;

        // when
        String incrementedRevision = VersionUtil.incrementRevision(givenRevision, givenSemanticVersion);

        // then
        assertEquals("2.0.0", incrementedRevision);
    }

    @Test
    void should_increment_minor_version() {
        // given
        String givenRevision = "1.0.1";
        SemanticVersion givenSemanticVersion = SemanticVersion.MINOR;

        // when
        String incrementedRevision = VersionUtil.incrementRevision(givenRevision, givenSemanticVersion);

        // then
        assertEquals("1.1.0", incrementedRevision);
    }

    @Test
    void should_increment_patch_version() {
        // given
        String givenRevision = "1.0.0";
        SemanticVersion givenSemanticVersion = SemanticVersion.PATCH;

        // when
        String incrementedRevision = VersionUtil.incrementRevision(givenRevision, givenSemanticVersion);

        // then
        assertEquals("1.0.1", incrementedRevision);
    }

    @Test
    void should_increment_major_version_when_revision_contains_2_digits() {
        // given
        String givenRevision = "1.1";
        SemanticVersion givenSemanticVersion = SemanticVersion.MAJOR;

        // when
        String incrementedRevision = VersionUtil.incrementRevision(givenRevision, givenSemanticVersion);

        // then
        assertEquals("2.0", incrementedRevision);
    }

    @Test
    void should_increment_minor_version_when_revision_contains_2_digits() {
        // given
        String givenRevision = "1.0";
        SemanticVersion givenSemanticVersion = SemanticVersion.MINOR;

        // when
        String incrementedRevision = VersionUtil.incrementRevision(givenRevision, givenSemanticVersion);

        // then
        assertEquals("1.1", incrementedRevision);
    }

    @Test
    void should_increment_version_when_revision_contains_1_digit() {
        // given
        String givenRevision = "1";
        SemanticVersion givenSemanticVersion = SemanticVersion.MAJOR;

        // when
        String incrementedRevision = VersionUtil.incrementRevision(givenRevision, givenSemanticVersion);

        // then
        assertEquals("2", incrementedRevision);
    }

    @Test
    void should_increment_minor_version_when_revision_contains_2_digits_and_semantic_version_is_patch() {
        // given
        String givenRevision = "1.0";
        SemanticVersion givenSemanticVersion = SemanticVersion.PATCH;

        // when
        String incrementedRevision = VersionUtil.incrementRevision(givenRevision, givenSemanticVersion);

        // then
        assertEquals("1.1", incrementedRevision);
    }

    @Test
    void should_increment_major_version_when_revision_contains_1_digit_and_semantic_version_is_patch() {
        // given
        String givenRevision = "1";
        SemanticVersion givenSemanticVersion = SemanticVersion.PATCH;

        // when
        String incrementedRevision = VersionUtil.incrementRevision(givenRevision, givenSemanticVersion);

        // then
        assertEquals("2", incrementedRevision);
    }
}