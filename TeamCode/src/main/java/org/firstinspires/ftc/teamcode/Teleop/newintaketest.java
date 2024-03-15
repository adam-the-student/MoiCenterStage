package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Helpers.BaseRobotMethodsAndStuff;


@TeleOp(name = "salahIntaketest teleop")
public class newintaketest extends LinearOpMode {
    RevTouchSensor touchSensor;
    @Override
    public void runOpMode() {
        touchSensor = hardwareMap.get(RevTouchSensor.class, "magnet");
        BaseRobotMethodsAndStuff robotMethodsAndStuff = new BaseRobotMethodsAndStuff(this);

        waitForStart();


        while (opModeIsActive()){
        telemetry.addData("yes: ", touchSensor.isPressed());
        telemetry.update();
        }
    }
}