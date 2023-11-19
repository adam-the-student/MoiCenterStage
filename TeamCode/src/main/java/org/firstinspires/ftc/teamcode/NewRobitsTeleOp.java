package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImpl;

@TeleOp(name = "Meet TeleOP")
public class NewRobitsTeleOp extends LinearOpMode {
    DcMotor motor1,motor2, armMotor;
    Servo roller, wrist, Ign;
    @Override
    public void runOpMode(){
        motor1 = hardwareMap.get(DcMotor.class,"leftMotor");
        motor2 = hardwareMap.get(DcMotor.class, "rightMotor");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        roller = hardwareMap.get(Servo.class, "roller");
        wrist = hardwareMap.get(Servo.class,"wrist");
        Ign = hardwareMap.get(Servo.class, "ign");
        double servoPos = 0;
        wrist.setPosition(servoPos);

        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        boolean isDown=false;
        while (opModeIsActive()){

            double verticalPower = gamepad1.right_trigger>0?-gamepad1.right_trigger:gamepad1.left_trigger;
            if (gamepad2.right_bumper){
                verticalPower/=2;
            }
            double turnPower = -gamepad1.left_stick_x;
            motor1.setPower((verticalPower+turnPower)/Math.max(1,verticalPower+turnPower));
            motor2.setPower((turnPower-verticalPower)/Math.max(1,verticalPower+turnPower));

            //gamepad 2 controls
            roller.setPosition(gamepad2.right_trigger!=0?1:0);

            if (gamepad2.right_bumper){
                servoPos = isDown?0:.1;
                isDown = !isDown;
            }
            wrist.setPosition(servoPos);
            if (gamepad2.x){
                Ign.setPosition(1);
            }
            else if (gamepad2.triangle){
                Ign.setPosition(0.5);
            }

            armMotor.setPower(-gamepad2.left_stick_y);
            telemetry.addData("Servo Position: ", servoPos);

            telemetry.update();
        }
    }
}
