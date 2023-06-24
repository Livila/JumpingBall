public class Ball {

    private double x, y, width, height, velocity, maxJumpingHeight;
    private boolean isJumping, isMovingLeft, isMovingRight;

    public Ball(double x, double y, int width, int height, double velocity, int maxJumpingHeight) {
        this.x = x;
        this.y = y;

        if (width > 0) this.width = width;
        else this.width = 50;

        if (height > 0) this.height = height;
        else this.height = 50;

        this.velocity = velocity;
        this.isJumping = false;
        this.maxJumpingHeight = maxJumpingHeight;
    }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }
    public void setVelocity(double speed) { this.velocity = speed; }
    public void setMaxJumpingHeight(double maxJumpingHeight) { this.maxJumpingHeight = maxJumpingHeight; }
    public void setIsJumping(boolean isJumping) { this.isJumping = isJumping; }
    public void setIsMovingLeft(boolean isMovingLeft) { this.isMovingLeft = isMovingLeft; }
    public void setIsMovingRight(boolean isMovingRight) { this.isMovingRight = isMovingRight; }

    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public double getWidth() { return this.width; }
    public double getHeight() { return this.height; }
    public double getVelocity() { return this.velocity; }
    public double getMaxJumpingHeight() { return this.maxJumpingHeight; }
    public boolean getIsJumping() { return this.isJumping; }
    public boolean getIsMovingLeft() { return this.isMovingLeft; }
    public boolean getIsMovingRight() { return this.isMovingRight; }
}
