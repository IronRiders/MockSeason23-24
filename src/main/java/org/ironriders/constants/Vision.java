package org.ironriders.constants;

public class Vision {
    public static final String CAMERA = "LIMELIGHT";

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

    public static final String APRIL_TAG_FIELD_LAYOUT_LOCATION = "/apriltags/2018-powerup.json";
}
