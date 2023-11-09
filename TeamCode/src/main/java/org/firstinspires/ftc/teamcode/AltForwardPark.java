package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Alt Straight Park")
public class AltForwardPark extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        BaseRobotMethodsAndStuff robotMethods = new BaseRobotMethodsAndStuff(this);

        waitForStart();

        robotMethods.forward((int)(537.6*90));

    }

}