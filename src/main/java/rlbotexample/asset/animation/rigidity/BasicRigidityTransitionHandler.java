package rlbotexample.asset.animation.rigidity;

import rlbotexample.asset.animation.discrete_player.DiscreteCarGroupAnimator;

public class BasicRigidityTransitionHandler {

    public static void handle(DiscreteCarGroupAnimator animator) {
        handle(animator, 0.2);
    }

    public static void handle(DiscreteCarGroupAnimator animator, double initialRigidity) {
        animator.carsRigidity = initialRigidity + animator.currentFrameIndex()/180.0;
        if(animator.carsRigidity > 1) {
            animator.carsRigidity = 1;
        }
    }

    public static void handle(DiscreteCarGroupAnimator animator, double initialRigidity, double duration) {
        animator.carsRigidity = initialRigidity + animator.currentFrameIndex()/duration;
        if(animator.carsRigidity > 1) {
            animator.carsRigidity = 1;
        }
    }

    public static void handle(DiscreteCarGroupAnimator animator, double initialRigidity, double duration, int amountOfFramesSpent) {
        animator.carsRigidity = initialRigidity + amountOfFramesSpent/duration;
        if(animator.carsRigidity > 1) {
            animator.carsRigidity = 1;
        }
    }
}
