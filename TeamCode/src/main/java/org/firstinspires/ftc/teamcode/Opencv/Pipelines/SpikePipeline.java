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
public class SpikePipeline extends OpenCvPipeline {
    Telemetry telemetry;
    // rectangles
    public Rect lastValidBoundingRect;
    //points
    //backlog of frames to average out to reduce noise
    ArrayList<double[]> frameList;
    //these are public static to be tuned in dashboard
    public static double LowH = 0.0;
    public static double LowS = 126.1;
    public static double LowV = 174.4;
    public static double HighH = 26.9;
    public static double HighS = 255;
    public static double HighV = 255;

//    public static double strictLowH = 0;
//    public static double strictLowS = 158.7;
//    public static double strictLowV = 194.1;
//    public static double strictHighH = 255;
//    public static double strictHighS = 255;
//    public static double strictHighV = 255;
//    public static double threshold1 = 100;
//    public static double threshold2 = 200;

    private Point start = null;
    private Point end = null;
    public SpikePipeline() {
        frameList = new ArrayList<>();
    }



    public SpikePipeline(Telemetry t) {telemetry = t;}

    @Override
    public Mat processFrame(Mat input) {
        Mat mat = new Mat();



        //mat turns into HSV value
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        if (mat.empty()) {
            return input;
        }

        Scalar lowHSV = new Scalar(LowH, LowS, LowV);
        Scalar higHSV = new Scalar(HighH, HighS, HighV);

        Core.inRange(mat,lowHSV,higHSV,mat);

        Imgproc.morphologyEx(mat,mat,Imgproc.MORPH_OPEN,Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(5,4)));

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(mat,contours,hierarchy,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);



        Imgproc.cvtColor(mat,mat,Imgproc.COLOR_GRAY2RGB);
        Imgproc.drawContours(mat,contours,-1,new Scalar(255,255,0),2);

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

            if (boundRect.length > 0)
                for (Rect rect : boundRect) {
                    if (rect.width > 10 && rect.height > 20) {
                        lastValidBoundingRect = rect.clone();
                    }
                }

        }



        if (lastValidBoundingRect != null) {
            double fromSide = lastValidBoundingRect.x;
            double Halfside = lastValidBoundingRect.y + lastValidBoundingRect.height/2;
            Point start = new Point(fromSide, Halfside);
            Point end = new Point(0,Halfside);
            Imgproc.line(mat, start,end , new Scalar(255, 0, 255));

        }


        return mat;
    }
    public double LENGTH(){
        double length = (int) Core.norm(new MatOfPoint2f(start), new MatOfPoint2f(end));
        return length;
    }


}





































































































































































































































































































//  (☞ﾟヮﾟ)☞ Adam was here  ☜(ﾟヮﾟ☜)  //