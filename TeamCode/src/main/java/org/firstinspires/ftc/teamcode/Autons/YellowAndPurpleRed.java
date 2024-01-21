package org.firstinspires.ftc.teamcode.Autons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Opencv.CvMaster;
import org.firstinspires.ftc.teamcode.Opencv.Pipelines.workspace.SpikeZoneDetectionRed;

@Autonomous(name = "2' + 0 Red BackDrop")
public class YellowAndPurpleRed extends LinearOpMode {
    DcMotor motor1, motor2, motor3, motor4, slideMotor, slideAngle;
    Servo yellow;
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

        CvMaster<SpikeZoneDetectionRed> cam1 = new CvMaster<>(this, new SpikeZoneDetectionRed());
        cam1.runPipeline();
        byte spikeZone=0;
        yellow.setPosition(.8);
        sleep(100);

        while(!opModeIsActive()){
            spikeZone = (byte)cam1.getZone();
            telemetry.addData("Camera 1 Zone: ", spikeZone);
            telemetry.update();
            sleep(200);
        }
        cam1.stopCamera();

        //537.7 ticks per rev
        //13.5 track width
        // 13.5 * Math.PI / 2

        if (spikeZone==2) {
            baseController((int) (TICKS_PER_INCH * -30), (int) (TICKS_PER_INCH * 30), (int) (TICKS_PER_INCH * -30), (int) (TICKS_PER_INCH * 30));
        } else {
            baseController((int)(TICKS_PER_INCH*-30),(int)(TICKS_PER_INCH*30),(int)(TICKS_PER_INCH*-30),(int)(TICKS_PER_INCH*30));
            if (spikeZone==3){
                baseController((int)(-TICKS_PER_INCH*13.5 * Math.PI/2),(int)(-TICKS_PER_INCH*13.5 * Math.PI/2),(int)(-TICKS_PER_INCH*13.5 * Math.PI/2),(int)(-TICKS_PER_INCH*13.5 * Math.PI/2));
            } else {
                baseController((int)(TICKS_PER_INCH*13.5 * Math.PI / 2),(int)(TICKS_PER_INCH*13.5 * Math.PI / 2),(int)(TICKS_PER_INCH*13.5 * Math.PI / 2),(int)(TICKS_PER_INCH*13.5 * Math.PI / 2));
            }
        }
        baseController((int)(TICKS_PER_INCH*-5),(int)(TICKS_PER_INCH*5),(int)(TICKS_PER_INCH*-5),(int)(TICKS_PER_INCH*5));
        baseController((int)(TICKS_PER_INCH*2),(int)(TICKS_PER_INCH*-2),(int)(TICKS_PER_INCH*2),(int)(TICKS_PER_INCH*-2));

        if (spikeZone == 3){
            baseController((int)(TICKS_PER_INCH*2),(int)(TICKS_PER_INCH*-2),(int)(TICKS_PER_INCH*2),(int)(TICKS_PER_INCH*-2));
            baseController((int)(-TICKS_PER_INCH*13.5 * Math.PI/2*.1),(int)(-TICKS_PER_INCH*13.5 * Math.PI/2*.1),(int)(-TICKS_PER_INCH*13.5 * Math.PI/2*.1),(int)(-TICKS_PER_INCH*13.5 * Math.PI/2*.1));
            baseController((int)(TICKS_PER_INCH*-36),(int)(TICKS_PER_INCH*36),(int)(TICKS_PER_INCH*-36),(int)(TICKS_PER_INCH*36));
        } else if (spikeZone == 2){
            baseController((int)(TICKS_PER_INCH*4.5),(int)(TICKS_PER_INCH*-4.5),(int)(TICKS_PER_INCH*4.5),(int)(TICKS_PER_INCH*-4.5));
            baseController((int)(-TICKS_PER_INCH*13.5 * Math.PI/2*1.1),(int)(-TICKS_PER_INCH*13.5 * Math.PI/2*1.1),(int)(-TICKS_PER_INCH*13.5 * Math.PI/2*1.1),(int)(-TICKS_PER_INCH*13.5 * Math.PI/2*1.1));
            baseController((int)(TICKS_PER_INCH*-43),(int)(TICKS_PER_INCH*43),(int)(TICKS_PER_INCH*-43),(int)(TICKS_PER_INCH*43));
        } else {
            baseController((int)(TICKS_PER_INCH*20),(int)(TICKS_PER_INCH*-20),(int)(TICKS_PER_INCH*20),(int)(TICKS_PER_INCH*-20));
            baseController((int)(TICKS_PER_INCH*13.5 * Math.PI * 1.25),(int)(TICKS_PER_INCH*13.5 * Math.PI * 1.25),(int)(TICKS_PER_INCH*13.5 * Math.PI * 1.25),(int)(TICKS_PER_INCH*13.5 * Math.PI * 1.25));
            baseController((int)(TICKS_PER_INCH*-14),(int)(TICKS_PER_INCH*14),(int)(TICKS_PER_INCH*-14),(int)(TICKS_PER_INCH*14));
            baseController(-(int)(TICKS_PER_INCH*13.5 * Math.PI * .2),-(int)(TICKS_PER_INCH*13.5 * Math.PI * .2),-(int)(TICKS_PER_INCH*13.5 * Math.PI * .2),-(int)(TICKS_PER_INCH*13.5 * Math.PI * .2));
            baseController((int)(TICKS_PER_INCH*-10),(int)(TICKS_PER_INCH*10),(int)(TICKS_PER_INCH*-10),(int)(TICKS_PER_INCH*10));
        }

        yellow.setPosition(.45);
        sleep(1000);
        baseController((int)(TICKS_PER_INCH*4),(int)(TICKS_PER_INCH*-4),(int)(TICKS_PER_INCH*4),(int)(TICKS_PER_INCH*-4));
        yellow.setPosition(.8);
        baseController(-(int)(TICKS_PER_INCH*16),-(int)(TICKS_PER_INCH*16),(int)(TICKS_PER_INCH*16),(int)(TICKS_PER_INCH*16));

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

        while ((Math.abs(error1) > 25)||(Math.abs(error2) > 25)||(Math.abs(error3) > 25)||(Math.abs(error4) > 25)) { // <= change this value for your error range /  reliability.
            motor1.setPower((motor1.getPower() + (error1 / targetDestination1)) / 2);
            error1 = motor1.getTargetPosition()-motor1.getCurrentPosition();
            motor2.setPower((motor2.getPower() + (error2 / targetDestination2)) / 2);
            error2 = motor2.getTargetPosition()-motor2.getCurrentPosition();
            motor3.setPower((motor3.getPower() + (error3 / targetDestination3)) / 2);
            error3 = motor3.getTargetPosition()-motor3.getCurrentPosition();
            motor4.setPower((motor4.getPower() + (error4 / targetDestination4)) / 2);
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
