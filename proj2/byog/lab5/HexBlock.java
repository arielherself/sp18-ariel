package byog.lab5;

import byog.TileEngine.TETile;

public class HexBlock {
    public TETile[][] data; // unsafe
    public final int width, height, size;

    public HexBlock(int size) {
        assert size != 0;
        this.size = size;
        width = size * 3 - 2;
        height = 2 * size;
        data = new TETile[height][width];
    }

    public void fill(TETile target) {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size + 2 * i; ++j) {
                data[i][size - 1 - i + j] = target;
            }
        }
        for (int i = size; i < height; ++i) {
            for (int j = 0; j < size * 3 - 2 - 2 * i + height; ++j) {
                data[i][i - size + j] = target;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                sb.append(data[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        HexBlock a = new HexBlock(4);
        // a.fill(3);
        System.out.print(a);
        System.out.println();
    }
}
