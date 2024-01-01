package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class MotorVeloTesting extends LinearOpMode {
    DcMotor motor1,motor2,motor3,motor4;
    @Override
    public void runOpMode() throws InterruptedException {
        motor1 = hardwareMap.get(DcMotor.class, "frontLeft");
        motor2 = hardwareMap.get(DcMotor.class, "frontRight");
        motor3 = hardwareMap.get(DcMotor.class, "backLeft");
        motor4 = hardwareMap.get(DcMotor.class, "backRight");
        waitForStart();
        for (double i = 0; i < 100; i++) {
            motor1.setPower(i/20);
            motor2.setPower(i/20);
            motor3.setPower(i/20);
            motor4.setPower(i/20);
            sleep(100);
        }
        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);
    }
}
