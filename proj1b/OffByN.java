public class OffByN {
    private int N;
    public OffByN(int N) {
        this.N = N;
    }

    public boolean equalChars(char x, char y) {
        return (x - y) == N || (x - y) == -N;
    }
}
