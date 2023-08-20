package byog.util;

public abstract class MirrorCompatible<T> {
    public final T[][] mirrored() {
        var target = getData();
        if (target.length == 0) {
            return target.clone();
        }
        final int m = target.length;
        final int n = target[0].length;
        var result = (T[][]) new Object[n][m];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                result[j][i] = target[i][j];
            }
        }
        return result;
    }

    public abstract T[][] getData();
}
