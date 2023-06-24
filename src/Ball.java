public class Ball {

    private double x, y, width, height, speed;
    private boolean isJumping;

    public Ball(double x, double y, double width, double height, double speed) {
        this.x = x;
        this.y = y;

        if (width > 0) this.width = width;
        else this.width = 50;

        if (height > 0) this.height = height;
        else this.height = 50;

        this.speed = speed;
        this.isJumping = false;
    }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setIsJumping(boolean isJumping) { this.isJumping = isJumping; }

    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public double getWidth() { return this.width; }
    public double getHeight() { return this.height; }
    public double getVelocity() { return this.speed; }
    public boolean getIsJumping() { return this.isJumping; }
}
