package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Park Left red")
public class ParkRedLeft extends LinearOpMode {
    DcMotor motor1,motor2, armMotor;
    Servo roller, wrist;
    @Override
    public void runOpMode() throws InterruptedException {

        BaseRobotMethodsAndStuff robotMethods = new BaseRobotMethodsAndStuff(this);

        waitForStart();

        robotMethods.forward((int)(537.6*51)*60);
        robotMethods.turn(90);
        robotMethods.forward((int)(537.6*51)*90);

    }

}
