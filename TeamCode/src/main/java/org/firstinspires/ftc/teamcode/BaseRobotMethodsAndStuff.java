package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

public class BaseRobotMethodsAndStuff {
    public enum ClawSide{
        LEFT,
        RIGHT
    }
    public enum ClawPos{
        UP,
        DOWN
    }

    LinearOpMode l_op;
    ElapsedTime elapsedTime;

    boolean isUp= false;
    boolean yes = true;
    byte inverseControls = 1;

     DcMotor motor1, motor2, motor3, motor4, leftRig, rightRig, wormDrive, slideMotor;
     Servo drone, leftHook, rightHook;
     CRServo leftClaw, rightClaw, wrist;
     RevTouchSensor armLimit;

    private final double ACCEL_GAIN = 0.01;
    private final double MAX_VELO = 0.7;
    private final double MAX_TURN_VELO = 0.4;
    private final double STRAFE_VECTOR = 1;
    private final double TICKS_PER_REV = (((1+((double) 46 /17))) * (1+((double) 46 /17))) * 28;
    private final double WHEEL_CIRCUMFERENCE_INCH = 11.87373601358268;
    private final double TICKS_PER_INCH = TICKS_PER_REV / WHEEL_CIRCUMFERENCE_INCH;
    private final double TRACK_WIDTH_INCH = 13;

    public BaseRobotMethodsAndStuff(LinearOpMode linearOpMode){
        l_op = linearOpMode;
        motor1 = l_op.hardwareMap.get(DcMotor.class, "frontLeft");
        motor2 = l_op.hardwareMap.get(DcMotor.class, "frontRight");
        motor3 = l_op.hardwareMap.get(DcMotor.class, "backLeft");
        motor4 = l_op.hardwareMap.get(DcMotor.class, "backRight");
        wormDrive = l_op.hardwareMap.get(DcMotor.class, "wormDrive");
        slideMotor = l_op.hardwareMap.get(DcMotor.class, "slideMotor");
        leftRig = l_op.hardwareMap.get(DcMotor.class, "leftRig");
        rightRig = l_op.hardwareMap.get(DcMotor.class, "rightRig");
        wrist = l_op.hardwareMap.get(CRServo.class, "wrist");
        leftHook = l_op.hardwareMap.get(Servo.class, "leftHook");
        rightHook = l_op.hardwareMap.get(Servo.class, "rightHook");
        drone = l_op.hardwareMap.get(Servo.class, "drone");
        leftClaw = l_op.hardwareMap.get(CRServo.class, "leftClaw");
        rightClaw = l_op.hardwareMap.get(CRServo.class, "rightClaw");
        armLimit = l_op.hardwareMap.get(RevTouchSensor.class, "touch");
        elapsedTime = new ElapsedTime();
    }

    public void flipArm(){
        if (!armLimit.isPressed()){
            elapsedTime.reset();
            while (!armLimit.isPressed()&&elapsedTime.time(TimeUnit.SECONDS)<5) {
                wormDrive.setPower(.75);
            }
            wormDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            wormDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void slide(int pos){
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        slideMotor.setTargetPosition((int)(TICKS_PER_INCH*pos));
        slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double motorPower = 0;
        double error = pos;

        while (Math.abs(error)>5){
            motorPower = (Math.abs(slideMotor.getCurrentPosition())<Math.abs(pos/2)?motorPower+ACCEL_GAIN:motorPower-ACCEL_GAIN);
            slideMotor.setPower(motorPower);
            error = pos-slideMotor.getCurrentPosition();
        }
        slideMotor.setPower(0);
    }

    public void armDown(){
        rightClaw.setPower(.2);
        leftClaw.setPower(-.2);
        if (armLimit.isPressed()){
            wormDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            wormDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            wormDrive.setTargetPosition((int)(-145.1*28/3+1.5));
            wormDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            double motorPower = 0;
            double error = wormDrive.getTargetPosition();

            while (Math.abs(error)>5){
                motorPower = (Math.abs(wormDrive.getCurrentPosition())<Math.abs(wormDrive.getTargetPosition()/2)?motorPower+ACCEL_GAIN+0.02:motorPower-ACCEL_GAIN-0.02);
                wormDrive.setPower(motorPower);
                error = wormDrive.getTargetPosition()-wormDrive.getCurrentPosition();
            }
            wormDrive.setPower(0);
            wormDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            wormDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void wristPos(ClawPos pos){
        if (pos==ClawPos.DOWN) {
            wrist.setPower(.85);
        } else {
            wrist.setPower(-.85);
        }
            l_op.sleep(600);
            wrist.setPower(0);

    }

    public void openClaw(ClawSide side){
        if (side==ClawSide.RIGHT){
            rightClaw.setPower(-.5);
            l_op.sleep(200);
            rightClaw.setPower(0);
        } else {
            leftClaw.setPower(.5);
            l_op.sleep(200);
            leftClaw.setPower(0);
        }
    }

    public void closeClaw(){
        while (!l_op.opModeIsActive()&&yes){
            if (l_op.gamepad1.cross){
                leftClaw.setPower(-.2);
                yes= false;
            }
        }
        yes= true;
        while (!l_op.opModeIsActive()&&yes){
            if (l_op.gamepad1.triangle){
                rightClaw.setPower(.2);
                yes= false;
            }
        }
    }

    public void motorController(DcMotor motor, int targetPos){
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motor.setTargetPosition(targetPos);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double motorPower = 0;
        double error = targetPos;

        while (Math.abs(error)>5){
            motorPower = (Math.abs(motor.getCurrentPosition())<Math.abs(targetPos/2)?motorPower+ACCEL_GAIN:motorPower-ACCEL_GAIN);
            motor.setPower(motorPower);
            error = targetPos-motor.getCurrentPosition();
        }
        motor.setPower(0);
    }

    public void resetEncoders(){
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void turn(double degree){
        resetEncoders();
        motor1.setTargetPosition((int)(degree*TICKS_PER_INCH*TRACK_WIDTH_INCH*Math.PI/180));
        motor2.setTargetPosition((int)(degree*TICKS_PER_INCH*TRACK_WIDTH_INCH*Math.PI/180));
        motor3.setTargetPosition((int)(degree*TICKS_PER_INCH*TRACK_WIDTH_INCH*Math.PI/180));
        motor4.setTargetPosition((int)(degree*TICKS_PER_INCH*TRACK_WIDTH_INCH*Math.PI/180));

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor4.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double motorPower1 = 0;
        double motorPower2 = 0;
        double motorPower3 = 0;
        double motorPower4 = 0;
        double error1 = motor1.getTargetPosition();
        double error2 = motor2.getTargetPosition();
        double error3 = motor3.getTargetPosition();
        double error4 = motor4.getTargetPosition();

        l_op.telemetry.addData("error1: ", error1);
        l_op.telemetry.addData("power1: ", motorPower2);
        l_op.telemetry.addData("error2: ", error2);
        l_op.telemetry.addData("power2: ", motorPower2);
        l_op.telemetry.addData("error3: ", error3);
        l_op.telemetry.addData("power3: ", motorPower3);
        l_op.telemetry.addData("error4: ", error4);
        l_op.telemetry.addData("power4: ", motorPower4);
        l_op.telemetry.update();

        while ((Math.abs(error1)>5||Math.abs(error2)>5||Math.abs(error3)>5||Math.abs(error4)>5)&&l_op.opModeIsActive()){
            motorPower1 = (Math.abs(motor1.getCurrentPosition())<Math.abs(motor1.getTargetPosition()-motor1.getCurrentPosition()/2)?motorPower1+ACCEL_GAIN:motorPower1-ACCEL_GAIN);
            motorPower2 = (Math.abs(motor2.getCurrentPosition())<Math.abs(motor2.getTargetPosition()-motor2.getCurrentPosition()/2)?motorPower2+ACCEL_GAIN:motorPower2-ACCEL_GAIN);
            motorPower3 = (Math.abs(motor3.getCurrentPosition())<Math.abs(motor3.getTargetPosition()-motor3.getCurrentPosition()/2)?motorPower3+ACCEL_GAIN:motorPower3-ACCEL_GAIN);
            motorPower4 = (Math.abs(motor4.getCurrentPosition())<Math.abs(motor4.getTargetPosition()-motor4.getCurrentPosition()/2)?motorPower4+ACCEL_GAIN:motorPower4-ACCEL_GAIN);

            motor1.setPower((motorPower1>MAX_TURN_VELO)?MAX_TURN_VELO:(motorPower1<MAX_TURN_VELO)?-MAX_TURN_VELO:motorPower1);
            motor2.setPower((motorPower2>MAX_TURN_VELO)?MAX_TURN_VELO:(motorPower2<MAX_TURN_VELO)?-MAX_TURN_VELO:motorPower2);
            motor3.setPower((motorPower3>MAX_TURN_VELO)?MAX_TURN_VELO:(motorPower3<MAX_TURN_VELO)?-MAX_TURN_VELO:motorPower3);
            motor4.setPower((motorPower4>MAX_TURN_VELO)?MAX_TURN_VELO:(motorPower4<MAX_TURN_VELO)?-MAX_TURN_VELO:motorPower4);

            error1 = motor1.getTargetPosition()-motor1.getCurrentPosition();
            error2 = motor2.getTargetPosition()-motor2.getCurrentPosition();
            error3 = motor3.getTargetPosition()-motor3.getCurrentPosition();
            error4 = motor4.getTargetPosition()-motor4.getCurrentPosition();

            l_op.telemetry.addData("error1: ", error1);
            l_op.telemetry.addData("power1: ", motorPower2);
            l_op.telemetry.addData("error2: ", error2);
            l_op.telemetry.addData("power2: ", motorPower2);
            l_op.telemetry.addData("error3: ", error3);
            l_op.telemetry.addData("power3: ", motorPower3);
            l_op.telemetry.addData("error4: ", error4);
            l_op.telemetry.addData("power4: ", motorPower4);
            l_op.telemetry.update();
        }
        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);
        resetEncoders();
    }

    public void calculateTargetPos(double y, double x){
        resetEncoders();
        double yVector = y*TICKS_PER_INCH;
        double xVector = x*TICKS_PER_INCH;
        motor1.setTargetPosition((int)(yVector+xVector));
        motor2.setTargetPosition((int)(-yVector+xVector));
        motor3.setTargetPosition((int)(yVector-xVector));
        motor4.setTargetPosition((int)(-yVector-xVector));

        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor3.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor4.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double motorPower1 = 0;
        double motorPower2 = 0;
        double motorPower3 = 0;
        double motorPower4 = 0;
        double error1 = motor1.getTargetPosition();
        double error2 = motor2.getTargetPosition();
        double error3 = motor3.getTargetPosition();
        double error4 = motor4.getTargetPosition();

        l_op.telemetry.addData("error1: ", error1);
        l_op.telemetry.addData("power1: ", motorPower2);
        l_op.telemetry.addData("error2: ", error2);
        l_op.telemetry.addData("power2: ", motorPower2);
        l_op.telemetry.addData("error3: ", error3);
        l_op.telemetry.addData("power3: ", motorPower3);
        l_op.telemetry.addData("error4: ", error4);
        l_op.telemetry.addData("power4: ", motorPower4);
        l_op.telemetry.update();

        while ((Math.abs(error1)>5||Math.abs(error2)>5||Math.abs(error3)>5||Math.abs(error4)>5)&&l_op.opModeIsActive()){
            motorPower1 = (Math.abs(motor1.getCurrentPosition())<Math.abs(motor1.getTargetPosition()-motor1.getCurrentPosition()/2)?motorPower1+ACCEL_GAIN:motorPower1-ACCEL_GAIN);
            motorPower2 = (Math.abs(motor2.getCurrentPosition())<Math.abs(motor2.getTargetPosition()-motor2.getCurrentPosition()/2)?motorPower2+ACCEL_GAIN:motorPower2-ACCEL_GAIN);
            motorPower3 = (Math.abs(motor3.getCurrentPosition())<Math.abs(motor3.getTargetPosition()-motor3.getCurrentPosition()/2)?motorPower3+ACCEL_GAIN:motorPower3-ACCEL_GAIN);
            motorPower4 = (Math.abs(motor4.getCurrentPosition())<Math.abs(motor4.getTargetPosition()-motor4.getCurrentPosition()/2)?motorPower4+ACCEL_GAIN:motorPower4-ACCEL_GAIN);

            motor1.setPower((motorPower1>MAX_VELO)?MAX_VELO:(motorPower1<MAX_VELO)?-MAX_VELO:motorPower1);
            motor2.setPower((motorPower2>MAX_VELO)?MAX_VELO:(motorPower2<MAX_VELO)?-MAX_VELO:motorPower2);
            motor3.setPower((motorPower3>MAX_VELO)?MAX_VELO:(motorPower3<MAX_VELO)?-MAX_VELO:motorPower3);
            motor4.setPower((motorPower4>MAX_VELO)?MAX_VELO:(motorPower4<MAX_VELO)?-MAX_VELO:motorPower4);

            error1 = motor1.getTargetPosition()-motor1.getCurrentPosition();
            error2 = motor2.getTargetPosition()-motor2.getCurrentPosition();
            error3 = motor3.getTargetPosition()-motor3.getCurrentPosition();
            error4 = motor4.getTargetPosition()-motor4.getCurrentPosition();

            l_op.telemetry.addData("error1: ", error1);
            l_op.telemetry.addData("power1: ", motorPower2);
            l_op.telemetry.addData("error2: ", error2);
            l_op.telemetry.addData("power2: ", motorPower2);
            l_op.telemetry.addData("error3: ", error3);
            l_op.telemetry.addData("power3: ", motorPower3);
            l_op.telemetry.addData("error4: ", error4);
            l_op.telemetry.addData("power4: ", motorPower4);
            l_op.telemetry.update();
        }
        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);
        motor4.setPower(0);
    }


    public void moveBase(){
        double y = l_op.gamepad1.left_stick_y;
        double x = -l_op.gamepad1.left_stick_x*1.15 ; // Counteract imperfect strafing
        double rx = l_op.gamepad1.right_stick_x*.75;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double motor1Power = (inverseControls * (y + x )- rx) / denominator;  //motor1 is top left corner
        double motor2Power = (inverseControls * (y - x) + rx) / denominator;  //motor2 is top right corner
        double motor3Power = (inverseControls * (y - x) - rx) / denominator;  //motor3 is bottom left corner
        double motor4Power = (inverseControls * (y + x) + rx) / denominator;  //motor4 is bottom right corner

        if(l_op.gamepad1.left_trigger > 0){
            motor1Power = motor1Power/4;
            motor2Power = motor2Power/4;
            motor3Power = motor3Power/4;
            motor4Power = motor4Power/4;
        }

        if (l_op.gamepad1.circle) {
            inverseControls = (byte) (inverseControls==1?-1:1);
        }

        if (l_op.gamepad1.cross){
            drone.setPosition(1);
            l_op.sleep(1000);
            drone.setPosition(0);
        }

        if (l_op.gamepad1.left_bumper) {
            motor1.setPower(0);  // motor1 is top left
            motor2.setPower(0);  // motor2 is top right
            motor3.setPower(0);  // motor3 is bottom left
            motor4.setPower(0);  // motor4 is bottom right
            leftRig.setPower(1);
            rightRig.setPower(1);
            leftHook.setPosition(.5);
            rightHook.setPosition(.5);
            l_op.sleep(900);
            leftRig.setPower(0);
            rightRig.setPower(0);
        }
        if (l_op.gamepad1.right_bumper) {
            motor1.setPower(0);  // motor1 is top left
            motor2.setPower(0);  // motor2 is top right
            motor3.setPower(0);  // motor3 is bottom left
            motor4.setPower(0);  // motor4 is bottom right
            leftRig.setPower(-1);
            rightRig.setPower(-1);
            l_op.sleep(900);
            leftRig.setPower(0);
            rightRig.setPower(0);
        }

        motor1.setPower(motor1Power);  // motor1 is top left
        motor2.setPower(-motor2Power);  // motor2 is top right
        motor3.setPower(motor3Power);  // motor3 is bottom left
        motor4.setPower(-motor4Power);  // motor4 is bottom right

        l_op.telemetry.addData("CONTROLS : ",inverseControls==1?"REGULAR":"INVERTED");
        l_op.telemetry.update();

        slideMotor.setPower(l_op.gamepad2.right_stick_y);
        wormDrive.setPower(-l_op.gamepad2.left_stick_y/1.5);

        leftClaw.setPower(l_op.gamepad2.left_bumper?.3:l_op.gamepad2.left_trigger>0?-.5:0);
        rightClaw.setPower(l_op.gamepad2.right_bumper?-.3:l_op.gamepad2.right_trigger>0?.5:0);
        wrist.setPower(l_op.gamepad2.dpad_down?.3:l_op.gamepad2.dpad_up?-.3:0);

        if (l_op.gamepad2.triangle && !armLimit.isPressed() && ! isUp){
            elapsedTime.reset();
            while (!armLimit.isPressed()&&elapsedTime.time(TimeUnit.SECONDS)<5) {
                wormDrive.setPower(.75);
            }
            wormDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            wormDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            isUp = true;
        }
        if (armLimit.isPressed()&&l_op.gamepad2.cross&&isUp){
            wormDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            wormDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            wormDrive.setTargetPosition((int)(-145.1*(28/3+1.4)));
            wormDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            double motorPower = 0;
            double error = wormDrive.getTargetPosition();

            while (Math.abs(error)>5){
                motorPower = (Math.abs(wormDrive.getCurrentPosition())<Math.abs(wormDrive.getTargetPosition()/2)?motorPower+ACCEL_GAIN+0.02:motorPower-ACCEL_GAIN-0.02);
                wormDrive.setPower(motorPower);
                error = wormDrive.getTargetPosition()-wormDrive.getCurrentPosition();
            }
            wormDrive.setPower(0);
            wormDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            wormDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            isUp = false;

        }
    }
}