package org.firstinspires.ftc.teamcode.Opencv.Pipelines;

import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

public abstract class TransitionPipeline extends OpenCvPipeline {
    @Override
    public abstract Mat processFrame(Mat input);

    public abstract byte getData();
}
