package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "salahIntaketest teleop")
public class newintaketest extends LinearOpMode {
    private CRServo leftspin, rightspin;
    private Servo outTake;

    @Override
    public void runOpMode() {
        leftspin = hardwareMap.get(CRServo.class, "leftSpin");
        rightspin = hardwareMap.get(CRServo.class, "rightSpin");
        outTake = hardwareMap.get(Servo.class, "outTake");

        waitForStart();
while (opModeIsActive()){
        if (gamepad1.left_trigger != 0) {
            leftspin.setPower(-0.3);
            rightspin.setPower(0.3);
        } else if (gamepad1.right_trigger != 0) {
            leftspin.setPower(0.3);
            rightspin.setPower(-0.3);
        }
        else {
            leftspin.setPower(0);
            rightspin.setPower(0);
        }
        if (gamepad1.dpad_up){
            outTake.setPosition(1);
        } else if (gamepad1.dpad_down) {
            outTake.setPosition(.5);

        }
        else {
            outTake.setPosition(0);
        }
}
}
}