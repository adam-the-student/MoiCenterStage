package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class ParkAuton extends LinearOpMode {

    DcMotor motor1, motor2, motor3, motor4, intakeMotor;
    @Override
    public void runOpMode() throws InterruptedException {
        motor1 = hardwareMap.get(DcMotor.class, "frontLeft");
        motor2 = hardwareMap.get(DcMotor.class,"frontRight");
        motor3 = hardwareMap.get(DcMotor.class,"backLeft");
        motor4 = hardwareMap.get(DcMotor.class,"backRight");
        waitForStart();
        lineFromVector(0,1,0,4);
    }

    public void lineFromVector(double xVector, double yVector, double rx, double distance){

        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double denominator = Math.max(Math.abs(yVector) + Math.abs(xVector) + Math.abs(rx), 1);
        double motor1Power = (yVector - xVector + rx) / denominator;
        double motor2Power = (yVector + xVector - rx) / denominator;
        double motor3Power = (yVector + xVector + rx) / denominator;
        double motor4Power = (yVector - xVector - rx) / denominator;

        int targetDistance = (int)(distance / Math.sqrt(Math.pow(yVector,2)+Math.pow(xVector,2) + rx)*103.6);

        motor1.setTargetPosition(targetDistance);
        motor2.setTargetPosition(-targetDistance);
        motor3.setTargetPosition(targetDistance);
        motor4.setTargetPosition(-targetDistance);

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor4.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motor1.setPower(motor1Power/2);  // motor1 is top left
        motor2.setPower(motor2Power/2);  // motor2 is top right
        motor3.setPower(-motor3Power/2);  // motor3 is bottom left
        motor4.setPower(motor4Power/2);

        while (!(motor1.getTargetPosition()-10 <= motor1.getCurrentPosition()&&motor1.getTargetPosition()+10 >= motor1.getCurrentPosition())&&!(motor2.getTargetPosition()-10 <= motor2.getCurrentPosition()&&motor2.getTargetPosition()+10 >= motor2.getCurrentPosition())&&!(motor3.getTargetPosition()-10 <= motor3.getCurrentPosition()&&motor3.getTargetPosition()+10 >= motor3.getCurrentPosition())&&!(motor4.getTargetPosition()-10 <= motor4.getCurrentPosition()&&motor4.getTargetPosition()+10 >= motor4.getCurrentPosition())){
            telemetry.addData("motor1 Position: ", motor1.getCurrentPosition());
            telemetry.addData("motor2 position: ", motor2.getCurrentPosition());
            telemetry.addData("motor3 Position: ", motor3.getCurrentPosition());
            telemetry.addData("motor4 position: ", motor4.getCurrentPosition());
            telemetry.update();
        }

        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);

    }
}
