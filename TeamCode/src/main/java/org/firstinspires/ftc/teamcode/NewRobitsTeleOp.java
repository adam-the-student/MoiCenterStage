package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImpl;

@TeleOp(name = "ROBITS TELE")
public class NewRobitsTeleOp extends LinearOpMode {
    DcMotor motor1,motor2, armMotor;
    @Override
    public void runOpMode(){
        motor1 = hardwareMap.get(DcMotor.class,"leftMotor");
        motor2 = hardwareMap.get(DcMotor.class, "rightMotor");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");

        waitForStart();
        boolean gm2 = false;
        while (opModeIsActive()){
            gm2 = gamepad2.right_bumper != gm2;

            if (!gm2) {
                motor1.setPower((gamepad1.left_stick_y + gamepad1.right_stick_x) / (gamepad2.right_trigger * 2 + 1) / 2 * (gamepad2.left_trigger * 2 + 1));
                motor2.setPower((-gamepad1.left_stick_y + gamepad1.right_stick_x) / (gamepad2.right_trigger * 2 + 1) / 2 * (gamepad2.left_trigger * 2 + 1));
            } else {
                motor1.setPower(gamepad2.left_stick_y + gamepad2.right_stick_x);
                motor2.setPower(-gamepad2.left_stick_y + gamepad2.right_stick_x);
            }

            telemetry.addData("Gamepad2? => ",gm2);
            telemetry.update();
        }

    }
}