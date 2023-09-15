import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class SimpleRayTracer {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final Vector3 LIGHT = new Vector3(0, 0, -10);

    public static void main(String[] args) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Sphere sphere = new Sphere(new Vector3(0, 0, -5), 2, Color.RED);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Ray ray = new Ray(new Vector3(x - WIDTH / 2, HEIGHT / 2 - y, 0), new Vector3(0, 0, -1));
                Color color = trace(ray, sphere);
                image.setRGB(x, y, color.getRGB());
            }
        }

        try {
            ImageIO.write(image, "png", new File("output.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Color trace(Ray ray, Sphere sphere) {
        Double t = sphere.intersect(ray);
        if (t == null) return Color.BLACK;

        Vector3 point = ray.origin.add(ray.direction.scale(t));
        Vector3 normal = point.subtract(sphere.center).normalize();
        Vector3 lightDir = LIGHT.subtract(point).normalize();

        double dot = Math.max(0, normal.dot(lightDir));
        return new Color((int)(sphere.color.getRed() * dot), (int)(sphere.color.getGreen() * dot), (int)(sphere.color.getBlue() * dot));
    }

    static class Ray {
        Vector3 origin, direction;

        public Ray(Vector3 origin, Vector3 direction) {
            this.origin = origin;
            this.direction = direction;
        }
    }

    static class Sphere {
        Vector3 center;
        double radius;
        Color color;

        public Sphere(Vector3 center, double radius, Color color) {
            this.center = center;
            this.radius = radius;
            this.color = color;
        }

        public Double intersect(Ray ray) {
            Vector3 l = center.subtract(ray.origin);
            double tca = l.dot(ray.direction);
            if (tca < 0) return null;
            double d2 = l.dot(l) - tca * tca;
            if (d2 > radius * radius) return null;
            double thc = Math.sqrt(radius * radius - d2);
            return tca - thc;
        }
    }

    static class Vector3 {
        double x, y, z;

        public Vector3(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vector3 add(Vector3 v) {
            return new Vector3(x + v.x, y + v.y, z + v.z);
        }

        public Vector3 subtract(Vector3 v) {
            return new Vector3(x - v.x, y - v.y, z - v.z);
        }

        public Vector3 scale(double s) {
            return new Vector3(x * s, y * s, z * s);
        }

        public double dot(Vector3 v) {
            return x * v.x + y * v.y + z * v.z;
        }

        public double magnitude() {
            return Math.sqrt(x * x + y * y + z * z);
        }

        public Vector3 normalize() {
            double mag = magnitude();
            return new Vector3(x / mag, y / mag, z / mag);
        }
    }
}
