package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Helpers.BaseRobotMethodsAndStuff;

@TeleOp(name = "AREA TELE")
public class AreaTeleOP extends LinearOpMode {
    RevBlinkinLedDriver lights;
    @Override
    public void runOpMode() throws InterruptedException {

        BaseRobotMethodsAndStuff robot = new BaseRobotMethodsAndStuff(this);
        robot.center();
        robot.wristPos(BaseRobotMethodsAndStuff.WristPos.BACKDROP);
        waitForStart();
        robot.hooksDown(true);

        while (opModeIsActive()){
            robot.moveBase();
        }

    }
}
