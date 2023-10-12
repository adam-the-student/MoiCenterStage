package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Motor Velo testing")
public class MotorVelocityTesting extends LinearOpMode {
    private DcMotorEx testMotor,testMotor2;
    @Override
    public void runOpMode(){
        testMotor = hardwareMap.get(DcMotorEx.class, "frontLeft");
        testMotor2 = hardwareMap.get(DcMotorEx.class, "frontRight");
        ElapsedTime timer = new ElapsedTime();
        waitForStart();
        timer.reset();
        int motor1Position, motor2Position;
        double time;
        timer.reset();
            while (opModeIsActive()){
                testMotor.setPower(1);
                testMotor2.setPower(1);
                motor1Position =  testMotor.getCurrentPosition();
                motor2Position = testMotor2.getCurrentPosition();
                time = timer.time();
                telemetry.addData("motor1 velocity",motor1Position/time);
                telemetry.addData("motor2 velocity",motor2Position/time);
                timer.reset();
            }
    }
}
