package hw3.hash;

import java.util.HashMap;
import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        final int N = oomages.size();
        int[] stat = new int[M];
        for (Oomage oomage : oomages) {
            ++stat[(oomage.hashCode() & 0x7FFFFFFF) % M];
        }
        for (int i = 0; i < M; ++i) {
            if (stat[i] < N / 50 || stat[i] > N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
