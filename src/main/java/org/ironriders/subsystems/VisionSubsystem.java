package org.ironriders.subsystems;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.ironriders.lib.VisionPipeline;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.common.hardware.VisionLEDMode;

import java.io.IOException;
import java.util.Optional;

import static org.ironriders.constants.Robot.LIMELIGHT_POSITION;
import static org.ironriders.lib.VisionPipeline.APRIL_TAGS;
import static org.photonvision.PhotonPoseEstimator.PoseStrategy.AVERAGE_BEST_TARGETS;

public class VisionSubsystem extends SubsystemBase {
    private final PhotonCamera camera = new PhotonCamera("LIMELIGHT");
    private final PhotonPoseEstimator estimator;
    private final AprilTagFieldLayout tagLayout;

    public VisionSubsystem() {
        try {
            tagLayout = new AprilTagFieldLayout(Filesystem.getDeployDirectory() + "/apriltags/2018-powerup.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        estimator = new PhotonPoseEstimator(tagLayout, AVERAGE_BEST_TARGETS, camera, LIMELIGHT_POSITION);

        camera.setLED(VisionLEDMode.kOff);
        camera.setDriverMode(false);
        setPipeline(APRIL_TAGS);
    }

    public AprilTagFieldLayout getTagLayout() {
        return tagLayout;
    }

    public Optional<EstimatedRobotPose> getPoseEstimate() {
        return estimator.update();
    }

    public PhotonCamera getCamera() {
        return camera;
    }

    public VisionPipeline getPipeline() {
        return VisionPipeline.fromIndex(camera.getPipelineIndex());
    }

    public void setPipeline(VisionPipeline pipeline) {
        camera.setPipelineIndex(pipeline.getIndex());
    }
}
