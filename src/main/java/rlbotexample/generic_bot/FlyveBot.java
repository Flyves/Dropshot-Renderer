package rlbotexample.generic_bot;

import rlbot.render.Renderer;
import rlbotexample.dynamic_objects.DataPacket;
import util.game_constants.RlConstants;
import util.renderers.RenderTasks;

import java.awt.*;

public abstract class FlyveBot extends BotBehaviour {

    @Override
    public void updateGui(DataPacket input, double currentFps, double averageFps, long botExecutionTime) {
        int offset = input.botIndex *130;
        displayFpsCounter(currentFps, offset);
        displayAvgFps(averageFps, offset);
        displayMsPerFrame(botExecutionTime, offset);
    }

    private void displayFpsCounter(double fps, int offset) {
        if(Math.abs(fps - RlConstants.BOT_REFRESH_RATE) < RlConstants.BOT_REFRESH_RATE/10) {
            RenderTasks.append(r -> r.drawString2d(String.format("%.4f", fps), Color.green, new Point(10, 10 + offset), 2, 2));
        }
        else if(Math.abs(fps - RlConstants.BOT_REFRESH_RATE) < RlConstants.BOT_REFRESH_RATE/6) {
            RenderTasks.append(r -> r.drawString2d(String.format("%.4f", fps), Color.yellow, new Point(10, 10 + offset), 2, 2));
        }
        else {
            RenderTasks.append(r -> r.drawString2d(String.format("%.4f", fps), Color.red, new Point(10, 10 + offset), 2, 2));
        }
    }

    private void displayAvgFps(double fps, int offset) {
        if(Math.abs(fps - RlConstants.BOT_REFRESH_RATE) < RlConstants.BOT_REFRESH_RATE/10) {
            RenderTasks.append(r -> r.drawString2d(String.format("%.4f", fps) + "", Color.green, new Point(10, 50 + offset), 2, 2));
        }
        else if(Math.abs(fps - RlConstants.BOT_REFRESH_RATE) < RlConstants.BOT_REFRESH_RATE/6) {
            RenderTasks.append(r -> r.drawString2d(String.format("%.4f", fps) + "", Color.yellow, new Point(10, 50 + offset), 2, 2));
        }
        else {
            RenderTasks.append(r -> r.drawString2d(String.format("%.4f", fps) + "", Color.red, new Point(10, 50 + offset), 2, 2));
        }
    }

    private void displayMsPerFrame(long msPerFrame, int offset) {
        if(msPerFrame < 500/RlConstants.BOT_REFRESH_RATE) {
            RenderTasks.append(r -> r.drawString2d(msPerFrame + "", Color.green, new Point(10, 90 + offset), 2, 2));
        }
        else if(msPerFrame < 1000/RlConstants.BOT_REFRESH_RATE) {
            RenderTasks.append(r -> r.drawString2d(msPerFrame + "", Color.yellow, new Point(10, 90 + offset), 2, 2));
        }
        else {
            RenderTasks.append(r -> r.drawString2d(msPerFrame + "", Color.red, new Point(10, 90), 2, 2));
        }
    }
}
