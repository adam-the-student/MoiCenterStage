package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


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
        leftWheel = l_op.hardwareMap.get(DcMotor.class, "frontLeft");
        rightWheel = l_op.hardwareMap.get(DcMotor.class, "frontRight");
    }

    //8192 counts per revolution (REV Through Bore Encoder)
    //Odo Wheel 2 inch Diameter => 2 * π
    // Distance / Circumference (^^) * 8192 (counts)

    public void move(double y, double x, double r){

        leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if (x+y!=0) {
            double yVector = y / (x+y);
            double xVector = x / (x+y);

            double denominator = Math.max(Math.abs(y) + Math.abs(x), 1);
            double motor1Power = (yVector + xVector) / denominator;  //motor1 is top left corner
            double motor2Power = (yVector - xVector) / denominator;  //motor2 is top right corner
            double motor3Power = (yVector - xVector) / denominator;  //motor3 is bottom left corner
            double motor4Power = (yVector + xVector) / denominator;  //motor4 is bottom right corner

            leftWheel.setTargetPosition((int) (y / (Math.PI * 2) * 8192));
            rightWheel.setTargetPosition(-(int) (y / (Math.PI * 2) * 8192));

            leftWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            while ((!(leftWheel.getTargetPosition()+10< leftWheel.getCurrentPosition())||!(leftWheel.getTargetPosition()-10>leftWheel.getCurrentPosition())&&(!(rightWheel.getTargetPosition()+10< rightWheel.getCurrentPosition())||!(rightWheel.getTargetPosition()-10>rightWheel.getCurrentPosition())))){
                motor1.setPower(motor1Power*(1-((double)motor1.getCurrentPosition()/motor1.getTargetPosition())));
                motor2.setPower(motor2Power*(1-((double)motor2.getCurrentPosition()/motor2.getTargetPosition())));
                motor3.setPower(motor3Power*(1-((double)motor3.getCurrentPosition()/motor3.getTargetPosition())));
                motor4.setPower(motor4Power*(1-((double)motor4.getCurrentPosition()/motor4.getTargetPosition())));
            }
            motor1.setPower(0);
            motor2.setPower(0);
            motor3.setPower(0);
            motor4.setPower(0);

            double error = leftWheel.getCurrentPosition() - rightWheel.getCurrentPosition();
            if (Math.abs(error)>20){
                    turn(error/(r/720*339*Math.PI*8192));
            }
        }
        //robot track width = 13.5inch - 4mm = 339mm
        // (degree (r) / 360) * 339 / 2 * π * 8192 => distance
        if (r != 0) {
            turn(r);
        }
    }
    private void turn(double r){

            leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            leftWheel.setTargetPosition((int)(r/720*339*Math.PI*8192));
            rightWheel.setTargetPosition((int)(r/720*339*Math.PI*8192));


            leftWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while ((!(leftWheel.getTargetPosition()+10< leftWheel.getCurrentPosition())||!(leftWheel.getTargetPosition()-10>leftWheel.getCurrentPosition())&&(!(rightWheel.getTargetPosition()+10< rightWheel.getCurrentPosition())||!(rightWheel.getTargetPosition()-10>rightWheel.getCurrentPosition())))){
            motor1.setPower(.5*(1-((double)motor1.getCurrentPosition()/motor1.getTargetPosition())));
            motor2.setPower(.5*(1-((double)motor2.getCurrentPosition()/motor2.getTargetPosition())));
            motor3.setPower(.5*(1-((double)motor3.getCurrentPosition()/motor3.getTargetPosition())));
            motor4.setPower(.5*(1-((double)motor4.getCurrentPosition()/motor4.getTargetPosition())));
        }
        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);

    }
}
