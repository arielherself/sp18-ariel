package byog.util;

import java.lang.reflect.Array;

public abstract class MirrorCompatible<T> {
    public final T[][] mirrored(Class<T> c) {
        var target = getData().clone();
        if (target.length == 0) {
            return target;
        }
        final int m = target.length;
        final int n = target[0].length;
        for (int i = 0; i < (m + 1) / 2; ++i) {
            var temp = target[i];
            target[i] = target[m - i - 1];
            target[m - i - 1] = temp;
        }
        var result = (T[][]) Array.newInstance(c, n, m);
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                result[j][i] = target[i][j];
            }
        }

        return result;
    }

    public abstract T[][] getData();
}
