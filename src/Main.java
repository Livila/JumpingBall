import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

class Launcher {
    public static void main(String[] args) {
        Game game = new Game(
                1600,
                900,
                "Jumping Ball!",
                2,
                100);
        game.start();
    }
}

class Display {

    public JFrame frame;
    public Canvas canvas;
    public int width, height;

    public Display(int width, int height, String title) {
        this.width = width;
        this.height = height;

        frame = new JFrame();
        frame.setTitle(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));

        frame.add(canvas);
        frame.pack();
    }
}

class Game implements Runnable {

    public int width, height, fps = 60;
    private String title;
    public Display display;

    public Thread thread;
    public boolean running = false;

    public Game(int width, int height, String title, int velocity, int maxJumpingHeight) {
        this.width = width;
        this.height = height;
        this.title = title;

        // Initialize the ball class.
        int ballSizeX = 50, ballSizeY = 50;
        this.ball = new Ball(
                width / 2f - ballSizeX / 2f,
                0,
                ballSizeX,
                ballSizeY,
                velocity,
                maxJumpingHeight);
    }

    /*
     * Initialize the game.
     */
    private void init() {

        display = new Display(width, height, title);

        // Add key listeners.
        display.canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    ball.setIsMovingLeft(true);

                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    ball.setIsMovingRight(true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    ball.setIsMovingLeft(false);

                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    ball.setIsMovingRight(false);
            }
        });
    }

    int gameGroundHeight = 100;
    Ball ball;

    /*
     * Update the game variables.
     */
    private void tick() {
        // Update the height of the ball.
        if (ball.getIsJumping()) {
            if (ball.getY() < ball.getMaxJumpingHeight() / 2d)
                ball.setY(ball.getY() + 1);
            else
                ball.setY(ball.getY() + 0.5d);
        } else {
            ball.setY(ball.getY() - 1);
        }

        // Set whether the ball should be jumping or not.
        if (ball.getIsJumping() && ball.getY() >= ball.getMaxJumpingHeight())
            ball.setIsJumping(false);
        else if (!ball.getIsJumping() && ball.getY() <= 0)
            ball.setIsJumping(true);

        // Move the ball left.
        if (ball.getIsMovingLeft())
            ball.setX(ball.getX() - ball.getVelocity());

        // Move the ball right.
        if (ball.getIsMovingRight())
            ball.setX(ball.getX() + ball.getVelocity());
    }

    /*
     * Update and render the screen.
     */
    private void render() {

        // Setting up the buffer strategy.
        BufferStrategy bs = display.canvas.getBufferStrategy();
        if (bs == null) {
            display.canvas.createBufferStrategy(3);
            return;
        }

        // Clear up the screen.
        Graphics g = bs.getDrawGraphics();
        g.clearRect(0, 0, width, height);

        // Prepare graphics.
        g.drawLine(0, height - gameGroundHeight, width, height - gameGroundHeight);
        g.fillOval(((int)ball.getX()), height - gameGroundHeight - ((int)ball.getHeight()) - ((int)ball.getY()), ((int)ball.getWidth()), ((int)ball.getHeight()));

        // Make the graphics visible.
        bs.show();
        g.dispose();
    }

    /*
     * Initialize and loop the game.
     */
    public void run() {
        init();

        double tickDuration = 1000000000.0 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / tickDuration;
            lastTime = now;

            if (delta >= 1) {
                tick();
                render();
                delta--;
            }
        }

        stop();
    }

    /*
     * Start the thread for the application.
     */
    public synchronized void start() {

        if (running) return;
        running = true;

        thread = new Thread(this);
        thread.start();
    }

    /*
     * Stop all threads.
     */
    public synchronized void stop() {

        if (!running) return;
        running = false;

        try {
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}