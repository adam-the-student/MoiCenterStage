package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "test")
public class SingleMotorTestManual extends LinearOpMode {
    private DcMotor testMotor;

    // todo: write your code here
    @Override
    public void runOpMode(){
        testMotor = hardwareMap.get(DcMotor.class, "testMotor");
        waitForStart();
        while(opModeIsActive()){
            testMotor.setPower(gamepad1.left_stick_y);
        }
    }
}