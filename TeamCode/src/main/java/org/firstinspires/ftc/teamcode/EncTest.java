package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "PMC")
public class EncTest extends LinearOpMode {


    @Override
    public void runOpMode() throws InterruptedException {
        BaseRobotMethodsAndStuff robotMethodsAndStuff = new BaseRobotMethodsAndStuff(this);
        waitForStart();
        robotMethodsAndStuff.calculateTargetPos(24,24);
    }
}
