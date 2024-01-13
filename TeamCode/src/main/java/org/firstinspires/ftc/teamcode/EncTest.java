package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "PMC")
public class EncTest extends LinearOpMode {
    DcMotor testMotor;
    @Override
    public void runOpMode() throws InterruptedException {
        testMotor = hardwareMap.get(DcMotor.class, "testMotor");
        waitForStart();

        testMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        testMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        testMotor.setTargetPosition(10000);
        testMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (opModeIsActive()) {
            telemetry.addData("Pos1: ", testMotor.getCurrentPosition());
            telemetry.update();
        }
    }
    public void motorController(DcMotor motor, int targetDestination, int startingPos){

        double error = targetDestination-startingPos;

        motor.setTargetPosition(targetDestination);

        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (Math.abs(error) > 10) { // <= change this value for your error range /  reliability.
            motor.setPower((motor.getPower() + (error / targetDestination)) / 2);
            error = motor.getTargetPosition()-motor.getCurrentPosition();
            telemetry.addData("Position: ",motor.getCurrentPosition());
            telemetry.addData("Error: ", error);
            telemetry.update();
        }
        motor.setPower(0);
    }
}
