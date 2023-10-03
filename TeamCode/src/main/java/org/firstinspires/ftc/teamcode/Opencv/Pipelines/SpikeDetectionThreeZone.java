package org.firstinspires.ftc.teamcode.Opencv.Pipelines;


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


//for dashboard
/*@Config*/
public class SpikeDetectionThreeZone extends OpenCvPipeline {
    Telemetry telemetry;
    // rectangles
    public Rect lastValidBoundingRect;
    //points
    //backlog of frames to average out to reduce noise
    ArrayList<double[]> frameList;
    //these are public static to be tuned in dashboard
    public static double LowH = 0;
    public static double LowS = 0;
    public static double LowV = 171.4;
    public static double HighH = 174.3;
    public static double HighS = 51;
    public static double HighV = 255;

    int spikeZone = -1;
    private Point start = null;
    private Point end = null;
    public SpikeDetectionThreeZone() {
        frameList = new ArrayList<>();
    }

    public SpikeDetectionThreeZone(Telemetry t) {telemetry = t;}

    @Override
    public Mat processFrame(Mat input) {
        // mat turns into HSV value
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2HSV);
        if (input.empty()) {
            return input;
        }

        // Converts RGB Frame to HSV
        Scalar lowHSV = new Scalar(LowH, LowS, LowV);
        Scalar highHSV = new Scalar(HighH, HighS, HighV);

        Core.inRange(input, lowHSV, highHSV, input);

        Imgproc.morphologyEx(input, input, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10)));

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(input, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        // Calculate the width of each zone
        int imageWidth = input.width();
        int zoneWidth = imageWidth / 3;

        // Initialize variables to track which zone the spike is in
        // -1 indicates no spike found in any zone

        for (MatOfPoint contour : contours) {
            Rect boundingRect = Imgproc.boundingRect(contour);

            // Calculate the center of the bounding rectangle
            Point center = new Point(boundingRect.x + boundingRect.width / 2, boundingRect.y + boundingRect.height / 2);

            // Determine which zone the center of the bounding rectangle falls into
            int zone = (int) (center.x / zoneWidth);

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

        return input;
    }
    public byte getspikeZone(){
        return (byte)spikeZone;
    }

}





































































































































































































































































































//  (☞ﾟヮﾟ)☞ Adam was here  ☜(ﾟヮﾟ☜)  //