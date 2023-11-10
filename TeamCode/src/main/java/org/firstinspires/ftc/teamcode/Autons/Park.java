package org.firstinspires.ftc.teamcode.Autons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Park")
public class Park extends LinearOpMode {
    DcMotor motor1,motor2,motor3,motor4, intake, slide, leftWheel, rightWheel, sideWheel;
    CRServo lock, bs, fire;
    @Override
    public void runOpMode() throws InterruptedException {
        motor1 = hardwareMap.get(DcMotor.class , "frontLeft");
        motor2 = hardwareMap.get(DcMotor.class , "frontRight");
        motor3 = hardwareMap.get(DcMotor.class , "backLeft");
        motor4 = hardwareMap.get(DcMotor.class , "backRight");
        leftWheel = hardwareMap.get(DcMotor.class , "frontRight");
        rightWheel = hardwareMap.get(DcMotor.class , "backLeft");
        sideWheel = hardwareMap.get(DcMotor.class , "backRight");

        waitForStart();
        forward(3);

    }
    public void forward(int distance){
        leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sideWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        sideWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //target position
        leftWheel.setTargetPosition(-(int)(8192*distance*2*Math.PI));
        rightWheel.setTargetPosition((int)(8192*distance*2*Math.PI));
        sideWheel.setTargetPosition(0);

        leftWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sideWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motor1.setPower(.25);
        motor2.setPower(0.25);
        motor3.setPower(0.25);
        motor4.setPower(0.25);

        while (leftWheel.getCurrentPosition()+10<leftWheel.getTargetPosition()||rightWheel.getCurrentPosition()+10<rightWheel.getTargetPosition()||sideWheel.getCurrentPosition()+10<sideWheel.getTargetPosition()){
//            motor1.setPower((1-((double)leftWheel.getCurrentPosition()/leftWheel.getTargetPosition())));
//            motor3.setPower((1-((double)leftWheel.getCurrentPosition()/leftWheel.getTargetPosition())));
//            motor2.setPower((1-((double)leftWheel.getCurrentPosition()/leftWheel.getTargetPosition())));
//            motor4.setPower((1-((double)leftWheel.getCurrentPosition()/leftWheel.getTargetPosition())));
        }

        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);
    }
}