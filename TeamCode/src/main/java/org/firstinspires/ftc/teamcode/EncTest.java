package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Helpers.BaseRobotMethodsAndStuff;



@TeleOp(name = "PMC")
public class EncTest extends LinearOpMode {
    DcMotor motor;
    @Override
    public void runOpMode() throws InterruptedException {
        BaseRobotMethodsAndStuff base = new BaseRobotMethodsAndStuff(this);
        base.setLambdaParameters(base.simplePController,base.wormDrive, (int)(145.1*28/3+1.5));
        Thread armCode = new Thread(base);
        waitForStart();

        armCode.start();
        base.calculateTargetPos(-30,-26);

//        MoveToPoint baseController = new MoveToPoint(this);
//        waitForStart();
//        baseController.moveWithOutIMU(0,3);
    }
}
