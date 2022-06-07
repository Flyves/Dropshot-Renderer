package rlbotexample.app.physics.game.states;

import rlbotexample.dropshot_tiles.DropshotTileLocations;
import util.math.vector.Vector3;
import util.network.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FrameData {
    public static List<Integer> nextFrame = new ArrayList<>();
    public static List<Integer> currentFrame = new ArrayList<>();
    public static List<Integer> previousFrame;
    public static boolean isQueryingNextFrame = true;

    public static List<Integer> computeTileIndexesToBreak(String data) {
        final List<Integer> tileIndexes = new ArrayList<>();
        final List<String> rows = Arrays.stream(data.split("2")).collect(Collectors.toList());

        if(rows.isEmpty()) return tileIndexes;
        if(rows.get(0).length() == 0) return tileIndexes;

        for(int i = 0; i < DropshotTileLocations.locations.size(); i++) {
            int width = rows.get(i).length();
            int height = rows.size();
            double incrementsX = (DropshotTileLocations.MAX_POSITION_X - DropshotTileLocations.MIN_POSITION_X + DropshotTileLocations.AVG_TILE_DIAMETER)/width;
            double incrementsY = (DropshotTileLocations.MAX_POSITION_Y - DropshotTileLocations.MIN_POSITION_Y + DropshotTileLocations.AVG_TILE_DIAMETER)/height;

            final Vector3 position = DropshotTileLocations.locations.get(i)
                    .scaled(-1, -1, 1)
                    .minus(new Vector3(
                            DropshotTileLocations.MIN_POSITION_X,
                            DropshotTileLocations.MIN_POSITION_Y,
                            0));
            int startPosX = (int) (position.x/incrementsX);
            int startPosY = (int) (position.y/incrementsY);
            int endPosX = (int) ((position.x+DropshotTileLocations.AVG_TILE_DIAMETER)/incrementsX);
            int endPosY = (int) ((position.y+DropshotTileLocations.AVG_TILE_DIAMETER)/incrementsY);
            final String flatFrameWindow = getFrameWindow(rows, startPosX, startPosY, endPosX, endPosY);
            if(computePolarity(flatFrameWindow)) {
                tileIndexes.add(i);
            }
        }

        return tileIndexes;
    }

    private static boolean computePolarity(String flatFrameWindow) {
        return flatFrameWindow.chars().filter(c -> c == '1').count()
                - flatFrameWindow.chars().filter(c -> c == '0').count() > 0;
    }

    private static String getFrameWindow(List<String> rows, int startPosX, int startPosY, int endPosX, int endPosY) {
        final StringBuilder rowWindows = new StringBuilder();

        for(int i = startPosY; i < rows.size() && i < endPosY; i++) {
            if(endPosX > rows.get(i).length()) {
                rowWindows.append(rows.get(i), startPosX, rows.get(i).length());
            }
            else {
                rowWindows.append(rows.get(i), startPosX, endPosX);
            }
        }

        return rowWindows.toString();
    }
}
