public class RectangleObstacle extends Obstacle{
    private Point topRight;
    private Point bottomLeft;
    private Point bottomRight;
    private Point topLeft;
    private double width;
    private double height;

    public RectangleObstacle(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;

        this.topRight = new Point(bottomRight.getX(), topLeft.getY());
        this.bottomLeft = new Point(topLeft.getX(), bottomRight.getY());
        if (topRight.getX() - topLeft.getX() != bottomRight.getX() - bottomLeft.getX()) {
            throw new UnsupportedOperationException();
        }
        this.width = topRight.getX() - topLeft.getX();
        this.height = bottomRight.getY() - topRight.getY();
    }

    public Point getTopRight() {
        return topRight;
    }
    public Point getBottomLeft() {
        return bottomLeft;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }



}