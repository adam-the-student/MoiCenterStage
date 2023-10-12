package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "PTDB teleop")
public class PTDTeleOp extends LinearOpMode {
    private DcMotor motor1,motor2,motor3,motor4;
    @Override
    public void runOpMode(){
        motor1 = hardwareMap.get(DcMotor.class , "frontLeft");
        motor2 = hardwareMap.get(DcMotor.class , "frontRight");
        motor3 = hardwareMap.get(DcMotor.class , "backLeft");
        motor4 = hardwareMap.get(DcMotor.class , "backRight");

        waitForStart();
        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double motor1Power = (y + x + rx) / denominator;
            double motor2Power = (y - x + rx) / denominator;
            double motor3Power = (y - x - rx) / denominator;
            double motor4Power = (y + x - rx) / denominator;

            if(gamepad1.left_trigger >= 0.1){
                motor1Power = motor1Power/4;
                motor2Power = motor2Power/4;
                motor3Power = motor3Power/4;
                motor4Power = motor4Power/4;
            }
            else if(gamepad1.right_trigger >= 0.1){
                motor1Power = motor1Power*2;
                motor2Power = motor2Power*2;
                motor3Power = motor3Power*2;
                motor4Power = motor4Power*2;
            }

            motor1.setPower(motor1Power/2);  // motor1 is top left
            motor2.setPower(-motor2Power/2);  // motor2 is top right
            motor3.setPower(motor3Power/2);  // motor3 is bottom left
            motor4.setPower(-motor4Power/2);  // motor4 is bottom right
        }
    }
}