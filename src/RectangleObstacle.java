public class RectangleObstacle extends Obstacle{
    Point topRight;
    Point bottomLeft;

    public RectangleObstacle(Point bottomLeft, Point topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
    }

    public Point getTopRight() {
        return topRight;
    }
    public Point getBottomLeft() {
        return bottomLeft;
    }


}