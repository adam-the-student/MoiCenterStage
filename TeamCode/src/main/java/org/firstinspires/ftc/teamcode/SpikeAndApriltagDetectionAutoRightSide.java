package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Opencv.CvMaster;
import org.firstinspires.ftc.teamcode.Opencv.Pipelines.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.Opencv.Pipelines.SpikeDetectionThreeZone;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@Autonomous(name = "Spike+AprilTag Detection Right")
public class SpikeAndApriltagDetectionAutoRightSide extends LinearOpMode {
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    OpenCvCamera camera;
    static final double FEET_PER_METER = 3.28084;
    DcMotorEx motor1, motor2, motor3, motor4;
    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    int[] ID_TAGS_OF_INTEREST = {1,2,3}; // Tag ID 18 from the 36h11 family

    AprilTagDetection tagOfInterest = null;
    @Override
    public void runOpMode() throws InterruptedException {
        motor1 = hardwareMap.get(DcMotorEx.class, "frontRight");
        motor2 = hardwareMap.get(DcMotorEx.class,"frontRight");
        motor3 = hardwareMap.get(DcMotorEx.class,"backLeft");
        motor4 = hardwareMap.get(DcMotorEx.class,"backRight");
        int spikeZone = 0;
            CvMaster cam1 = new CvMaster(this, new SpikeDetectionThreeZone());
            cam1.runPipeline();
        while (!isStarted() && !isStopRequested()) {
            spikeZone = cam1.getZone();
            telemetry.addData("Camera 1 Zone: ", spikeZone);
            telemetry.update();
        }
        // just started
            cam1.stopCamera();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });
        lineFromVector(0,1,24);
        


        telemetry.setMsTransmissionInterval(50);
        boolean tagFound = false;
        while (!tagFound)
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                for(AprilTagDetection tag : currentDetections)
                {
                    for (int tagID:ID_TAGS_OF_INTEREST) {
                        if (tag.id == tagID) {
                            tagOfInterest = tag;
                            tagFound = true;
                            break;
                        }
                    }
                }
                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }
            }
            telemetry.update();
            sleep(20);
        }

    }
    public void lineFromVector(double xVector, double yVector, double distance){

        motor1.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        motor1.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motor3.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motor4.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        double denominator = Math.max(Math.abs(yVector) + Math.abs(xVector), 1);
        double motor1Power = (yVector + xVector) / denominator;  //motor1 is top left corner
        double motor2Power = (yVector - xVector) / denominator;  //motor2 is top right corner
        double motor3Power = (yVector - xVector) / denominator;  //motor3 is bottom left corner
        double motor4Power = (yVector + xVector) / denominator;  //motor4 is bottom right corner
        int targetDistance = (int)(distance / Math.sqrt(Math.pow(yVector,2)+Math.pow(xVector,2))*103.6);

        motor1.setTargetPosition(-targetDistance);
        motor2.setTargetPosition(targetDistance);
        motor3.setTargetPosition(targetDistance);
        motor4.setTargetPosition(-targetDistance);

        motor1.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        motor3.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        motor4.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        while (!(motor1.getTargetPosition()-10 <= motor1.getCurrentPosition()&&motor1.getTargetPosition()+10 >= motor1.getCurrentPosition())||!(motor2.getTargetPosition()-10 <= motor2.getCurrentPosition()&&motor2.getTargetPosition()+10 >= motor2.getCurrentPosition())||!(motor3.getTargetPosition()-10 <= motor3.getCurrentPosition()&&motor3.getTargetPosition()+10 >= motor3.getCurrentPosition())||!(motor4.getTargetPosition()-10 <= motor4.getCurrentPosition()&&motor4.getTargetPosition()+10 >= motor4.getCurrentPosition())){
            motor1.setPower(motor1Power*(1.1-((double)motor1.getCurrentPosition()/motor1.getTargetPosition())));
            motor2.setPower(motor2Power*(1.1-((double)motor2.getCurrentPosition()/motor2.getTargetPosition())));
            motor3.setPower(motor3Power*(1.1-((double)motor3.getCurrentPosition()/motor3.getTargetPosition())));
            motor4.setPower(motor4Power*(1.1-((double)motor4.getCurrentPosition()/motor4.getTargetPosition())));
            telemetry.addData("motor1 Position: ", motor1.getCurrentPosition());
            telemetry.addData("motor2 position: ", motor2.getCurrentPosition());
            telemetry.addData("motor3 Position: ", motor3.getCurrentPosition());
            telemetry.addData("motor4 position: ", motor4.getCurrentPosition());
            telemetry.update();
        }

        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);

    }
    void tagToTelemetry(AprilTagDetection detection)
    {
        Orientation rot = Orientation.getOrientation(detection.pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.DEGREES);

        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", rot.firstAngle));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", rot.secondAngle));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", rot.thirdAngle));
    }
}
