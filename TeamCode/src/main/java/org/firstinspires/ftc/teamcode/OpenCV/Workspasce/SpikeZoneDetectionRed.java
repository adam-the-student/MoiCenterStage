package org.firstinspires.ftc.teamcode.OpenCV.Workspasce;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
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
    // rectangles
    public Rect boundingRect;
    // points
    private Point start = null;
    private Point end = null;
    Point center = null;
    private byte spikeZone=0;
    // backlog of frames to average out to reduce noise
    ArrayList<double[]> frameList;
    // these are public static to be tuned in the dashboard
    public static double LowH = 5.0;
    public static double LowS = 126.1;
    public static double LowV = 200;
    public static double HighH = 26.9;
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

//        if (contours.size() > 0) {
//            MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
//            Rect[] boundRect = new Rect[contours.size()];
//            for (int i = 0; i < contours.size(); i++) {
//                contoursPoly[i] = new MatOfPoint2f();
//                Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
//                boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
//                // draw red bounding rectangles on mat
//                // the mat has been converted to HSV so we need to use HSV as well
//                Imgproc.rectangle(mat, boundRect[i], new Scalar(0.5, 76.9, 89.8));
//            }
//        }

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

    public int LENGTH() {
        return (int) Core.norm(new MatOfPoint2f(start), new MatOfPoint2f(end));
    }

    public byte getZone() {
        return (byte)(spikeZone+1);
    }
}