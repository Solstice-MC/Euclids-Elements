package org.solstice.euclidsElements.util;

import com.google.common.base.MoreObjects;
import com.mojang.serialization.Codec;
import net.minecraft.util.Util;

public class Vec2i {

    public static final Codec<Vec2i> CODEC = Codec.INT_STREAM.flatXmap(
            stream -> Util.decodeFixedLengthArray(stream, 2).map(Vec2i::new),
            null
    );

    public static Vec2i ZERO = new Vec2i(0, 0);

    public int x;
    public int y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i(int... xy) {
        this(xy[0], xy[1]);
    }


    public Vec2i add(int x, int y) {
        return new Vec2i(this.x + x, this.y + y);
    }

    public Vec2i add(Vec2i vec) {
        return this.add(vec.x, vec.y);
    }

    public Vec2i subtract(Vec2i vec) {
        return this.add(-vec.x, -vec.y);
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", x).add("y", y).toString();
    }

    public String toShortString() {
        return x + ", " + y;
    }

}
