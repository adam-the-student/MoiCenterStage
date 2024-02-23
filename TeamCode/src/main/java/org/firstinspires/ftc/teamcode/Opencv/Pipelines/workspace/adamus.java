package org.firstinspires.ftc.teamcode.Opencv.Pipelines.workspace;

        import org.firstinspires.ftc.robotcore.external.Telemetry;
        import org.opencv.core.Mat;
        import org.opencv.core.Point;
        import org.opencv.core.Rect;
        import org.opencv.core.Scalar;
        import org.opencv.imgproc.Imgproc;
        import org.openftc.easyopencv.OpenCvPipeline;

        import java.util.ArrayList;

public class adamus extends OpenCvPipeline {
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

    public adamus() {
        frameList = new ArrayList<>();
    }

    public adamus(Telemetry t) {
        telemetry = t;
    }

    @Override
    public Mat processFrame(Mat input) {

        Imgproc.rectangle(input, new Point(0, 0),
                new Point(input.width(),140),
                new Scalar(128, 128, 128), -1);

        Imgproc.rectangle(input, new Point(input.width(), input.height()),
                new Point(0,170),
                new Scalar(128, 128, 128), -1);
        // mat turns into HSV value
//        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        if (mat.empty()) {
            return input;
        }

        Scalar lowHSV = new Scalar(LowH, LowS, LowV);
        Scalar higHSV = new Scalar(HighH, HighS, HighV);

        return mat;
    }

    public byte getData() {
        return (byte)(spikeZone+1);
    }
}
