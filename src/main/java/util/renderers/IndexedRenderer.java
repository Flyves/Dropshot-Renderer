package util.renderers;

import com.google.flatbuffers.FlatBufferBuilder;
import rlbot.cppinterop.RLBotDll;
import rlbot.render.RenderPacket;
import rlbot.render.Renderer;

/**
 * This renderer must be constructed with a specific unique name. You can draw, clear, etc. independently of
 * other renderers.
 */
public class IndexedRenderer extends Renderer {
    public final int index;

    private static Integer amountOfRenderersCreated = 0;

    public IndexedRenderer(final Integer offset) {
        super(Integer.valueOf(amountOfRenderersCreated + offset).hashCode());
        this.index = amountOfRenderersCreated + offset;
        IndexedRenderer.amountOfRenderersCreated++;
    }

    public void startPacket() {
        builder = new FlatBufferBuilder(0);
    }

    public RenderPacket finishPacket() {
        return doFinishPacket();
    }

    public void finishAndSend() {
        RLBotDll.sendRenderPacket(finishPacket());
    }
}
