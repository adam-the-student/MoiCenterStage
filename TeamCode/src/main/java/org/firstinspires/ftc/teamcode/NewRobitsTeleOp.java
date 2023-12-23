package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImpl;

@TeleOp(name = "Meet TeleOP")
public class NewRobitsTeleOp extends LinearOpMode {
    DcMotor motor1,motor2, armMotor;
    Servo ignition, yellowPixel, wrist;
    CRServo grab, twist;
    @Override
    public void runOpMode(){
        motor1 = hardwareMap.get(DcMotor.class,"leftMotor");
        motor2 = hardwareMap.get(DcMotor.class, "rightMotor");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        ignition = hardwareMap.get(Servo.class,"ign");
        yellowPixel = hardwareMap.get(Servo.class,"yellowPixel");
        wrist = hardwareMap.get(Servo.class, "wrist");
        twist = hardwareMap.get(CRServo.class, "twist");
        grab = hardwareMap.get(CRServo.class, "grab");

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


            if (gamepad1.triangle){
                ignition.setPosition(1);
            }
            else if (gamepad1.x){
                ignition.setPosition(0.5);
            }

            // gamepad2
            if(gamepad2.dpad_down){
                e=true;
            }

            if (e){
                armMotor.setPower(-1);
            } else {
                armMotor.setPower(gamepad1.left_stick_y);
            }
wrist.scaleRange(-0.5,0.5);
            if (gamepad2.left_bumper)
            {
                wrist.setPosition(1);
            } else if (gamepad2.right_bumper) {
                wrist.setPosition(-1);
            }
            if (gamepad2.square){
                twist.setPower(1);
            } else if (gamepad2.circle) {
                twist.setPower(-1);
            }
            else {twist.setPower(0);}
            if (gamepad2.right_trigger > 0)
                grab.setPower(gamepad2.right_trigger);
            else if (gamepad2.left_trigger > 0) {
                grab.setPower(-gamepad2.left_trigger);
            }
            else{
                grab.setPower(0);
            }
            telemetry.update();
        }

    }
}
// (┬┬﹏┬┬) so sad