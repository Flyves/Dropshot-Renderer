package rlbotexample.asset.animation.animation;

import util.math.vector.OrientedPosition;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

class AnimationProfile {
    public final Animation animation;
    public final Supplier<Double> playbackSpeed;
    public final Function<Double, Double> inBetweenFramesInterpolationFunction;
    public final Supplier<OrientedPosition> animationOffset;
    public final Supplier<Boolean> finishingSupplier;
    public final Boolean isLooping;
    public final Function<Integer, Double> rigidityFunction;
    public final Map<Integer, Runnable> frameEvents;

    public AnimationProfile(
            final Animation animation,
            final Supplier<Double> playbackSpeed,
            final Function<Double, Double> inBetweenFramesInterpolationFunction,
            final Supplier<OrientedPosition> animationOffset,
            final Supplier<Boolean> finishingSupplier,
            final Boolean isLooping,
            final Function<Integer, Double> rigidityFunction,
            final Map<Integer, Runnable> frameEvents) {
        this.animation = animation;
        this.playbackSpeed = playbackSpeed;
        this.inBetweenFramesInterpolationFunction = inBetweenFramesInterpolationFunction;
        this.animationOffset = animationOffset;
        this.finishingSupplier = finishingSupplier;
        this.isLooping = isLooping;
        this.rigidityFunction = rigidityFunction;
        this.frameEvents = frameEvents;
    }
}