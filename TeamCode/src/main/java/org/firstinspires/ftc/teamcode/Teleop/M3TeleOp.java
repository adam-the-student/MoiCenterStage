package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "M3 teleop")
public class M3TeleOp extends LinearOpMode {
    private DcMotor motor1,motor2,motor3,motor4, leftRigging, rightRigging, slideRig, wormDrive;
    private Servo leftHook, rightHook, drone;
    private CRServo leftClaw, rightClaw, wrist;
    @Override
    public void runOpMode(){
        motor1 = hardwareMap.get(DcMotor.class , "frontLeft");
        motor2 = hardwareMap.get(DcMotor.class , "frontRight");
        motor3 = hardwareMap.get(DcMotor.class , "backLeft");
        motor4 = hardwareMap.get(DcMotor.class , "backRight");
        slideRig = hardwareMap.get(DcMotor.class , "slideRig");
        wormDrive = hardwareMap.get(DcMotor.class , "wormDrive");
        leftRigging = hardwareMap.get(DcMotor.class, "leftRig");
        rightRigging = hardwareMap.get(DcMotor.class, "rightRig");
        leftHook = hardwareMap.get(Servo.class, "leftHook");
        rightHook = hardwareMap.get(Servo.class, "rightHook");
        leftClaw = hardwareMap.get(CRServo.class, "leftClaw");
        rightClaw = hardwareMap.get(CRServo.class, "rightClaw");
        wrist = hardwareMap.get(CRServo.class, "wrist");
        drone = hardwareMap.get(Servo.class, "drone");

        waitForStart();

        byte inverseControls = 1;

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y;
            double x = -gamepad1.left_stick_x*1.15 ; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double motor1Power = (inverseControls * (y + x)- rx*1.2) / denominator;  //motor1 is top left corner
            double motor2Power = (inverseControls * (y - x) + rx*1.2) / denominator;  //motor2 is top right corner
            double motor3Power = (inverseControls * (y - x) - rx*1.2) / denominator;  //motor3 is bottom left corner
            double motor4Power = (inverseControls * (y + x) +  rx*1.2) / denominator;  //motor4 is bottom right corner

            if(gamepad1.left_trigger > 0){
                motor1Power = motor1Power/2;
                motor2Power = motor2Power/2;
                motor3Power = motor3Power/2;
                motor4Power = motor4Power/2;
            }
            else if(gamepad1.right_trigger > 0){
                motor1Power = motor1Power*2;
                motor2Power = motor2Power*2;
                motor3Power = motor3Power*2;
                motor4Power = motor4Power*2;
            }

            if (gamepad1.circle) {
                inverseControls = (byte) (inverseControls==1?-1:1);
            }

            motor1.setPower(motor1Power/2);  // motor1 is top left
            motor2.setPower(-motor2Power/2);  // motor2 is top right
            motor3.setPower(motor3Power/2);  // motor3 is bottom left
            motor4.setPower(-motor4Power/2);  // motor4 is bottom right

            telemetry.addData("CONTROLS : ",inverseControls==1?"REGULAR":"INVERTED");
            telemetry.update();

            if (gamepad1.left_bumper){
                leftRigging.setPower(1);
                rightRigging.setPower(1);
                sleep(3000);
                leftRigging.setPower(0);
                rightRigging.setPower(0);
                leftHook.setPosition(1);
                rightHook.setPosition(0);
            }
            if (gamepad1.right_bumper){
                leftRigging.setPower(-1);
                rightRigging.setPower(-1);
                sleep(3000);
                leftRigging.setPower(0);
                rightRigging.setPower(0);
            }

            //gamepad2
            wormDrive.setPower(-gamepad2.left_stick_y);
            slideRig.setPower(-gamepad2.right_stick_y/4);

            if (gamepad2.b){
                leftClaw.setPower(0.2);
            } else if (gamepad2.left_trigger>0.5){
                leftClaw.setPower(-0.2);
            } else if (gamepad2.left_trigger>0) {
                leftClaw.setPower(0);
            }
            if (gamepad2.b){
                rightClaw.setPower(-0.2);
            } else if (gamepad2.right_trigger>0.5){
                rightClaw.setPower(0.2);}
            else if (gamepad2.right_trigger>0) {
                rightClaw.setPower(0);
            }
            if(gamepad2.dpad_down){
                wrist.setPower(-.2);
            } else if(gamepad2.dpad_up){
                wrist.setPower(.2);
            } else {
                wrist.setPower(0);
            }

            if (gamepad2.right_bumper&& gamepad2.left_bumper){
                drone.setPosition(1);
            }
        }
    }
}