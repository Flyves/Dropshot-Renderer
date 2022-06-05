package rlbotexample.asset.animation.car_group;

import java.io.Serializable;

public class IndexedCarGroup implements Serializable {

    public final CarGroup carGroup;
    public final int frameIndex;

    public IndexedCarGroup(int index) {
        this.carGroup = new CarGroup();
        this.frameIndex = index;
    }
}
