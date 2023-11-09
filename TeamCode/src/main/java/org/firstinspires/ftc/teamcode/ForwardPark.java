package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "(Red) Backstage Park")
public class ForwardPark extends LinearOpMode {
    DcMotor motor1,motor2, armMotor;
    Servo roller, wrist;
    @Override
    public void runOpMode() {
        motor1 = hardwareMap.get(DcMotor.class, "leftMotor");
        motor2 = hardwareMap.get(DcMotor.class, "rightMotor");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        roller = hardwareMap.get(Servo.class, "roller");
        wrist = hardwareMap.get(Servo.class, "wrist");



        wrist.setPosition(9/(double)56);
        sleep(1000);
        roller.setPosition(1);
        sleep(1000);
        wrist.setPosition(0);
        waitForStart();
        //537.6 ticks per rev
        forward((int)(537.6*5));
        sleep(2000);
        roller.setPosition(0);
    }
    public void forward(int distance){
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motor1.setTargetPosition(-distance);
        motor2.setTargetPosition(distance);

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (!(motor1.getTargetPosition()-10 <= motor1.getCurrentPosition()&&motor1.getTargetPosition()+10 >= motor1.getCurrentPosition())||!(motor2.getTargetPosition()-10 <= motor2.getCurrentPosition()&&motor2.getTargetPosition()+10 >= motor2.getCurrentPosition())){
            motor1.setPower(1-((double)motor1.getCurrentPosition()/motor1.getTargetPosition()));
            motor2.setPower(1-((double)motor2.getCurrentPosition()/motor2.getTargetPosition()));
            telemetry.addData("motor1 Position: ", motor1.getCurrentPosition());
            telemetry.addData("motor2 position: ", motor2.getCurrentPosition());
            telemetry.update();
        }
        motor1.setPower(0);
        motor2.setPower(0);
    }
}
