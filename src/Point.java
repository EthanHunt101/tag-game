import java.lang.Math;

public class Point {
    /**  The x-coordinate of the Point */
    private double xCoord = 0.0;
    /**  The y-coordinate of the Point */
    private double yCoord = 0.0;

    /**
     * Constructor to initialize the Point with specific x and y coordinates.
     * @param xCoord The x-coordinate of the Point
     * @param yCoord The y-coordinate of the Point
     */
    public Point(double xCoord, double yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    /**
     * Returns the x-coordinate of this Point.
     * @return The x-coordinate of the Point
     */
    public double getX() {
        return this.xCoord;
    }

    /**
     * Returns the y-coordinate of this Point.
     * @return The y-coordinate of the Point
     */
    public double getY() {
        return this.yCoord;
    }

    /**
     * Sets the x-coordinate of this Point.
     * @param xCoord The new x-coordinate to set
     */
    public void setX(double xCoord) {
        this.xCoord = xCoord;
    }

    /**
     * Sets the y-coordinate of this Point.
     * @param yCoord The new y-coordinate to set
     */
    public void setY(double yCoord) {
        this.yCoord = yCoord;
    }

    public void rotateAbout(Point p, double angle) {
        // Calculate the difference between this Point's coordinates and Point p
        double subX = this.getX() - p.getX();
        double subY = this.getY() - p.getY();

        // Calculate the rotated x and y coordinates
        double rotatedX = (subX * Math.cos(angle)) - (subY * Math.sin(angle));
        double rotatedY = (subX * Math.sin(angle)) + (subY * Math.cos(angle));

        // Update this Point's coordinates to the new rotated position
        this.setX(rotatedX + p.getX());
        this.setY(rotatedY + p.getY());
    }
}