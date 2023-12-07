package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="aPieceOfJunkTeleOp")

public class Robits_CENTERSTAGE_Starterbot_Encoderless extends OpMode
{

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor backLeft = null;
    private DcMotor backRight = null;
    private DcMotor armDrive = null;
    private Servo wrist = null;
    private CRServo roller = null;
    private double open = 0.8;//wrist pickup
    private double closed = 0.35;//wrist stowed

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        backLeft= hardwareMap.dcMotor.get("backLeft"); // Initialize left drive motor
        backRight = hardwareMap.dcMotor.get("backRight"); // Initialize right drive motor
        wrist = hardwareMap.get(Servo.class, "wrist");
        roller = hardwareMap.get(CRServo.class, "roller");
        armDrive = hardwareMap.get(DcMotor.class, "armDrive");

        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");

    }


    @Override
    public void init_loop() {
    }


    @Override
    public void start() {
        runtime.reset();
    }


    @Override
    public void loop() {

        double leftpower;//drive computation
        double rightpower;


        leftpower = gamepad1.left_stick_y - gamepad1.left_stick_x;
        rightpower = gamepad1.left_stick_y + gamepad1.left_stick_x;

        if (gamepad1.left_bumper){
            backLeft.setPower(leftpower*0.25);
            backRight.setPower(rightpower*0.25);
        }
        else if (gamepad1.right_bumper){
            backLeft.setPower(leftpower*2);
            backRight.setPower(rightpower*2);
        }
        else {
            backLeft.setPower(leftpower*.5);
            backRight.setPower(rightpower*.5);
        }


        armDrive.setPower(gamepad2.right_stick_y*.35);//arm on right stick y

        if (gamepad1.left_trigger > 0){//pickup
            wrist.setPosition(open);//wrist servo position pickup
            roller.setPower(1.0);//sets roller to intake
        }
        else if (gamepad1.right_trigger > 0){//drop
            roller.setPower(-1.0);//sets roller to outtake
        }
        else if (gamepad2.left_bumper){//fold wrist
            wrist.setPosition(open); //wrist to open position
        }
        else if (gamepad2.right_bumper){//fold up wrist
            wrist.setPosition(closed); //wrist to stow position
        }
        else{
            roller.setPower(0.0);
        }



        telemetry.addData("Wrist", wrist.getPosition());//data on wrist position
        telemetry.addData("Roller claw", roller.getPower());//data on roller power
        telemetry.update();
    }
    @Override
    public void stop() {
    }

}
// 10