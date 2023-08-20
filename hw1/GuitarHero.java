import synthesizer.GuitarString;

public class GuitarHero {
    private static final double CONCERT_A = 440.0;
    private static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static final int keysCount = keyboard.length();
    private final GuitarString[] strings = new GuitarString[keysCount];

    public GuitarHero() {
        for (int i = 0; i < keysCount; ++i) {
            strings[i] = new GuitarString(CONCERT_A * Math.pow(2, (double) (i - 24) / 12));
        }
    }

    public void pluck(int index) {
        strings[index].pluck();
    }

    public void ticAll() {
        for (int i = 0; i < keysCount; ++i) {
            strings[i].tic();
        }
    }

    public double sampleAll() {
        double result = 0;
        for (int i = 0; i < keysCount; ++i) {
            result += strings[i].sample();
        }
        return result;
    }

    public static void main(String[] args) {
        GuitarHero guitar = new GuitarHero();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                try {
                    guitar.pluck(keyboard.indexOf(key));
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }

            double sample = guitar.sampleAll();

            StdAudio.play(sample);

            guitar.ticAll();
        }
    }
}
