package org.ironriders.subsystems;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.constants.Game;
import org.ironriders.lib.VisionPipeline;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonPipelineResult;

import java.io.IOException;
import java.util.Optional;

import static org.ironriders.constants.Robot.LIMELIGHT_POSITION;
import static org.ironriders.constants.Vision.APRIL_TAG_FIELD_LAYOUT_LOCATION;
import static org.ironriders.lib.VisionPipeline.APRIL_TAGS;
import static org.photonvision.PhotonPoseEstimator.PoseStrategy.LOWEST_AMBIGUITY;

public class VisionSubsystem extends SubsystemBase {
    private final PhotonCamera camera = new PhotonCamera("LIMELIGHT");
    private final PhotonPoseEstimator estimator;
    private final AprilTagFieldLayout tagLayout;
    private boolean useVisionForEstimation = false;

    public VisionSubsystem() {
        try {
            tagLayout = new AprilTagFieldLayout(
                    Filesystem.getDeployDirectory() + APRIL_TAG_FIELD_LAYOUT_LOCATION
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        estimator = new PhotonPoseEstimator(tagLayout, LOWEST_AMBIGUITY, camera, LIMELIGHT_POSITION);

        setPipeline(APRIL_TAGS);
        camera.setLED(VisionLEDMode.kOff);
        camera.setDriverMode(false);
    }

    public AprilTagFieldLayout getTagLayout() {
        return tagLayout;
    }

    public Optional<EstimatedRobotPose> getPoseEstimate() {
        if (useVisionForEstimation) {
            setPipeline(APRIL_TAGS);
            return estimator.update();
        }
        return Optional.empty();
    }

    public int bestTagFor(Game.Field.AprilTagLocation location) {
        setPipeline(APRIL_TAGS);

        if (!getResult().hasTargets()) {
            return 0;
        }
        if (!location.has(getResult().getBestTarget().getFiducialId())) {
            return 0;
        }

        return getResult().getBestTarget().getFiducialId();
    }

    public Optional<Pose3d> getTag(int id) {
        return getTagLayout().getTagPose(id);
    }

    public void useVisionForPoseEstimation(boolean useVision) {
        useVisionForEstimation = useVision;
    }

    public PhotonPipelineResult getResult() {
        return camera.getLatestResult();
    }

    public VisionPipeline getPipeline() {
        return VisionPipeline.fromIndex(camera.getPipelineIndex());
    }

    public void setPipeline(VisionPipeline pipeline) {
        camera.setPipelineIndex(pipeline.getIndex());
    }
}
