package org.ironriders.lib;

public enum VisionPipeline {
    APRIL_TAGS(0),
    CUBES(1);

    final int index;

    VisionPipeline(int index) {
        this.index = index;
    }

    public static VisionPipeline fromIndex(int index) {
        for (VisionPipeline pipeline : VisionPipeline.values()) {
            if (pipeline.index == index) {
                return pipeline;
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }
}