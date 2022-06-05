package rlbotexample.asset.animation.animation;

import rlbotexample.asset.animation.car_group.CarGroup;
import rlbotexample.asset.animation.car_group.IndexedCarGroup;

import java.io.Serializable;
import java.util.List;

public class Animation implements Serializable {
    public final List<IndexedCarGroup> frames;

    public Animation(List<IndexedCarGroup> frames) {
        this.frames = frames;
    }

    public CarGroup queryFrame(int frameIndex) {
        if(frameIndex < frames.size()
                && frameIndex >= 0) {
            return frames.get(frameIndex).carGroup;
        }

        else if(frameIndex < 0) {
            return frames.get(0).carGroup;
        }
        return frames.get(frames.size()-1).carGroup;
    }
}
