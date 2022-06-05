package rlbotexample.app.physics.game.game_option;

public enum DifficultyType {
    ROCKET_SLEDGE,
    TRIVIAL,
    EASY,
    MEDIUM,
    HARD,
    EXPERT,
    IMPOSSIBLE,
    WTF;

    public static int toId(final DifficultyType difficultyType) {
        switch (difficultyType) {
            case ROCKET_SLEDGE: return 0;
            case EASY: return 1;
            case MEDIUM: return 2;
            case HARD: return 3;
            case EXPERT: return 4;
            case IMPOSSIBLE: return 5;
            case WTF: return 6;
        }
        throw new RuntimeException("No game difficulty selected!");
    }
}
