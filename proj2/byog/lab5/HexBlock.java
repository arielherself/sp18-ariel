package byog.lab5;

public class HexBlock {
    private int[][] data;
    private final int width, size;

    public HexBlock(int size) {
        assert size != 0;
        this.size = size;
        width = size * 3 - 2;
        data = new int[size * 2][width];
    }

    public void fill(int target) {
        assert target != 0;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size + 2 * i; ++j) {
                data[i][size - 1 - i + j] = target;
            }
        }
        for (int i = size; i < size * 2; ++i) {
            for (int j = 0; j < size * 3 - 2 - 2 * i + 2 * size; ++j) {
                data[i][i - size + j] = target;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size * 2; ++i) {
            for (int j = 0; j < width; ++j) {
                sb.append(data[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        HexBlock a = new HexBlock(5);
        a.fill(3);
        System.out.print(a);
        System.out.println();
    }
}
