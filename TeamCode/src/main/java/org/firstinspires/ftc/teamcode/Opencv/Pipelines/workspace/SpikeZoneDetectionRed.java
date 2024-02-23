package org.firstinspires.ftc.teamcode.Opencv.Pipelines.workspace;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class SpikeZoneDetectionRed extends OpenCvPipeline {
    Telemetry telemetry;
    private double largestArea = 0;
    public Rect boundingRect;
    private byte spikeZone = -1;
    ArrayList<double[]> frameList;
    public static double LowH = 0;
    public static double LowS = 75.1;
    public static double LowV = 0;
    public static double HighH = 11.3;
    public static double HighS = 255;
    public static double HighV = 255;
    Mat hierarchy = new Mat();
    Mat mat = new Mat();

    public SpikeZoneDetectionRed() {
        frameList = new ArrayList<>();
    }

    public SpikeZoneDetectionRed(Telemetry t) {
        telemetry = t;
    }

    @Override
    public Mat processFrame(Mat input) {
        largestArea = 0;
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        if (mat.empty()) {
            return input;
        }

        // Draw solid rectangles to divide the screen into two equal parts
        Imgproc.rectangle(mat, new Point(0, 0),
                new Point(input.width(),140),
                new Scalar(128,0, 0), -1);

        Imgproc.rectangle(mat, new Point(input.width(), input.height()),
                new Point(0,170),
                new Scalar(128, 0, 0), -1);
        Scalar lowHSV = new Scalar(LowH, LowS, LowV);
        Scalar highHSV = new Scalar(HighH, HighS, HighV);

        Core.inRange(mat, lowHSV, highHSV, mat);

        Imgproc.morphologyEx(mat, mat, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 4)));

        List<MatOfPoint> contours = new ArrayList<>();

        Imgproc.findContours(mat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);
        Imgproc.drawContours(mat, contours, -1, new Scalar(255, 255, 0), 2);

        // Calculate the width of each zone
        int imageWidth = input.width();
        int zoneWidth = imageWidth / 2;
        spikeZone=2;

        for (MatOfPoint contour : contours) {
            boundingRect = Imgproc.boundingRect(contour);

            if (boundingRect.area() < largestArea) {
                continue;
            }
            largestArea = boundingRect.area();

            // Calculate the center of the bounding rectangle
            Point center = new Point(boundingRect.x + (double) boundingRect.width / 2, boundingRect.y + (double) boundingRect.height / 2);

            // Determine which zone the center of the bounding rectangle falls into
            if (center.x < zoneWidth) {
                spikeZone = 0; // Zone 0 for the left side
            } else {
                spikeZone = 1; // Zone 1 for the right side
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

    public byte getData() {
        return (byte) (spikeZone + 1);
    }
}
