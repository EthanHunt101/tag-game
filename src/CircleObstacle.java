public class CircleObstacle extends Obstacle{
    Point center;
    double radius;
    public CircleObstacle(Point center, double radius) {

        this.center = center;
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }
}
