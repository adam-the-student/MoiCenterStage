package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImpl;

@TeleOp(name = "Meet TeleOP")
public class NewRobitsTeleOp extends LinearOpMode {
    DcMotor motor1,motor2, armMotor;
    Servo ignition, yellowPixel;
    @Override
    public void runOpMode(){
        motor1 = hardwareMap.get(DcMotor.class,"leftMotor");
        motor2 = hardwareMap.get(DcMotor.class, "rightMotor");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        ignition = hardwareMap.get(Servo.class,"ign");
        yellowPixel = hardwareMap.get(Servo.class,"yellowPixel");

        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        yellowPixel.scaleRange(0.6,.7);
        yellowPixel.setPosition(1);

        boolean e = false;
        while (opModeIsActive()){

            double verticalPower = gamepad1.right_trigger>0?-gamepad1.right_trigger:gamepad1.left_trigger;
            if (gamepad2.right_bumper){
                verticalPower/=4;
            }
            double turnPower = -gamepad1.left_stick_x/3;
            motor1.setPower((verticalPower+turnPower)/Math.max(1,verticalPower+turnPower));
            motor2.setPower((turnPower-verticalPower)/Math.max(1,verticalPower+turnPower));

            if(gamepad1.dpad_down){
                e=true;
            }
            if (e){
                armMotor.setPower(-1);
            } else {
                armMotor.setPower(gamepad1.left_stick_y);
            }

            if (gamepad1.triangle){
                ignition.setPosition(1);
            }
            else if (gamepad1.x){
                ignition.setPosition(0.5);
            }
            telemetry.update();
        }

    }
}