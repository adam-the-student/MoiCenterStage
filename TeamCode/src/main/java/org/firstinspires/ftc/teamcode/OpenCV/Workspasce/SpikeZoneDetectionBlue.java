package org.firstinspires.ftc.teamcode.OpenCV.Workspasce;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.OpenCV.TransitionPipeline;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class SpikeZoneDetectionBlue extends TransitionPipeline {
    Telemetry telemetry;
    private double largestArea = 0;
    // rectangles
    public Rect boundingRect;
    // points
    private Point start = null;
    private Point end = null;
    Point center = null;
    private byte spikeZone=0;
    // backlog of frames to average out to reduce noise
    ArrayList<double[]> frameList;

    Mat hierarchy = new Mat();
    Mat mat = new Mat();
    // these are public static to be tuned in the dashboard
    public static double LowH = 107.7;
    public static double LowS = 124.7;
    public static double LowV = 117.6;
    public static double HighH = 140.3;
    public static double HighS = 226.7;
    public static double HighV = 255;

    public SpikeZoneDetectionBlue() {
        frameList = new ArrayList<>();
    }

    public SpikeZoneDetectionBlue(Telemetry t) {
        telemetry = t;
    }

    @Override
    public Mat processFrame(Mat input) {

        // mat turns into HSV value
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        if (mat.empty()) {
            return input;
        }

        Scalar lowHSV = new Scalar(LowH, LowS, LowV);
        Scalar higHSV = new Scalar(HighH, HighS, HighV);

        Core.inRange(mat, lowHSV, higHSV, mat);

        Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 4)));

        List<MatOfPoint> contours = new ArrayList<>();

        Imgproc.findContours(mat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);
        Imgproc.drawContours(mat, contours, -1, new Scalar(255, 255, 0), 2);

        // Calculate the width of each zone
        int imageWidth = input.width();
        int zoneWidth = imageWidth / 3;

        // Initialize variables to track which zone the spike is in
        // -1 indicates no spike found in any zone

        for (MatOfPoint contour : contours) {
            boundingRect = Imgproc.boundingRect(contour);

            if (boundingRect.area()<largestArea){
                continue;
            }
            largestArea = boundingRect.area();

            // Calculate the center of the bounding rectangle
            Point center = new Point(boundingRect.x + (double)boundingRect.width / 2, boundingRect.y + (double)boundingRect.height / 2);

            // Determine which zone the center of the bounding rectangle falls into
            byte zone = (byte) (center.x / zoneWidth);

            // Update the spikeZone if the contour is found in a zone
            if (zone >= 0 && zone <= 2) {
                spikeZone = zone;
            }
        }

        // Send telemetry based on the spikeZone
        if (telemetry != null) {
            if (spikeZone == -1) {
                telemetry.addData("Spike Zone", "No spike detected");
            } else {
                telemetry.addData("Spike Zone", "Zone " + spikeZone);
            }
            telemetry.update();
        }

        return mat;
    }

    @Override
    public byte getZone(){
        return (byte) (spikeZone+1);
    }
}