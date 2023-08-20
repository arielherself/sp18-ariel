import synthesizer.GuitarString;

public class GuitarHero {
    private static final double CONCERT_A = 440.0;
    private static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static final int KEYS_COUNT = KEYBOARD.length();
    private final GuitarString[] strings = new GuitarString[KEYS_COUNT];

    public GuitarHero() {
        for (int i = 0; i < KEYS_COUNT; ++i) {
            strings[i] = new GuitarString(CONCERT_A * Math.pow(2, (double) (i - 24) / 12));
        }
    }

    public void pluck(int index) {
        strings[index].pluck();
    }

    public void ticAll() {
        for (int i = 0; i < KEYS_COUNT; ++i) {
            strings[i].tic();
        }
    }

    public double sampleAll() {
        double result = 0;
        for (int i = 0; i < KEYS_COUNT; ++i) {
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
                    guitar.pluck(KEYBOARD.indexOf(key));
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
