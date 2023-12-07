package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "(RED) Park Left")
public class ParkRedLeft extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        BaseRobotMethodsAndStuff robotMethods = new BaseRobotMethodsAndStuff(this);

        waitForStart();

        robotMethods.forward((int)(537.6*60));
        robotMethods.turn(-90);
        robotMethods.forward((int)(537.6*90));
    }
}