package rlbotexample.asset.animation.animation;

import rlbotexample.dynamic_objects.DataPacket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AnimationTasks {
    private static final List<AnimationPlayer> animationHandlers = new ArrayList<>();

    public static void append(final AnimationPlayer animationPlayer) {
        animationHandlers.add(animationPlayer);
    }

    public static void stateSetAnimations(final DataPacket input) {
        animationHandlers.forEach(animationPlayer -> animationPlayer.stateSet(input));
    }

    public static void clearFinishedTasks() {
        final List<AnimationPlayer> finishedAnimations = new ArrayList<>();
        animationHandlers.stream()
                .filter(AnimationPlayer::isFinished)
                .peek(finishedAnimations::add)
                .forEach(AnimationPlayer::close);
        animationHandlers.removeAll(finishedAnimations);
    }

    public static List<AnimationPlayer> getCurrentAnimations() {
        return animationHandlers;
    }
}