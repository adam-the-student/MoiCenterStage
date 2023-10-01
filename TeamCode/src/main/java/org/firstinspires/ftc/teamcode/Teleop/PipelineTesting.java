package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Opencv.CvMaster;
import org.firstinspires.ftc.teamcode.Opencv.SpikeDetectionThreeZone;

@TeleOp(name = "Pipeline testing one camera")
public class PipelineTesting extends LinearOpMode {
    @Override
    public void runOpMode(){
        CvMaster cam1 = new CvMaster(hardwareMap, new SpikeDetectionThreeZone());
        cam1.runPipeline();
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Camera 1 Zone: ",cam1.getZone());
            telemetry.update();
            sleep(500);
        }
    }
}