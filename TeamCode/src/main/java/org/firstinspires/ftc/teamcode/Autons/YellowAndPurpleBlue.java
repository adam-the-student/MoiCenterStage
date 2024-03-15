package org.firstinspires.ftc.teamcode.Autons;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Helpers.BaseRobotMethodsAndStuff;
import org.firstinspires.ftc.teamcode.Opencv.CvMaster;
import org.firstinspires.ftc.teamcode.Opencv.Pipelines.workspace.SpikeZoneDetectionBlue;

@Autonomous(name = "2' + 0 Blue BackDrop")
public class YellowAndPurpleBlue extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        boolean yes = true;
        BaseRobotMethodsAndStuff robot = new BaseRobotMethodsAndStuff(this);
        robot.closeClaw(BaseRobotMethodsAndStuff.ClawSide.RIGHT);
        CvMaster<SpikeZoneDetectionBlue> cam1 = new CvMaster<>(this, new SpikeZoneDetectionBlue());
        cam1.runPipeline();
        byte spikeZone=2;
        APTAlign aptAlign = new APTAlign(this,"Webcam 1");
        while(!opModeIsActive()){
            spikeZone = (byte)cam1.getZone();
            telemetry.addData("Spike Camera Zone: ", spikeZone);
            telemetry.update();
            sleep(500);
        }
        cam1.stopCamera();

        waitForStart();

        if (spikeZone==1){
            robot.calculateTargetPos(-20,30);
        } else if (spikeZone==2){
            robot.calculateTargetPos(-24,30);
        } else {
            robot.calculateTargetPos(-28,30);
        }
        robot.turn(-90);
        robot.resetEncoders();
        aptAlign.runToTag(spikeZone==1?1:spikeZone==2?2:3,10);
        robot.flipArm();
        robot.calculateTargetPos(10,spikeZone==1?0:2);
        robot.openClaw(BaseRobotMethodsAndStuff.ClawSide.RIGHT);
        robot.armDown();

        if (spikeZone==3){
            robot.calculateTargetPos(-40,-9);
            robot.wristPos(BaseRobotMethodsAndStuff.WristPos.AUTONDOWN);
            robot.openClaw(BaseRobotMethodsAndStuff.ClawSide.LEFT);
            sleep(100);
            robot.wristPos(BaseRobotMethodsAndStuff.WristPos.BACKDROP);
            robot.calculateTargetPos(40,-24);
        } else if (spikeZone==2){
            robot.calculateTargetPos(-26,11);
            robot.wristPos(BaseRobotMethodsAndStuff.WristPos.AUTONDOWN);
            robot.openClaw(BaseRobotMethodsAndStuff.ClawSide.LEFT);
            sleep(100);
            robot.wristPos(BaseRobotMethodsAndStuff.WristPos.BACKDROP);
            robot.calculateTargetPos(28,-40);

        } else {
            robot.calculateTargetPos(-20,7);
            robot.wristPos(BaseRobotMethodsAndStuff.WristPos.AUTONDOWN);
            robot.openClaw(BaseRobotMethodsAndStuff.ClawSide.LEFT);
            sleep(100);
            robot.wristPos(BaseRobotMethodsAndStuff.WristPos.BACKDROP);
            robot.calculateTargetPos(28,-32);
        }

    }
}
