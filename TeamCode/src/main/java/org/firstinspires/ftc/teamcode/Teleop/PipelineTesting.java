package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Opencv.CvMaster;
import org.firstinspires.ftc.teamcode.Opencv.Pipelines.workspace.SpikeZoneDetectionRed;

@TeleOp(name = "Pipeline testing one camera")
public class PipelineTesting extends LinearOpMode {
    LinearOpMode linearOpMode = this;
    @Override
    public void runOpMode(){
        CvMaster<SpikeZoneDetectionRed> cam1 = new CvMaster<>(linearOpMode, new SpikeZoneDetectionRed());
        cam1.runPipeline();
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Camera 1 Zone: ",cam1.getZone());
            telemetry.update();
        }
        cam1.stopCamera();
    }
}