package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.OpenCV.CVMaster;
import org.firstinspires.ftc.teamcode.OpenCV.Workspasce.SpikeZoneDetectionBlue;
import org.firstinspires.ftc.teamcode.OpenCV.Workspasce.SpikeZoneDetectionRed;

@Autonomous()
public class PurplePixelBlue extends LinearOpMode {
    @Override
    public void runOpMode() {

        CVMaster Cam1 = new CVMaster(this, new SpikeZoneDetectionBlue());
        BaseRobotMethodsAndStuff robotMethods = new BaseRobotMethodsAndStuff(this);
        Cam1.runPipeline();
        int zone = 2;

        while (!isStarted()) {
            zone = Cam1.getZone();
            telemetry.addData("zone", zone);
            telemetry.update();
            sleep(1000);
        }
        waitForStart();
        Cam1.stopCamera();
        //537.6 ticks per rev

        robotMethods.forward((int)(537.6*25),.5);
        if (zone == 1){
            robotMethods.turn(50,1);
            sleep(500);
            robotMethods.forward((int)(537.6*-4));
            // auton code
        }
        else if (zone == 2) {
            robotMethods.forward((int)(537.6*3));
            sleep(500);
            robotMethods.forward((int)(537.6*-5));
            // auton /\
        }
        else if (zone==3){
            robotMethods.turn(-40,.2);
            robotMethods.forward((int)(537.6),.5);
            robotMethods.turn(-30,.2);
            sleep(500);
            robotMethods.forward((int)(537.6*-5));
            // auton /\
        }

    }
}
