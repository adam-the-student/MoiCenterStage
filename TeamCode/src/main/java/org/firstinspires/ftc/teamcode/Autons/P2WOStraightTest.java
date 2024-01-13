package org.firstinspires.ftc.teamcode.Autons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class P2WOStraightTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Parallel2WheelOdo base = new Parallel2WheelOdo(this);
        waitForStart();

    }
}
