public class NBody {
    public static double readRadius(String fileName) {
        In in = new In(fileName);
        in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String fileName) {
        In in = new In(fileName);
        int n = in.readInt();
        in.readDouble();

        Planet[] result = new Planet[n];
        for (int i = 0; i < n; ++i) {
            result[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(),
                    in.readDouble(), in.readDouble(), in.readString());
        }
        return result;
    }

    public static void main(String[] args) {
        final double T = Double.parseDouble(args[0]), dt = Double.parseDouble(args[1]);
        final String filename = args[2];
        final Planet[] planets = readPlanets(filename);
        final double radius = readRadius(filename);
        final int n = planets.length;

        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-radius, radius);
        StdDraw.clear();

        StdDraw.picture(0, 0, "images/starfield.jpg");
        for (Planet p: planets) {
            p.draw();
        }
        StdDraw.show();

        var xForces = new double[n];
        var yForces = new double[n];
        double t = 0;
        int j;
        while(t < T) {
            StdDraw.picture(0, 0, "images/starfield.jpg");
            for (j = 0; j < n; ++j) {
                xForces[j] = planets[j].calcNetForceExertedByX(planets);
                yForces[j] = planets[j].calcNetForceExertedByY(planets);
            }
            for (j = 0; j < n; ++j) {
                planets[j].update(dt, xForces[j], yForces[j]);
                planets[j].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            t += dt;
        }

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (Planet planet : planets) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planet.xxPos, planet.yyPos, planet.xxVel,
                    planet.yyVel, planet.mass, planet.imgFileName);
        }
    }
}
