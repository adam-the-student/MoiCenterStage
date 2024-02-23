package org.firstinspires.ftc.teamcode.Opencv.Pipelines.workspace;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Opencv.Pipelines.TransitionPipeline;
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

public class SpikeZoneDetectionBlue extends TransitionPipeline {
    Telemetry telemetry;
    private double largestArea = 0;
    public Rect boundingRect;
    private byte spikeZone = 2; // Initially set to 2 to indicate no spike detected
    ArrayList<double[]> frameList;
    public static double LowH = 107.7;
    public static double LowS = 124.7;
    public static double LowV = 117.6;
    public static double HighH = 140.3;
    public static double HighS = 226.7;
    public static double HighV = 255;
    Mat hierarchy = new Mat();
    Mat mat = new Mat();

    public SpikeZoneDetectionBlue() {
        frameList = new ArrayList<>();
    }

    public SpikeZoneDetectionBlue(Telemetry t) {
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
        boundingRect = new Rect(new Point(1,1),new Point(2,2));
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
            telemetry.addData("Spike Zone", "Zone " + spikeZone);
            telemetry.addData("Area", boundingRect.area());
            telemetry.update();
        }

        return mat;
    }

    public byte getData() {
        return (byte) (spikeZone + 1);
    }
}
