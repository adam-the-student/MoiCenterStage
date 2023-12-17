package org.firstinspires.ftc.teamcode.Autons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Opencv.AprilTagMultiThreadable;

@Autonomous
public class APTAlign extends LinearOpMode {
    // UNITS ARE METERS
    double backDropTagSize = 0.046;
    int[] ID_TAG_OF_INTEREST = {4,5,6}; // Tag ID 18 from the 36h11 family

    DcMotor motor1,motor2,motor3,motor4;

    @Override
    public void runOpMode() throws InterruptedException {
        motor1 = hardwareMap.get(DcMotor.class, "frontLeft");
        motor2 = hardwareMap.get(DcMotor.class,"frontRight");
        motor3 = hardwareMap.get(DcMotor.class,"backLeft");
        motor4 = hardwareMap.get(DcMotor.class,"backRight");

        AprilTagMultiThreadable backDrop = new AprilTagMultiThreadable(backDropTagSize,ID_TAG_OF_INTEREST,this);
        waitForStart();

        //camera = 5.75 inch
        // backdrop tags are at 3√3 inch
        //both are y pos^^ => difference is .55

        //pitch should be 30º
        if (-.2<backDrop.getPose().x&&backDrop.getPose().x<.2 || backDrop.getPose().z>1.4){

        }
    }
}
