package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "PTDB teleop")
public class PTDTeleOp extends LinearOpMode {
    private DcMotor motor1,motor2,motor3,motor4;
    @Override
    public void runOpMode(){
        motor1 = hardwareMap.get(DcMotor.class , "frontLeft");
        motor2 = hardwareMap.get(DcMotor.class , "frontRight");
        motor3 = hardwareMap.get(DcMotor.class , "backLeft");
        motor4 = hardwareMap.get(DcMotor.class , "backRight");
        waitForStart();

        byte inverseControls = 1;

        while (opModeIsActive()) {
            double y = gamepad1.left_stick_y;
            double x = -gamepad1.left_stick_x*1.15 ; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double motor1Power = (inverseControls * (y + x )- rx*1.2) / denominator;  //motor1 is top left corner
            double motor2Power = (inverseControls * (y - x) + rx*1.2) / denominator;  //motor2 is top right corner
            double motor3Power = (inverseControls * (y - x) - rx*1.2) / denominator;  //motor3 is bottom left corner
            double motor4Power = (inverseControls * (y + x) +  rx*1.2) / denominator;  //motor4 is bottom right corner

            if(gamepad1.left_trigger > 0){
                motor1Power = motor1Power/4;
                motor2Power = motor2Power/4;
                motor3Power = motor3Power/4;
                motor4Power = motor4Power/4;
            }

            if (gamepad1.circle) {
                inverseControls = (byte) (inverseControls==1?-1:1);
            }

            motor1.setPower(motor1Power);  // motor1 is top left
            motor2.setPower(-motor2Power);  // motor2 is top right
            motor3.setPower(motor3Power);  // motor3 is bottom left
            motor4.setPower(-motor4Power);  // motor4 is bottom right

            telemetry.addData("CONTROLS : ",inverseControls==1?"REGULAR":"INVERTED");
            telemetry.update();


        }
    }
}