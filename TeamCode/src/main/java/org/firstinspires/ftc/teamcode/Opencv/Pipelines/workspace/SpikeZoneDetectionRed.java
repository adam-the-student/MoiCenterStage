package org.firstinspires.ftc.teamcode.Opencv.Pipelines.workspace;

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
    // rectangles
    public Rect lastValidBoundingRect;
    // points
    private Point start = null;
    private Point end = null;
    // backlog of frames to average out to reduce noise
    ArrayList<double[]> frameList;
    // these are public static to be tuned in the dashboard
    public static double LowH = 0.0;
    public static double LowS = 126.1;
    public static double LowV = 174.4;
    public static double HighH = 26.9;
    public static double HighS = 255;
    public static double HighV = 255;

    public SpikeZoneDetectionRed() {
        frameList = new ArrayList<>();
    }

    public SpikeZoneDetectionRed(Telemetry t) {
        telemetry = t;
    }

    @Override
    public Mat processFrame(Mat input) {
        Mat mat = new Mat();

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
        Mat hierarchy = new Mat();

        Imgproc.findContours(mat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);
        Imgproc.drawContours(mat, contours, -1, new Scalar(255, 255, 0), 2);

        if (contours.size() > 0) {
            MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
            Rect[] boundRect = new Rect[contours.size()];
            for (int i = 0; i < contours.size(); i++) {
                contoursPoly[i] = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
                boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
                // draw red bounding rectangles on mat
                // the mat has been converted to HSV so we need to use HSV as well
                Imgproc.rectangle(mat, boundRect[i], new Scalar(0.5, 76.9, 89.8));
            }

            if (boundRect.length > 0) {
                for (Rect rect : boundRect) {
                    if (rect.width > 10 && rect.height > 20) {
                        lastValidBoundingRect = rect.clone();
                    }
                }
            }
        }

        if (lastValidBoundingRect != null) {
            double fromSide = lastValidBoundingRect.x + lastValidBoundingRect.width / 2.0; // Update the calculation
            double Halfside = lastValidBoundingRect.y + lastValidBoundingRect.height / 2.0;
            Point start = new Point(fromSide, Halfside);
            Point end = new Point(0, Halfside);
            Imgproc.line(mat, start, end, new Scalar(255, 0, 255));

            // Calculate the third of the screen
            double third = input.cols() / 3.0;

            String zone;
            if (fromSide < third) {
                zone = "Left Third";
            } else if (fromSide < 2 * third) {
                zone = "Center Third";
            } else {
                zone = "Right Third";
            }

            telemetry.addData("Position", zone);
            telemetry.update();
        }

        return mat;
    }

    public double LENGTH() {
        double length = (int) Core.norm(new MatOfPoint2f(start), new MatOfPoint2f(end));
        return length;
    }
}
