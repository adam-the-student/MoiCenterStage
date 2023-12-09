package org.firstinspires.ftc.teamcode.OpenCV.Workspasce;

import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

public abstract class TransitionPipeline extends OpenCvPipeline {
    public abstract Mat processFrame(Mat input);

    public abstract byte getZone();
}
