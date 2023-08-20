package byog.util;

public class ElementBase<T> {
    public int height, width;
    public int positionX, positionY;
    public final T[][] data;

    public ElementBase(int height, int width, int positionX, int positionY) {
        this.height = height;
        this.width = width;
        this.positionX = positionX;
        this.positionY = positionY;
        data = (T[][]) new Object[height][width];
    }
}
