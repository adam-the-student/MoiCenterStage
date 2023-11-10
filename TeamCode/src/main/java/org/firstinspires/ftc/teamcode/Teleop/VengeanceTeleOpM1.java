package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "M1teleop")
public class VengeanceTeleOpM1 extends LinearOpMode {
    private DcMotor motor1,motor2,motor3,motor4,slideMotor,intakeMotor;
    private Servo Bs, ignition;
    private CRServo locker;
    @Override
    public void runOpMode(){
        motor1 = hardwareMap.get(DcMotor.class , "frontLeft");
        motor2 = hardwareMap.get(DcMotor.class , "frontRight");
        motor3 = hardwareMap.get(DcMotor.class , "backLeft");
        motor4 = hardwareMap.get(DcMotor.class , "backRight");
        slideMotor = hardwareMap.get(DcMotor.class, "slideMotor");
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        Bs = hardwareMap.get(Servo.class,"bigServo");
        locker = hardwareMap.get(CRServo.class,"locker");
        ignition = hardwareMap.get(Servo.class,"ign");

        waitForStart();
        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double motor1Power = (y + x + rx) / denominator;
            double motor2Power = (y - x - rx) / denominator;
            double motor3Power = (y - x + rx) / denominator;
            double motor4Power = (y + x - rx) / denominator;

            if(gamepad1.left_trigger >= 0.1){
                motor1Power = motor1Power/2;
                motor2Power = motor2Power/2;
                motor3Power = motor3Power/2;
                motor4Power = motor4Power/2;
            }
            else if(gamepad1.right_trigger >= 0.1){
                motor1Power = motor1Power*2;
                motor2Power = motor2Power*2;
                motor3Power = motor3Power*2;
                motor4Power = motor4Power*2;
            }

            if (gamepad2.right_trigger>0){
                motor1Power = motor1Power/4;
                motor2Power = motor2Power/4;
                motor3Power = motor3Power/4;
                motor4Power = motor4Power/4;
            }

            motor1.setPower(-motor1Power/2);  // motor1 is top left
            motor2.setPower(motor2Power/2);  // motor2 is top right
            motor3.setPower(-motor3Power/2);  // motor3 is bottom left
            motor4.setPower(motor4Power/2);  // motor4 is bottom right


            // gamepad 2 starts

            slideMotor.setPower(gamepad2.right_stick_y/2);

            if (gamepad2.right_trigger!=0){
                intakeMotor.setPower(1);
            } else if (gamepad2.left_trigger!=0) {
                intakeMotor.setPower(-1);
            } else {
                intakeMotor.setPower(0);
            }

            if (gamepad2.x){
               locker.setPower(1);
            }
            else if(gamepad2.b){
                locker.setPower(-1);
            }else {
                locker.setPower(0);
            }

            if (gamepad2.right_bumper){
                Bs.setPosition(-1);
            } else if (gamepad2.dpad_down) {
                Bs.setPosition(-.4);
            } else if (gamepad2.left_bumper) {
                Bs.setPosition(.375);
            }

            if (gamepad1.triangle){
                ignition.setPosition(1);
            }
            else if (gamepad1.x){
                ignition.setPosition(0.5);
            }

        }
    }
}
