package org.firstinspires.ftc.teamcode.Opencv;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Opencv.Pipelines.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.apriltag.AprilTagPose;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

public class AprilTagMultiThreadable implements Runnable{

    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    LinearOpMode opMode;
    AprilTagDetection tag;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 816.434;
    double fy = 816.434;
    double cx = 337.687;
    double cy = 234.308;

    // UNITS ARE METERS
    double tagsize;

    int[] ID_TAG_OF_INTEREST = null; // Tag ID 18 from the 36h11 family

    AprilTagDetection tagOfInterest;

    public AprilTagMultiThreadable(double tagsize, int[] ID_TAG_OF_INTERESTS, LinearOpMode opMode){
        this.opMode = opMode;
        ID_TAG_OF_INTEREST = ID_TAG_OF_INTERESTS;
        this.tagsize = tagsize;
        int cameraMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(opMode.hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
    }

    @Override
    public void run() {
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(1280,720, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {}
        });
        opMode.telemetry.setMsTransmissionInterval(50);

        while (opMode.opModeIsActive()){
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tagseen : currentDetections)
                {
                    tag=tagseen;
                    for (int THETAGOFINTEREST:ID_TAG_OF_INTEREST) {
                        if (tag.id ==THETAGOFINTEREST) {
                            tagOfInterest = tag;
                            tagFound = true;
                            break;
                        }
                    }
                }

                if(tagFound)
                {
                    opMode.telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    opMode.telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        opMode.telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        opMode.telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                opMode.telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    opMode.telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    opMode.telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            opMode.telemetry.update();
        }
    }

    public AprilTagPose getPose(){
        return tag.pose;
    }

    @SuppressLint("DefaultLocale")
    void tagToTelemetry(AprilTagDetection detection)
    {
        Orientation rot = Orientation.getOrientation(detection.pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES);

        opMode.telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        opMode.telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        opMode.telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        opMode.telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        opMode.telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", rot.firstAngle));
        opMode.telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", rot.secondAngle));
        opMode.telemetry.addLine(String.format("Rotation Roll: %.2f degrees", rot.thirdAngle));
    }
}
