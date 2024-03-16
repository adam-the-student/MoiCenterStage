package org.firstinspires.ftc.teamcode.Autons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Opencv.CvMaster;
import org.firstinspires.ftc.teamcode.Opencv.Pipelines.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.Opencv.Pipelines.workspace.SpikeZoneDetectionRed;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;

@Autonomous(name = "Spike+AprilTag Detection Right")
public class SpikeAndApriltagDetectionAutoRightSide extends LinearOpMode {
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    OpenCvCamera camera;
    static final double FEET_PER_METER = 3.28084;
    DcMotor motor1, motor2, motor3, motor4, intakeMotor;
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

    int[] ID_TAG_OF_INTEREST = {1,2,3,4,5,6};

    AprilTagDetection tagOfInterest = null;
    @Override
    public void runOpMode() throws InterruptedException {
        motor1 = hardwareMap.get (DcMotor.class, "frontLeft");
        motor2 = hardwareMap.get(DcMotor.class,"frontRight");
        motor3 = hardwareMap.get(DcMotor.class,"backLeft");
        motor4 = hardwareMap.get(DcMotor.class,"backRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        int spikeZone = 0;
            CvMaster cam1 = new CvMaster(this, new SpikeZoneDetectionRed());
            cam1.runPipeline();
        while (!isStarted()) {
            spikeZone = cam1.getZone();
            telemetry.addData("Camera 1 Zone: ", spikeZone);
            telemetry.update();
            sleep(3000);
        }
        for (int i = 0; i < 2; i++) {
            spikeZone = cam1.getZone();
            sleep(1000);
        }
        // just started
            cam1.stopCamera();

        lineFromVector(0,1,4);
        sleep(500);

        if(spikeZone==1){
            turn(1.8);
        } else if (spikeZone==3){
            turn(-1.8);
        }
        sleep(500);
        intakeMotor.setPower(.75);
        sleep(1000);
        intakeMotor.setPower(0);

//        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
//        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);
//
//        camera.setPipeline(aprilTagDetectionPipeline);
//        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
//        {
//            @Override
//            public void onOpened()
//            {
//                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
//            }
//
//            @Override
//            public void onError(int errorCode)
//            {
//
//            }
//        });
//
//        telemetry.setMsTransmissionInterval(50);
//        boolean tagFound = false;
//        while (!tagFound)
//        {
//            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();
//
//            if(currentDetections.size() != 0)
//            {
//                for(AprilTagDetection tag : currentDetections)
//                {
//                    for (int THETAGOFINTEREST:ID_TAG_OF_INTEREST) {
//                        if (tag.id ==THETAGOFINTEREST) {
//                            tagOfInterest = tag;
//                            tagFound = true;
//                            break;
//                        }
//                    }
//                }
//
//                if(tagFound)
//                {
//                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
//                    tagToTelemetry(tagOfInterest);
//                }
//                else
//                {
//                    telemetry.addLine("Don't see tag of interest :(");
//
//                    if(tagOfInterest == null)
//                    {
//                        telemetry.addLine("(The tag has never been seen)");
//                    }
//                    else
//                    {
//                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
//                        tagToTelemetry(tagOfInterest);
//                    }
//                }
//
//            }
//            else
//            {
//                telemetry.addLine("Don't see tag of interest :(");
//
//                if(tagOfInterest == null)
//                {
//                    telemetry.addLine("(The tag has never been seen)");
//                }
//                else
//                {
//                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
//                    tagToTelemetry(tagOfInterest);
//                }
//
//            }
//
//            telemetry.update();
//            sleep(20);
//        }

    }
    public void turn(double rot){

        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motor1.setTargetPosition((int)(rot*103.6));
        motor2.setTargetPosition((int)(rot*103.6));
        motor3.setTargetPosition((int)(rot*103.6));
        motor4.setTargetPosition((int)(rot*103.6));

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor4.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motor1.setPower(-.2);  // motor1 is top left
        motor2.setPower(.2);  // motor2 is top right
        motor3.setPower(.2);  // motor3 is bottom left
        motor4.setPower(.2);

        while (!(motor1.getTargetPosition()-10 <= motor1.getCurrentPosition()&&motor1.getTargetPosition()+10 >= motor1.getCurrentPosition())&&!(motor2.getTargetPosition()-10 <= motor2.getCurrentPosition()&&motor2.getTargetPosition()+10 >= motor2.getCurrentPosition())&&!(motor3.getTargetPosition()-10 <= motor3.getCurrentPosition()&&motor3.getTargetPosition()+10 >= motor3.getCurrentPosition())&&!(motor4.getTargetPosition()-10 <= motor4.getCurrentPosition()&&motor4.getTargetPosition()+10 >= motor4.getCurrentPosition())){
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

    public void lineFromVector(double xVector, double yVector, double distance){

        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double denominator = Math.max(Math.abs(yVector) + Math.abs(xVector), 1);
        double motor1Power = (yVector - xVector) / denominator;
        double motor2Power = (yVector + xVector) / denominator;
        double motor3Power = (yVector + xVector) / denominator;
        double motor4Power = (yVector - xVector) / denominator;

        int targetDistance = (int)(distance / Math.sqrt(Math.pow(yVector,2)+Math.pow(xVector,2))*103.6);

        motor1.setTargetPosition(targetDistance);
        motor2.setTargetPosition(-targetDistance);
        motor3.setTargetPosition(targetDistance);
        motor4.setTargetPosition(-targetDistance);

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor4.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motor1.setPower(motor1Power/5);  // motor1 is top left
        motor2.setPower(motor2Power/5);  // motor2 is top right
        motor3.setPower(-motor3Power/5);  // motor3 is bottom left
        motor4.setPower(motor4Power/5);

        while (!(motor1.getTargetPosition()-10 <= motor1.getCurrentPosition()&&motor1.getTargetPosition()+10 >= motor1.getCurrentPosition())&&!(motor2.getTargetPosition()-10 <= motor2.getCurrentPosition()&&motor2.getTargetPosition()+10 >= motor2.getCurrentPosition())&&!(motor3.getTargetPosition()-10 <= motor3.getCurrentPosition()&&motor3.getTargetPosition()+10 >= motor3.getCurrentPosition())&&!(motor4.getTargetPosition()-10 <= motor4.getCurrentPosition()&&motor4.getTargetPosition()+10 >= motor4.getCurrentPosition())){
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