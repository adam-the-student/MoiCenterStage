package org.firstinspires.ftc.teamcode.Autons;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

import java.awt.font.NumericShaper;


public class Parallel2WheelOdo {
    LinearOpMode l_op;
    DcMotor motor1, motor2, motor3, motor4;
    DcMotor leftWheel, rightWheel;

    public Parallel2WheelOdo(LinearOpMode l_op) {
        this.l_op = l_op;
        motor1 = l_op.hardwareMap.get(DcMotor.class, "frontLeft");
        motor2 = l_op.hardwareMap.get(DcMotor.class, "frontRight");
        motor3 = l_op.hardwareMap.get(DcMotor.class, "backLeft");
        motor4 = l_op.hardwareMap.get(DcMotor.class, "backRight");
        leftWheel = l_op.hardwareMap.get(DcMotor.class, "par1");
        rightWheel = l_op.hardwareMap.get(DcMotor.class, "par0");

        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor4.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    //8192 counts per revolution (REV Through Bore Encoder)
    //Odo Wheel 2 inch Diameter => 2 * π
    // Distance / Circumference (^^) * 8192 (counts)

    public void move(double y, double x, double r){

        leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if ((x+y)!=0) {
            double yVector = y / Math.abs(x+y);
            double xVector = x / Math.abs(x+y);

            double denominator = Math.max(Math.abs(yVector) + Math.abs(xVector), 1);
            double motor1Power = (yVector + xVector) / denominator;  //motor1 is top left corner
            double motor2Power = (yVector - xVector) / denominator;  //motor2 is top right corner
            double motor3Power = (yVector - xVector) / denominator;  //motor3 is bottom left corner
            double motor4Power = (yVector + xVector) / denominator;  //motor4 is bottom right corner

            leftWheel.setTargetPosition((int) (y / (Math.PI * 2) * 8192));
            rightWheel.setTargetPosition(-(int) (y / (Math.PI * 2) * 8192));

            leftWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            int error = 0;
            if (leftWheel.getTargetPosition()>0) {
                while (leftWheel.getCurrentPosition()< leftWheel.getTargetPosition()||rightWheel.getCurrentPosition() > rightWheel.getTargetPosition()) {
                    motor1.setPower(motor1Power*Math.pow(1.25,-30*Math.pow((1-Math.abs((double)leftWheel.getCurrentPosition()/leftWheel.getTargetPosition()))-.45,2))*.65);
                    motor2.setPower(-motor2Power*Math.pow(1.25,-30*Math.pow((1-Math.abs((double)rightWheel.getCurrentPosition()/rightWheel.getTargetPosition()))-.45,2))*.65);
                    motor3.setPower(motor3Power*Math.pow(1.25,-30*Math.pow((1-Math.abs((double)leftWheel.getCurrentPosition()/leftWheel.getTargetPosition()))-.45,2))*.65);
                    motor4.setPower(-motor4Power*Math.pow(1.25,-30*Math.pow((1-Math.abs((double)rightWheel.getCurrentPosition()/rightWheel.getTargetPosition()))-.45,2))*.65);

                    error = Math.abs(leftWheel.getCurrentPosition()) - Math.abs(rightWheel.getCurrentPosition());

                    l_op.telemetry.addData("Front Left : ", motor1.getPower());
                    l_op.telemetry.addData("Front Right : ", motor2.getPower());
                    l_op.telemetry.addData("Back Left: ", motor3.getPower());
                    l_op.telemetry.addData("Back Right: ", motor4.getPower());
                    l_op.telemetry.addData("Left Encoder: ", leftWheel.getCurrentPosition());
                    l_op.telemetry.addData("Right Encoder: ", rightWheel.getCurrentPosition());
                    l_op.telemetry.addLine();
                    l_op.telemetry.addData("Error: ", error);
                    l_op.telemetry.update();
                }
            } else {
                while (leftWheel.getCurrentPosition() > leftWheel.getTargetPosition()||rightWheel.getCurrentPosition() < rightWheel.getTargetPosition()) {
                    motor1.setPower(motor1Power*Math.pow(1.25,-30*Math.pow((1-Math.abs((double)leftWheel.getCurrentPosition()/leftWheel.getTargetPosition()))-.45,2))*.65);
                    motor2.setPower(-motor2Power*Math.pow(1.25,-30*Math.pow((1-Math.abs((double)rightWheel.getCurrentPosition()/rightWheel.getTargetPosition()))-.45,2))*.65);
                    motor3.setPower(motor3Power*Math.pow(1.25,-30*Math.pow((1-Math.abs((double)leftWheel.getCurrentPosition()/leftWheel.getTargetPosition()))-.45,2))*.65);
                    motor4.setPower(-motor4Power*Math.pow(1.25,-30*Math.pow((1-Math.abs((double)rightWheel.getCurrentPosition()/rightWheel.getTargetPosition()))-.45,2))*.65);

                    error = Math.abs(leftWheel.getCurrentPosition()) - Math.abs(rightWheel.getCurrentPosition());

                    l_op.telemetry.addData("Front Left : ", motor1.getPower());
                    l_op.telemetry.addData("Front Right : ", motor2.getPower());
                    l_op.telemetry.addData("Back Left: ", motor3.getPower());
                    l_op.telemetry.addData("Back Right: ", motor4.getPower());
                    l_op.telemetry.addData("Left Encoder: ", leftWheel.getCurrentPosition());
                    l_op.telemetry.addData("Right Encoder: ", rightWheel.getCurrentPosition());
                    l_op.telemetry.addLine();
                    l_op.telemetry.addData("Error: ", error);
                    l_op.telemetry.update();
                }
            }
            motor1.setPower(-motor1Power/5);
            motor2.setPower(-motor2Power/5);
            motor3.setPower(-motor3Power/5);
            motor4.setPower(-motor4Power/5);

            motor1.setPower(0);
            motor2.setPower(0);
            motor3.setPower(0);
            motor4.setPower(0);

            if (Math.abs(error)>20){
                l_op.telemetry.addData("Error: ", error);
                l_op.telemetry.update();
                    turnTicks(-error);
            }
        }

        //robot track width = 13.5inch - 4mm = 339mm
        // (degree (r) / 360) * 339 / 2 * π * 8192 => distance
        if (r != 0) {
            turn(r);
        }
    }
    private void turnTicks(int rTicks){

            leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            leftWheel.setTargetPosition(rTicks);
            rightWheel.setTargetPosition(rTicks);


            leftWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (Math.abs(leftWheel.getCurrentPosition())<Math.abs(leftWheel.getTargetPosition())){
            double motorPower = (rTicks>0)?-1:1 * (1-(double)leftWheel.getCurrentPosition()/leftWheel.getTargetPosition());
            motor1.setPower(motorPower);
            motor2.setPower(motorPower);
            motor3.setPower(motorPower);
            motor4.setPower(motorPower);
        }
        motor1.setPower((rTicks>0)?.1:-.1);
        motor2.setPower((rTicks>0)?.1:-.1);
        motor3.setPower((rTicks>0)?.1:-.1);
        motor4.setPower((rTicks>0)?.1:-.1);

        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);
    }
    private void turn(double r) {

        leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftWheel.setTargetPosition((int) (r / 720 * 339 * Math.PI * 8192));
        rightWheel.setTargetPosition((int) (r / 720 * 339 * Math.PI * 8192));


        leftWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (leftWheel.getTargetPosition() > 0) {
            while (leftWheel.getCurrentPosition() > leftWheel.getTargetPosition()) {

                motor1.setPower(-.5);
                motor2.setPower(-.5);
                motor3.setPower(-.5);
                motor4.setPower(-.5);
                l_op.telemetry.addData("Left Encoder: ", leftWheel.getCurrentPosition());
                l_op.telemetry.addData("Right Encoder: ", rightWheel.getCurrentPosition());
                l_op.telemetry.update();
            }
            motor1.setPower(0);
            motor2.setPower(0);
            motor3.setPower(0);
            motor4.setPower(0);

            leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }else {
            while (leftWheel.getCurrentPosition() < leftWheel.getTargetPosition()){

                motor1.setPower(.5);
                motor2.setPower(.5);
                motor3.setPower(.5);
                motor4.setPower(.5);
                l_op.telemetry.addData("Left Encoder: ",leftWheel.getCurrentPosition());
                l_op.telemetry.addData("Right Encoder: ",rightWheel.getCurrentPosition());
                l_op.telemetry.update();
            }
            motor1.setPower(0);
            motor2.setPower(0);
            motor3.setPower(0);
            motor4.setPower(0);

            leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

    }
    public void selfCorrect(){
        leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftWheel.setTargetPosition(0);
        rightWheel.setTargetPosition(0);

        leftWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (!l_op.gamepad1.circle){
            if (leftWheel.getCurrentPosition() > 10 || leftWheel.getCurrentPosition() < -10) {
                if (Math.abs(leftWheel.getCurrentPosition())-Math.abs(rightWheel.getCurrentPosition())<10){

                }
            }
        }
    }
}
