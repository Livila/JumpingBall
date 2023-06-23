import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

class Launcher {
    public static void main(String[] args) {
        Game game = new Game(1600, 900, "Jumping Ball!", 2);
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
    private BufferStrategy bs;
    private Graphics g;

    public Game(int width, int height, String title, int speed) {
        this.width = width;
        this.height = height;
        this.title = title;

        int ballSizeX = 50, ballSizeY = 0;
        this.ball = new Ball(width / 2 - ballSizeX / 2, 0, ballSizeX, ballSizeY, speed);
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
                //System.out.println("[INFO] Key typed: " + e.getKeyCode());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println("[INFO] Key pressed: " + e.getKeyCode());

                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    ballMoveLeft = true;

                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    ballMoveRight = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("[INFO] Key released: " + e.getKeyCode());

                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    ballMoveLeft = false;

                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    ballMoveRight = false;
            }
        });
    }

    // TODO: Variables should be in a class and not here.
    int gameBaseHeight = 100;
    Ball ball;
    boolean ballMoveLeft = false;
    boolean ballMoveRight = false;
    int jumpingHeight = 100;

    /*
     * Update the game variables.
     */
    private void tick() {

        System.out.println("[Tick] Location: " + ball.getX() + " :: " + ball.getY());

        if (ball.getIsJumping()) {
            // Just some simple testing before adding the real thing...
            if (ball.getY() < jumpingHeight / 2d) {
                {
                    ball.setY(ball.getY() + ball.getY());
                }
            } else {
                ball.setY(ball.getY() + 0.5d);
            }
        } else {
            ball.setY(ball.getY() - 1);
        }

        // Checking whether the ball should be jumping or not.
        if (ball.getIsJumping() && ball.getY() >= jumpingHeight)
            ball.setIsJumping(false);
        else if (!ball.getIsJumping() && ball.getY() <= 0)
            ball.setIsJumping(true);

        // Move the ball left.
        if (ballMoveLeft)
            ball.setX(ball.getX() - ball.getVelocity());

        // Move the ball right.
        if (ballMoveRight)
            ball.setX(ball.getVelocity());
    }

    /*
     * Update and render the screen.
     */
    private void render() {

        // Setting up the buffer strategy.
        bs = display.canvas.getBufferStrategy();
        if (bs == null) {
            display.canvas.createBufferStrategy(3);
            return;
        }

        // Clear up the screen.
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, width, height);

        // Prepare graphics.
        g.drawLine(0, height - gameBaseHeight, width, height - gameBaseHeight);
        g.fillOval(width / 2 - ((int)ball.getWidth()) / 2 + ((int)ball.getX()), height - gameBaseHeight - ((int)ball.getHeight()) - ((int)ball.getY()), ((int)ball.getWidth()), ((int)ball.getHeight()));

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

            if(delta >= 1) {
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