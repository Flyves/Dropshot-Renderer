package rlbotexample.asset.animation.animation;

import util.data_structure.builder.Builder;
import util.math.vector.OrientedPosition;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class AnimationProfileBuilder implements Builder<AnimationProfile> {
    private static final Double DEFAULT_PLAYBACK_SPEED = 60d;   // fps
    private static final Function<Double, Double> DEFAULT_INTERPOLATION_FUNCTION = (t) -> t;

    private Animation animation;
    private Supplier<Double> playbackSpeed;
    private Function<Double, Double> inBetweenFramesInterpolationFunction;
    private Supplier<OrientedPosition> animationOffset;
    private Supplier<Boolean> finishingSupplier;
    private Boolean isLooping;
    private Function<Integer, Double> rigidityFunction;

    private Map<Integer, Runnable> frameEvents;

    public AnimationProfileBuilder() {
        this.playbackSpeed = () -> DEFAULT_PLAYBACK_SPEED;
        this.inBetweenFramesInterpolationFunction = DEFAULT_INTERPOLATION_FUNCTION;
        this.animationOffset = OrientedPosition::new;
        this.finishingSupplier = () -> false;
        this.isLooping = false;
        this.rigidityFunction = (i) -> 1d;
        this.frameEvents = new HashMap<>();
    }

    public AnimationProfileBuilder withAnimation(final Animation animation) {
        this.animation = animation;
        return this;
    }

    public AnimationProfileBuilder withPlaybackSpeed(final Supplier<Double> playbackSpeed) {
        this.playbackSpeed = playbackSpeed;
        return this;
    }

    public AnimationProfileBuilder withInterpolation(final Function<Double, Double> inBetweenFramesInterpolationFunction) {
        this.inBetweenFramesInterpolationFunction = inBetweenFramesInterpolationFunction;
        return this;
    }

    public AnimationProfileBuilder withAnimationOffset(final Supplier<OrientedPosition> orientedPosition) {
        this.animationOffset = orientedPosition;
        return this;
    }

    public AnimationProfileBuilder withFinishingFunction(final Supplier<Boolean> finishingSupplier) {
        this.finishingSupplier = finishingSupplier;
        return this;
    }

    public AnimationProfileBuilder withLooping(final Boolean isLooping) {
        this.isLooping = isLooping;
        return this;
    }

    public AnimationProfileBuilder withRigidity(final Function<Integer, Double> rigidityFunction) {
        this.rigidityFunction = (i) -> {
            final double r = rigidityFunction.apply(i);
            if(r > 1) {
                return 1d;
            }
            if(r < 0) {
                return 0d;
            }
            return r;
        };
        return this;
    }

    public AnimationProfileBuilder withFrameEvent(final int frameIndex, final Runnable event) {
        frameEvents.put(frameIndex, event);
        return this;
    }

    @Override
    public AnimationProfile build() {
        return new AnimationProfile(
                animation,
                playbackSpeed,
                inBetweenFramesInterpolationFunction,
                animationOffset,
                finishingSupplier,
                isLooping,
                rigidityFunction,
                frameEvents);
    }
}