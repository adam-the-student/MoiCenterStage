package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "test!")
public class test extends LinearOpMode {
    private DcMotor intakeMotor;

    @Override
    public void runOpMode(){
//        motor1 = hardwareMap.get(DcMotor.class , "frontLeft");
//        motor2 = hardwareMap.get(DcMotor.class , "frontRight");
//        motor3 = hardwareMap.get(DcMotor.class , "backLeft");
//        motor4 = hardwareMap.get(DcMotor.class , "backRight");
//        slideMotor = hardwareMap.get(DcMotor.class, "slideMotor");
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
//        Bs = hardwareMap.get(Servo.class,"bigServo");
//        locker = hardwareMap.get(CRServo.class,"locker");
//        ignition = hardwareMap.get(Servo.class,"ign");

        waitForStart();
        while (opModeIsActive()) {

            if (gamepad2.right_trigger!=0){
                intakeMotor.setPower(1);
            } else if (gamepad2.left_trigger!=0) {
                intakeMotor.setPower(-1);
            } else {
                intakeMotor.setPower(0);
            }


        }
    }
}
