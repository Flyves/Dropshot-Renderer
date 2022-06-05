package util.renderers;
import rlbot.render.Renderer;
import rlbotexample.SampleBot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class RenderTasks {
    private static final int AMOUNT_OF_RENDERERS_TO_CREATE = 1;
    private static final List<Consumer<Renderer>> tasks = new ArrayList<>();
    private static final List<IndexedRenderer> renderers = new ArrayList<>();

    public static void init() {
        while(renderers.size() < AMOUNT_OF_RENDERERS_TO_CREATE) {
            renderers.add(SampleBot.getNewIndexedRenderer());
        }
    }

    public static void append(final Consumer<Renderer> renderingTask) {
        tasks.add(renderingTask);
    }

    public static void render() {
        final AtomicInteger i = new AtomicInteger(0);
        renderers.get(0).startPacket();
        tasks.forEach(task -> {
            final IndexedRenderer renderer = renderers.get(i.get());
            task.accept(renderer);

            i.incrementAndGet();
            if(i.get() >= AMOUNT_OF_RENDERERS_TO_CREATE) {
                i.set(0);
            }
        });
        renderers.get(0).finishAndSend();
    }

    public static void clearTaskBuffer() {
        tasks.clear();
    }

    public static void close() {
        for (IndexedRenderer renderer : renderers) {
            renderer.eraseFromScreen();
        }
    }
}
