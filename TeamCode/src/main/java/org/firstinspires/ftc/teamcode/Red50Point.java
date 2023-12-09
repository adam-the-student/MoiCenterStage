package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.OpenCV.CVMaster;
import org.firstinspires.ftc.teamcode.OpenCV.Workspasce.SpikeZoneDetectionRed;

@Autonomous
public class Red50Point extends LinearOpMode {
    DcMotor motor1,motor2, armMotor;
    Servo roller, wrist;
    @Override
    public void runOpMode() {

        CVMaster Cam1 = new CVMaster(this, new SpikeZoneDetectionRed());
        BaseRobotMethodsAndStuff robotMethods = new BaseRobotMethodsAndStuff(this);
        Cam1.runPipeline();
        int zone = 2;

        while (!isStarted()) {
            zone = Cam1.getZone();
            telemetry.addData("zone", zone);
            telemetry.update();
            sleep(500);
        }
        waitForStart();
        Cam1.stopCamera();
        //537.6 ticks per rev

        robotMethods.forward((int)(537.6*25),.5);
        if (zone == 3){
            robotMethods.turn(-45,.5);
            robotMethods.forward((int)(537.6*2),.5);
            robotMethods.turn(-20,.5);
            sleep(500);
            robotMethods.forward((int)(537.6*-6));
            robotMethods.turn(-42,1);
            robotMethods.forward((int)(537.6*41.75),.5);
            robotMethods.turn(10,1);
            robotMethods.forward((int)(537.6*2),.6);
            robotMethods.putYellow();
            sleep(1000);
            robotMethods.forward((int)(537.6*-2));
            // auton code
        }
        else if (zone == 2) {
            robotMethods.forward((int)(537.6*3));
            sleep(500);
            robotMethods.forward((int)(537.6*-3.5),.5);
            robotMethods.turn(-85);
            robotMethods.forward((int)(537.6*36),.5);
            robotMethods.turn(-7);
            robotMethods.forward((int)(537.6),.7);
            robotMethods.putYellow();
            sleep(1000);
            robotMethods.forward((int)(-537.6*2));
            // auton /\
        }
        else if (zone==1){
            robotMethods.turn(50,.2);
            sleep(500);
            robotMethods.forward((int)(537.6*-5.5));
            robotMethods.turn(-115,.5);
            robotMethods.forward((int)(537.6*29),.9);
            robotMethods.turn(-25,.5);
            sleep(100);
            robotMethods.forward((int)(537.6*10.8),.5);
            robotMethods.putYellow();
            sleep(1000);
            robotMethods.forward((int)(537.6*-2));
            // auton /\
        }
    }
}
