package org.firstinspires.ftc.teamcode.Autons;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class APTAlign {

    private DcMotor motor1,motor2,motor3,motor4;

    // Adjust these numbers to suit your robot.
    final double DESIRED_DISTANCE = 14.0; //  this is how close the camera should get to the target (inches)

    //  Set the GAIN constants to control the relationship between the measured position error, and how much power is
    //  applied to the drive motors to correct the error.
    //  Drive = Error * Gain    Make these values smaller for smoother control, or larger for a more aggressive response.
    final double SPEED_GAIN  =  0.02  ;   //  Forward Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN =  0.015 ;   //  Strafe Speed Control "Gain".  eg: Ramp up to 25% power at a 25 degree Yaw error.   (0.25 / 25.0)
    final double TURN_GAIN   =  0.015  ;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MAX_AUTO_SPEED = 0.6;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_STRAFE= 0.6;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)

    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
    private static int DESIRED_TAG_ID = 2;     // Choose the tag you want to approach or set to -1 for ANY tag.
    private VisionPortal visionPortal;               // Used to manage the video source.
    private AprilTagProcessor aprilTag;              // Used for managing the AprilTag detection process.
    private AprilTagDetection desiredTag = null;     // Used to hold the data for a detected AprilTag

    private boolean targetFound     = false;    // Set to true when an AprilTag target is detected
    private double  drive           = 0;        // Desired forward power/speed (-1 to +1)
    private double  strafe          = 0;        // Desired strafe power/speed (-1 to +1)
    private double  turn            = 0;        // Desired turning power/speed (-1 to +1)

    LinearOpMode quack;

    public APTAlign(LinearOpMode l_op){
        quack = l_op;

        initAprilTag();

        motor1 = quack.hardwareMap.get(DcMotor.class, "frontLeft");
        motor2 = quack.hardwareMap.get(DcMotor.class, "frontRight");
        motor3 = quack.hardwareMap.get(DcMotor.class, "backLeft");
        motor4 = quack.hardwareMap.get(DcMotor.class, "backRight");


        if (USE_WEBCAM)
            setManualExposure(6, 250);  // Use low exposure time to reduce motion blur
    }

    public void setDesiredTagId(int desiredTagId){
        DESIRED_TAG_ID = desiredTagId;
    }

    public void runToTag(int destinationTag) {
        DESIRED_TAG_ID = destinationTag;

        targetFound = false;
        desiredTag = null;
        ElapsedTime smolTimer = new ElapsedTime();

        smolTimer.startTime();

        double rangeError = 0;
        List<AprilTagDetection> currentDetections;
        smolTimer.reset();

        while (smolTimer.time(TimeUnit.MILLISECONDS)<1000) {

            // Step through the list of detected tags and look for a matching tag
            currentDetections = aprilTag.getDetections();
            for (AprilTagDetection detection : currentDetections) {
                if ((detection.metadata != null) &&
                        ((DESIRED_TAG_ID < 0) || (detection.id == DESIRED_TAG_ID))) {
                    targetFound = true;
                    desiredTag = detection;
                    break;  // don't look any further.
                } else {
                    quack.telemetry.addData("Unknown Target", "Tag ID %d is not in TagLibrary\n", detection.id);
                }
            }

            // Tell the driver what we see, and what to do.
            if (targetFound) {
                quack.telemetry.addData("Target", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
                quack.telemetry.addData("Range", "%5.1f inches", desiredTag.ftcPose.range);
                quack.telemetry.addData("Bearing", "%3.0f degrees", desiredTag.ftcPose.bearing);
                quack.telemetry.addData("Yaw", "%3.0f degrees", desiredTag.ftcPose.yaw);
                rangeError = 20;
            } else {
                quack.telemetry.addData(">", "Drive using joysticks to find valid target\n");
            }
        }

        while (rangeError>=2) {

            // Step through the list of detected tags and look for a matching tag
            currentDetections = aprilTag.getDetections();
            for (AprilTagDetection detection : currentDetections) {
                if ((detection.metadata != null) &&
                        ((DESIRED_TAG_ID < 0) || (detection.id == DESIRED_TAG_ID))) {
                    targetFound = true;
                    desiredTag = detection;
                    break;  // don't look any further.
                } else {
                    quack.telemetry.addData("Unknown Target", "Tag ID %d is not in TagLibrary\n", detection.id);
                }
            }

            // Tell the driver what we see, and what to do.
            if (targetFound) {
                quack.telemetry.addData("Target", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
                quack.telemetry.addData("Range", "%5.1f inches", desiredTag.ftcPose.range);
                quack.telemetry.addData("Bearing", "%3.0f degrees", desiredTag.ftcPose.bearing);
                quack.telemetry.addData("Yaw", "%3.0f degrees", desiredTag.ftcPose.yaw);
            } else {
                quack.telemetry.addData(">", "Drive using joysticks to find valid target\n");
            }

            // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
            rangeError      = (desiredTag.ftcPose.range - DESIRED_DISTANCE);
            double  headingError    = desiredTag.ftcPose.bearing;
            double  yawError        = desiredTag.ftcPose.yaw;

            // Use the speed and turn "gains" to calculate how we want the robot to move.
            drive  = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
            turn   = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN) ;
            strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);

            quack.telemetry.addData("Auto","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
            quack.telemetry.update();

            // Apply desired axes motions to the drivetrain.
            moveRobot(drive, strafe, turn);
            quack.sleep(10);
        }
    }

    public void moveRobot(double x, double y, double yaw) {
        // Calculate wheel powers.
        double leftFrontPower    =  x -y -yaw;
        double rightFrontPower   =  x +y +yaw;
        double leftBackPower     =  x +y -yaw;
        double rightBackPower    =  x -y +yaw;

        // Normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }


        // Send powers to the wheels.
        motor1.setPower(-leftFrontPower);
        motor2.setPower(rightFrontPower);
        motor3.setPower(-leftBackPower);
        motor4.setPower(rightBackPower);
    }

    /**
     * Initialize the AprilTag processor.
     */
    private void initAprilTag() {
        // Create the AprilTag processor by using a builder.
        aprilTag = new AprilTagProcessor.Builder().setLensIntrinsics(816.434 , 816.434 , 337.687, 234.308).build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        // Note: Decimation can be changed on-the-fly to adapt during a match.


        // Create the vision portal by using a builder.
        if (USE_WEBCAM) {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(quack.hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(aprilTag)
                    .build();
        } else {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(BuiltinCameraDirection.BACK)
                    .addProcessor(aprilTag)
                    .build();
        }
    }
    /*
     Manually set the camera gain and exposure.
     This can only be called AFTER calling initAprilTag(), and only works for Webcams;
    */
    private void    setManualExposure(int exposureMS, int gain) {
        // Wait for the camera to be open, then use the controls

        if (visionPortal == null) {
            return;
        }

        // Make sure camera is streaming before we try to set the exposure controls
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            quack.telemetry.addData("Camera", "Waiting");
            quack.telemetry.update();
            while (!quack.isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
                quack.sleep(20);
            }
            quack.telemetry.addData("Camera", "Ready");
            quack.telemetry.update();
        }

        // Set camera controls unless we are stopping.
        if (!quack.isStopRequested())
        {
            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
                quack.sleep(50);
            }
            exposureControl.setExposure(exposureMS, TimeUnit.MILLISECONDS);
            quack.sleep(20);
            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
            quack.sleep(20);
        }
    }
}
