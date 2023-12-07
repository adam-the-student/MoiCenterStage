package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class BaseRobotMethodsAndStuff {
    HardwareMap hardwareMap;
    LinearOpMode l_op;
    DcMotor motor1,motor2, armMotor;
    Servo yellowPixel, roller;
    public final double WHEEL_CIRCUMFERENCE = Math.PI * 3, DEGREE_TO_TICK = ((6.5/3)/180)*560;

    public BaseRobotMethodsAndStuff(LinearOpMode l_op) {
        this.l_op = l_op;
        hardwareMap = l_op.hardwareMap;
        motor1 = hardwareMap.get(DcMotor.class, "leftMotor");
        motor2 = hardwareMap.get(DcMotor.class, "rightMotor");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        roller = hardwareMap.get(Servo.class, "ign");
        yellowPixel = hardwareMap.get(Servo.class, "yellowPixel");
    }

    public void putYellow(){
        yellowPixel.scaleRange(0.45,.55);
        yellowPixel.setPosition(.5);
        yellowPixel.setPosition(0);
        yellowPixel.setPosition(.5);
    }

    public void forward(int ticks){
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motor1.setTargetPosition(-(int)(ticks/WHEEL_CIRCUMFERENCE));
        motor2.setTargetPosition((int)(ticks/WHEEL_CIRCUMFERENCE));

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (!(motor1.getTargetPosition()-20 <= motor1.getCurrentPosition()&&motor1.getTargetPosition()+20 >= motor1.getCurrentPosition())||!(motor2.getTargetPosition()-20 <= motor2.getCurrentPosition()&&motor2.getTargetPosition()+20 >= motor2.getCurrentPosition())){
            motor1.setPower(.35*(1-((double)motor1.getCurrentPosition()/motor1.getTargetPosition())));
            motor2.setPower(.35*(1-((double)motor2.getCurrentPosition()/motor2.getTargetPosition())));
            l_op.telemetry.addData("motor1 Position: ", motor1.getCurrentPosition());
            l_op.telemetry.addData("motor2 position: ", motor2.getCurrentPosition());
            l_op.telemetry.update();
        }
        motor1.setPower(0);
        motor2.setPower(0);
    }

    public void turn(int degrees){
        degrees *= DEGREE_TO_TICK;

        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motor1.setTargetPosition(degrees);
        motor2.setTargetPosition(degrees);

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (!(motor1.getTargetPosition()-20 <= motor1.getCurrentPosition()&&motor1.getTargetPosition()+20 >= motor1.getCurrentPosition())||!(motor2.getTargetPosition()-20 <= motor2.getCurrentPosition()&&motor2.getTargetPosition()+20 >= motor2.getCurrentPosition())){
            motor1.setPower(.5*(1-((double)motor1.getCurrentPosition()/motor1.getTargetPosition())));
            motor2.setPower(.5*(1-((double)motor2.getCurrentPosition()/motor2.getTargetPosition())));
            l_op.telemetry.addData("motor1 Position: ", motor1.getCurrentPosition());
            l_op.telemetry.addData("motor2 position: ", motor2.getCurrentPosition());
            l_op.telemetry.update();
        }
        motor1.setPower(0);
        motor2.setPower(0);
    }

    public void forward(int ticks, double maxSpeed){
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motor1.setTargetPosition(-(int)(ticks/WHEEL_CIRCUMFERENCE));
        motor2.setTargetPosition((int)(ticks/WHEEL_CIRCUMFERENCE));

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (!(motor1.getTargetPosition()-20 <= motor1.getCurrentPosition()&&motor1.getTargetPosition()+20 >= motor1.getCurrentPosition())||!(motor2.getTargetPosition()-20 <= motor2.getCurrentPosition()&&motor2.getTargetPosition()+20 >= motor2.getCurrentPosition())){
            motor1.setPower(maxSpeed*(1-((double)motor1.getCurrentPosition()/motor1.getTargetPosition())));
            motor2.setPower(maxSpeed*(1-((double)motor2.getCurrentPosition()/motor2.getTargetPosition())));
            l_op.telemetry.addData("motor1 Position: ", motor1.getCurrentPosition());
            l_op.telemetry.addData("motor2 position: ", motor2.getCurrentPosition());
            l_op.telemetry.update();
        }
        motor1.setPower(0);
        motor2.setPower(0);
    }

    public void turn(int degrees, double maxSpeed){
        degrees *= DEGREE_TO_TICK;

        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motor1.setTargetPosition(degrees);
        motor2.setTargetPosition(degrees);

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (!(motor1.getTargetPosition()-20 <= motor1.getCurrentPosition()&&motor1.getTargetPosition()+20 >= motor1.getCurrentPosition())||!(motor2.getTargetPosition()-20 <= motor2.getCurrentPosition()&&motor2.getTargetPosition()+20 >= motor2.getCurrentPosition())){
            motor1.setPower(maxSpeed*(1-((double)motor1.getCurrentPosition()/motor1.getTargetPosition())));
            motor2.setPower(maxSpeed*(1-((double)motor2.getCurrentPosition()/motor2.getTargetPosition())));
            l_op.telemetry.addData("motor1 Position: ", motor1.getCurrentPosition());
            l_op.telemetry.addData("motor2 position: ", motor2.getCurrentPosition());
            l_op.telemetry.update();
        }
        motor1.setPower(0);
        motor2.setPower(0);
    }

}
