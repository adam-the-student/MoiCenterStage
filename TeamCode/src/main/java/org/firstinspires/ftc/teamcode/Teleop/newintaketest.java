package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "salahIntaketest teleop")
public class newintaketest extends LinearOpMode {
    private CRServo leftspin, rightspin;
    private DcMotor test;

    @Override
    public void runOpMode() {
        leftspin = hardwareMap.get(CRServo.class, "leftSpin");
        rightspin = hardwareMap.get(CRServo.class, "rightSpin");
        test = hardwareMap.get(DcMotor.class, "test");

        waitForStart();
        test.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        test.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        test.setTargetPosition(1000);
        test.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        while (opModeIsActive()){
        leftspin.setPower(gamepad2.right_trigger > 0 ? -1 : (gamepad2.left_trigger > 0 ? 1 : 0));
        rightspin.setPower(gamepad2.right_trigger > 0 ? 1 : (gamepad2.left_trigger > 0 ? -1 : 0));
        telemetry.addData("Motor pos: ", test.getCurrentPosition());
        telemetry.update();
        }
    }
}