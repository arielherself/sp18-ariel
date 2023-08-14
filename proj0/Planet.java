public class Planet {
    private static final double G = 6.67e-11;
    public double xxPos, yyPos, xxVel, yyVel, mass;
    String imgFileName;
    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }
    public Planet(Planet p) {
        this(p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
    }

    public double calcDistance(Planet p1) {
        return Math.sqrt((xxPos - p1.xxPos) * (xxPos - p1.xxPos) + (yyPos - p1.yyPos) * (yyPos - p1.yyPos));
    }

    public double calcForceExertedBy(Planet p1) {
        return G * mass * p1.mass / (calcDistance(p1) * calcDistance(p1));
    }

    public double calcForceExertedByX(Planet p1) {
        if (this.equals(p1)) {
            return 0;
        } else {
            return calcForceExertedBy(p1) * (p1.xxPos - xxPos) / calcDistance(p1);
        }
    }

    public double calcForceExertedByY(Planet p1) {
        if (this.equals(p1)) {
            return 0;
        } else {
            return calcForceExertedBy(p1) * (p1.yyPos - yyPos) / calcDistance(p1);
        }
    }

    public double calcNetForceExertedByX(Planet[] ps) {
        double result = 0;
        for (Planet p: ps) {
            result += calcForceExertedByX(p);
        }
        return result;
    }

    public double calcNetForceExertedByY(Planet[] ps) {
        double result = 0;
        for (Planet p: ps) {
            result += calcForceExertedByY(p);
        }
        return result;
    }

    public void update(double dt, double xxForce, double yyForce) {
        double xxAcc = xxForce / mass, yyAcc = yyForce / mass;
        xxVel += xxAcc * dt;
        yyVel += yyAcc * dt;
        xxPos += xxVel * dt;
        yyPos += yyVel * dt;
    }

    public void draw() {
        StdDraw.picture(xxPos, yyPos, imgFileName);
    }
}
