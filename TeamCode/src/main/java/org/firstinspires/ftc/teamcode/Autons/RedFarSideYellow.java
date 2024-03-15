package org.firstinspires.ftc.teamcode.Autons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Helpers.BaseRobotMethodsAndStuff;
import org.firstinspires.ftc.teamcode.Opencv.CvMaster;
import org.firstinspires.ftc.teamcode.Opencv.Pipelines.workspace.SpikeZoneDetectionRed;

@Autonomous(name = "2' + 0 Red Audience")
public class RedFarSideYellow extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        BaseRobotMethodsAndStuff robot = new BaseRobotMethodsAndStuff(this);
        robot.wristPos(BaseRobotMethodsAndStuff.WristPos.BACKDROP);
        robot.closeClaw(BaseRobotMethodsAndStuff.ClawSide.LEFT);
        CvMaster<SpikeZoneDetectionRed> cam1 = new CvMaster<>(this, new SpikeZoneDetectionRed());
        APTAlign aptAlign = new APTAlign(this, "Webcam 1");
        cam1.runPipeline();
        byte spikeZone=1;

        while(!opModeIsActive()){
            spikeZone = (byte)cam1.getZone();
            telemetry.addData("Spike Camera Zone: ", spikeZone);
            telemetry.update();
            sleep(500);
        }
        cam1.stopCamera();
        waitForStart();


        if (spikeZone==1) {
            robot.wristPos(BaseRobotMethodsAndStuff.WristPos.AUTONDOWN);
            robot.calculateTargetPos(-17,13);
            robot.openClaw(BaseRobotMethodsAndStuff.ClawSide.RIGHT);
            robot.calculateTargetPos(-34,-3);
            robot.turn(90);
            sleep(3000);
            robot.calculateTargetPos(75,22);
            robot.resetEncoders();
            aptAlign.runToTag(4,8);
            robot.flipArm();
            robot.calculateTargetPos(7.6,-1.3);
            robot.openClaw(BaseRobotMethodsAndStuff.ClawSide.LEFT);
        } else if (spikeZone == 2){
            robot.wristPos(BaseRobotMethodsAndStuff.WristPos.AUTONDOWN);
            robot.calculateTargetPos(-25.5,6.6);
            robot.openClaw(BaseRobotMethodsAndStuff.ClawSide.RIGHT);
            robot.calculateTargetPos(1,0);
            robot.turn(90);
            sleep(3000);
            robot.calculateTargetPos(65,0);
            robot.resetEncoders();
            aptAlign.runToTag(5,8);
            robot.flipArm();
            robot.calculateTargetPos(7,-1);
            robot.openClaw(BaseRobotMethodsAndStuff.ClawSide.LEFT);
        } else {
            robot.wristPos(BaseRobotMethodsAndStuff.WristPos.AUTONDOWN);
            robot.calculateTargetPos(-27,-4);
            robot.turn(-90);
            robot.openClaw(BaseRobotMethodsAndStuff.ClawSide.RIGHT);
            sleep(100);
            robot.wristPos(BaseRobotMethodsAndStuff.WristPos.BACKDROP);
            robot.calculateTargetPos(-8,30);
            sleep(2000);
            robot.calculateTargetPos(-70,-38);
            robot.turn(180);
            robot.resetEncoders();
            aptAlign.runToTag(6,8);
            robot.flipArm();
            robot.calculateTargetPos(7.7,4);
            robot.openClaw(BaseRobotMethodsAndStuff.ClawSide.LEFT);
        }
    }
}
