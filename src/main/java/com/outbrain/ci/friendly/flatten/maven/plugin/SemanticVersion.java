package com.outbrain.ci.friendly.flatten.maven.plugin;

import java.util.Arrays;

public enum SemanticVersion {

    MAJOR(0),
    MINOR(1),
    PATCH(2);

    final int index;

    SemanticVersion(int index) {
        this.index = index;
    }

    static SemanticVersion of(String value) {
        try {
            return SemanticVersion.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Unknown semantic.version: " + value +
                            ", expected values: " + Arrays.toString(SemanticVersion.values())
            );
        }
    }
}
