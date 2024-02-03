package org.firstinspires.ftc.teamcode.Autons;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Opencv.CvMaster;
import org.firstinspires.ftc.teamcode.Opencv.Pipelines.workspace.SpikeZoneDetectionBlue;
import org.firstinspires.ftc.teamcode.Opencv.Pipelines.workspace.SpikeZoneDetectionRed;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "2' + 0 Blue BackDrop")
public class YellowAndPurpleBlue extends LinearOpMode {
    DcMotor motor1, motor2, motor3, motor4, slideMotor, slideAngle;
    Servo yellow;
    CRServo wrist;
    RevColorSensorV3 leftDistance, rightDistance;
    private final double TICKS_PER_REV = 537.7;
    private final double TICKS_PER_INCH = TICKS_PER_REV/11.87;

    @Override
    public void runOpMode() throws InterruptedException {
        motor1 = hardwareMap.get(DcMotor.class, "frontLeft");
        motor2 = hardwareMap.get(DcMotor.class, "frontRight");
        motor3 = hardwareMap.get(DcMotor.class, "backLeft");
        motor4 = hardwareMap.get(DcMotor.class, "backRight");
        slideMotor = hardwareMap.get(DcMotor.class, "slideRig");
        slideAngle = hardwareMap.get(DcMotor.class, "wormDrive");
        yellow = hardwareMap.get(Servo.class,"yellowPixel");
        wrist = hardwareMap.get(CRServo.class, "wrist");

        CvMaster<SpikeZoneDetectionBlue> cam1 = new CvMaster<>(this, new SpikeZoneDetectionBlue());
        cam1.runPipeline();
        byte spikeZone=1;
        yellow.setPosition(.8);
        sleep(100);

        while(!opModeIsActive()){
            spikeZone = (byte)cam1.getZone();
            telemetry.addData("Camera 1 Zone: ", spikeZone);
            telemetry.update();
            sleep(500);
        }
        wrist.setPower(1);
        sleep(1000);
        wrist.setPower(0);
        cam1.stopCamera();
        //537.7 ticks per rev
        //13.5 track width
        // 13.5 * Math.PI / 2

        if (spikeZone==2){
            baseController((int)(TICKS_PER_INCH*-33),(int)(TICKS_PER_INCH*33),(int)(TICKS_PER_INCH*-33),(int)(TICKS_PER_INCH*33));
            baseController((int)(TICKS_PER_INCH*7),(int)(TICKS_PER_INCH*-7),(int)(TICKS_PER_INCH*7),(int)(TICKS_PER_INCH*-7));
            baseController((int)(TICKS_PER_INCH*Math.PI*14/2),(int)(TICKS_PER_INCH*Math.PI*14/2),(int)(TICKS_PER_INCH*Math.PI*14/2),(int)(TICKS_PER_INCH*Math.PI*14/2));
            baseController((int)(TICKS_PER_INCH*-39),(int)(TICKS_PER_INCH*39),(int)(TICKS_PER_INCH*-39),(int)(TICKS_PER_INCH*39));
            baseController((int)(TICKS_PER_INCH*-6),(int)(TICKS_PER_INCH*-6),(int)(TICKS_PER_INCH*6),(int)(TICKS_PER_INCH*6));
        } else if (spikeZone==3){
            baseController((int)(TICKS_PER_INCH*-29),(int)(TICKS_PER_INCH*29),(int)(TICKS_PER_INCH*-29),(int)(TICKS_PER_INCH*29));
            baseController((int)(TICKS_PER_INCH*Math.PI*-14/2),(int)(TICKS_PER_INCH*Math.PI*-14/2),(int)(TICKS_PER_INCH*Math.PI*-14/2),(int)(TICKS_PER_INCH*Math.PI*-14/2));
            baseController((int)(TICKS_PER_INCH*-5),(int)(TICKS_PER_INCH*5),(int)(TICKS_PER_INCH*-5),(int)(TICKS_PER_INCH*5));
            baseController((int)(TICKS_PER_INCH*14),(int)(TICKS_PER_INCH*-14),(int)(TICKS_PER_INCH*14),(int)(TICKS_PER_INCH*-14));
            baseController((int)(TICKS_PER_INCH*Math.PI*14),(int)(TICKS_PER_INCH*Math.PI*14),(int)(TICKS_PER_INCH*Math.PI*14),(int)(TICKS_PER_INCH*Math.PI*14));
            baseController((int)(TICKS_PER_INCH*-33),(int)(TICKS_PER_INCH*33),(int)(TICKS_PER_INCH*-33),(int)(TICKS_PER_INCH*33));
            baseController((int)(TICKS_PER_INCH*-8),(int)(TICKS_PER_INCH*-8),(int)(TICKS_PER_INCH*8),(int)(TICKS_PER_INCH*8));
        } else {
            baseController((int)(TICKS_PER_INCH*-8),(int)(TICKS_PER_INCH*8),(int)(TICKS_PER_INCH*-8),(int)(TICKS_PER_INCH*8));
            baseController((int)(TICKS_PER_INCH*14),(int)(TICKS_PER_INCH*14),(int)(TICKS_PER_INCH*-14),(int)(TICKS_PER_INCH*-14));
            baseController((int)(TICKS_PER_INCH*-20),(int)(TICKS_PER_INCH*20),(int)(TICKS_PER_INCH*-20),(int)(TICKS_PER_INCH*20));
            baseController((int)(TICKS_PER_INCH*8),(int)(TICKS_PER_INCH*-8),(int)(TICKS_PER_INCH*8),(int)(TICKS_PER_INCH*-8));
            baseController((int)(TICKS_PER_INCH*Math.PI*14/2),(int)(TICKS_PER_INCH*Math.PI*14/2),(int)(TICKS_PER_INCH*Math.PI*14/2),(int)(TICKS_PER_INCH*Math.PI*14/2));
            baseController((int)(TICKS_PER_INCH*-28),(int)(TICKS_PER_INCH*28),(int)(TICKS_PER_INCH*-28),(int)(TICKS_PER_INCH*28));
            baseController((int)(TICKS_PER_INCH*-6),(int)(TICKS_PER_INCH*-6),(int)(TICKS_PER_INCH*6),(int)(TICKS_PER_INCH*6));
        }
        yellow.setPosition(.37);
        sleep(1000);
        baseController((int)(TICKS_PER_INCH*4),(int)(TICKS_PER_INCH*-4),(int)(TICKS_PER_INCH*4),(int)(TICKS_PER_INCH*-4));
         yellow.setPosition(.9);
        baseController((int)(TICKS_PER_INCH*3),(int)(TICKS_PER_INCH*-3),(int)(TICKS_PER_INCH*3),(int)(TICKS_PER_INCH*-3));
        if(spikeZone==1){
            baseController((int)(TICKS_PER_INCH*26),(int)(TICKS_PER_INCH*26),(int)(TICKS_PER_INCH*-26),(int)(TICKS_PER_INCH*-26));
        } else if (spikeZone==3){
            baseController((int)(TICKS_PER_INCH*35),(int)(TICKS_PER_INCH*35),(int)(TICKS_PER_INCH*-35),(int)(TICKS_PER_INCH*-35));
        } else {
            baseController((int)(TICKS_PER_INCH*28),(int)(TICKS_PER_INCH*28),(int)(TICKS_PER_INCH*-28),(int)(TICKS_PER_INCH*-28));
        }

    }
    public void motorController(DcMotor motor, int targetDestination, int startingPos){

        double error = targetDestination-startingPos;

        motor.setTargetPosition(targetDestination);

        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (Math.abs(error) > 10) { // <= change this value for your error range /  reliability.
            motor.setPower((motor.getPower() + (error / targetDestination)) / 2);
            error = motor.getTargetPosition()-motor.getCurrentPosition();
            telemetry.addData("Position: ",motor.getCurrentPosition());
            telemetry.addData("Error: ", error);
            telemetry.update();
        }
        motor.setPower(0);
    }

    public void baseController(int targetDestination1, int targetDestination2, int targetDestination3, int targetDestination4){
//1 front left
//2 front right
//3 back left
//4 back right
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double error1 = targetDestination1;
        motor1.setTargetPosition(targetDestination1);
        double error2 = targetDestination2;
        motor2.setTargetPosition(targetDestination2);
        double error3 = targetDestination3;
        motor3.setTargetPosition(targetDestination3);
        double error4 = targetDestination4;
        motor4.setTargetPosition(targetDestination4);

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor4.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while ((Math.abs(error1) > 30)||(Math.abs(error2) > 30)||(Math.abs(error3) > 30)||(Math.abs(error4) > 30)) { // <= change this value for your error range /  reliability.
            motor1.setPower(.8*(motor1.getPower() + (error1 / targetDestination1)) / 2);
            error1 = motor1.getTargetPosition()-motor1.getCurrentPosition();
            motor2.setPower(.8*(motor2.getPower() + (error2 / targetDestination2)) / 2);
            error2 = motor2.getTargetPosition()-motor2.getCurrentPosition();
            motor3.setPower(.8*(motor3.getPower() + (error3 / targetDestination3)) / 2);
            error3 = motor3.getTargetPosition()-motor3.getCurrentPosition();
            motor4.setPower(.8*(motor4.getPower() + (error4 / targetDestination4)) / 2);
            error4 = motor4.getTargetPosition()-motor4.getCurrentPosition();

            telemetry.addData("Pos 1: ",motor1.getCurrentPosition());
            telemetry.addData("Error 1: ", error1);
            telemetry.addData("Pos 2: ",motor2.getCurrentPosition());
            telemetry.addData("Error 2: ", error2);
            telemetry.addData("Pos 3: ",motor3.getCurrentPosition());
            telemetry.addData("Error 3: ", error3);
            telemetry.addData("Pos 4: ",motor4.getCurrentPosition());
            telemetry.addData("Error 4: ", error4);
            telemetry.update();
        }
        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);
    }
}
