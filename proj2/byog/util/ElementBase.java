package byog.util;

import java.lang.reflect.Array;

public class ElementBase<T> {
    public int height, width;
    public int positionX, positionY;
    public final T[][] data;

    public ElementBase(Class<T> c, int height, int width, int positionX, int positionY) {
        this.height = height;
        this.width = width;
        this.positionX = positionX;
        this.positionY = positionY;
        data = (T[][]) Array.newInstance(c, height, width);
    }
}
