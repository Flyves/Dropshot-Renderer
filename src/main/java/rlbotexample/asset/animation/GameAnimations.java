package rlbotexample.asset.animation;

import rlbotexample.asset.animation.animation.Animation;
import util.files.ObjectSerializer;

import java.util.ArrayList;

public class GameAnimations {

    private static final String BASE_FILE_PATH = "src\\main\\resources\\car animations\\";

    public static boolean areReady = false;
    public static boolean areLoading = false;
    public static int loadingProgress = 0;

    public static final Animation DEFAULT_EMPTY_ANIMATION = new Animation(new ArrayList<>());
    public static Animation boss_transformation_0_To_1;
    public static Animation boss_transformation_1_To_2;
    public static Animation boss_dash_attack;
    public static Animation boss_electric_ball_firing;
    public static Animation quadrupedal_idle_2;
    public static Animation quadrupedal_beyblade;
    public static Animation lamp_glados_base;
    public static Animation lamp_glados_head;
    public static Animation boss_idle_idle;

    public static void loadAnimations() {
        boss_transformation_0_To_1 = ObjectSerializer.load(BASE_FILE_PATH + "boss_transformation_0_To_1.sob");
        loadingProgress++;
        boss_transformation_1_To_2 = ObjectSerializer.load(BASE_FILE_PATH + "boss_transformation_1_To_2.sob");
        loadingProgress++;
        boss_dash_attack = ObjectSerializer.load(BASE_FILE_PATH + "boss_dash_attack.sob");
        loadingProgress++;
        boss_electric_ball_firing = ObjectSerializer.load(BASE_FILE_PATH + "boss_electric_ball_firing.sob");
        loadingProgress++;
        quadrupedal_idle_2 = ObjectSerializer.load(BASE_FILE_PATH + "quadrupedal_idle_2.sob");
        loadingProgress++;
        quadrupedal_beyblade = ObjectSerializer.load(BASE_FILE_PATH + "quadrupedal_beyblade.sob");
        loadingProgress++;
        lamp_glados_base = ObjectSerializer.load(BASE_FILE_PATH + "boss_lamp_glados_base_form_rigged.sob");
        loadingProgress++;
        lamp_glados_head = ObjectSerializer.load(BASE_FILE_PATH + "boss_lamp_glados_head_form_rigged.sob");
        loadingProgress++;
        boss_idle_idle = ObjectSerializer.load(BASE_FILE_PATH + "boss_idle.sob");
        areReady = true;
        areLoading = false;
    }
}
