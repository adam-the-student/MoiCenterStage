package org.firstinspires.ftc.teamcode.Teleop;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "My TeleOp")
public class newintaketest extends LinearOpMode {

    DcMotor motor;
    Servo myServo;
    @Override
    public void runOpMode() {
        motor = hardwareMap.get(DcMotor.class, "motor");
        myServo = hardwareMap.get(Servo.class, "servo");

        waitForStart();

        motor.setPower(1);
        myServo.setPosition(0.5);

        sleep(5000);

        motor.setPower(-1);

        sleep(5000);

        motor.setPower(0);
    }
}