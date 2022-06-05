package util.shapes;

import util.math.vector.Vector2;
import util.renderers.RenderTasks;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Rectangle2D {
    public final Vector2 upperLeft;
    public final Vector2 size;

    public Rectangle2D(final Vector2 upperLeft, final Vector2 size) {
        this.upperLeft = upperLeft;
        this.size = size;
    }

    public List<Rectangle2D> decomposeIntoSmallerRectangles(final double widthOverHeight) {
        if(area() <= 0) {
            throw new RuntimeException();
        }

        final ArrayList<Rectangle2D> solutions = new ArrayList<>();
        Rectangle2D remainder = this;

        double height;
        Rectangle2D solution;

        boolean hasReachedRightSide = false;
        boolean hasReachedDownwards = false;

        Vector2 newRemainderUpperLeft;
        Vector2 newReminderSize;

        while(!(hasReachedDownwards && hasReachedRightSide)) {
            height = remainder.size.y;
            if (height * widthOverHeight > remainder.size.x) {
                height = remainder.size.x / widthOverHeight;
            }
            solution = new Rectangle2D(remainder.upperLeft, new Vector2(height * widthOverHeight, height));
            solutions.add(solution);

            hasReachedRightSide = Math.abs(remainder.size.x - solution.size.x) < 1;
            hasReachedDownwards = Math.abs(remainder.size.y - solution.size.y) < 1;
            if (hasReachedRightSide) {
                newRemainderUpperLeft = remainder.upperLeft
                        .plus(new Vector2(0, solution.size.y));
            }
            else {
                newRemainderUpperLeft = remainder.upperLeft
                        .plus(new Vector2(solution.size.x, 0));
            }

            if (hasReachedRightSide) {
                newReminderSize = new Vector2(remainder.size.x, remainder.size.y - solution.size.y);
            } else {
                newReminderSize = new Vector2(remainder.size.x - solution.size.x, remainder.size.y);
            }
            remainder = new Rectangle2D(newRemainderUpperLeft, newReminderSize);
        }

        return solutions;
    }

    public void render(final Color color) {
        RenderTasks.append(r -> r.drawRectangle2d(color, upperLeft.toAwtPoint(), (int)size.x, (int)size.y, true));
    }

    public double area() {
        return size.x * size.y;
    }
}
