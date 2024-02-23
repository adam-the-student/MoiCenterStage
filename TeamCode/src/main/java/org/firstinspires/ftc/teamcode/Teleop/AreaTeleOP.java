package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseRobotMethodsAndStuff;

@TeleOp(name = "AREA TELE")
public class AreaTeleOP extends LinearOpMode {
    RevBlinkinLedDriver lights;
    @Override
    public void runOpMode() throws InterruptedException {

        BaseRobotMethodsAndStuff robot = new BaseRobotMethodsAndStuff(this);

        waitForStart();

        while (opModeIsActive()){
            robot.moveBase();
        }

    }
}
