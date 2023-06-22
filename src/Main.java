import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

class Launcher {
    public static void main(String[] args) {
        Game game = new Game(1600, 900, "Jumping Ball Game!");
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

    public Game(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    private void init() {
        display = new Display(width, height, title);

        // Add key listeners.
        display.canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Key pressed: " + e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    ballMoveLeft = true;

                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    ballMoveRight = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("Key released: " + e.getKeyCode());

                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    ballMoveLeft = false;

                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    ballMoveRight = false;
            }
        });
    }

    // TODO: Variables should be in a class and not here.
    int baseHeight = 100;
    int ballSizeX = 50;
    int ballSizeY = 50;
    int ballX = width / 2 - ballSizeX / 2;
    double ballY = 0;
    boolean ballMoveLeft = false;
    boolean ballMoveRight = false;
    boolean isJumping = true;
    int jumpingHeight = 100;

    private void tick() {
        if (isJumping) {
            if (ballY < jumpingHeight / 2d) { // Just some simple testing...
                ballY++;
            } else {
                ballY += 0.5;
            }
        } else{
            ballY--;
        }

        // Checking if the ball should be jumping or not.
        if (isJumping && ballY >= jumpingHeight) {
            isJumping = false;
        } else if (!isJumping && ballY <= 0) {
            isJumping = true;
        }

        // Move the ball left.
        if (ballMoveLeft)
            ballX -= 500;

        // Move the ball right.
        if (ballMoveRight)
            ballX += 500;
    }

    private void render() {
        // Setting up the buffer.
        bs = display.canvas.getBufferStrategy();
        if(bs == null) {
            display.canvas.createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();
        g.clearRect(0, 0, width, height);

        // Drawing here...
        g.drawLine(0, height - baseHeight, width, height - baseHeight);
        g.fillOval(width / 2 - ballSizeX / 2 + ballX, height - baseHeight - ballSizeY - ((int)ballY), ballSizeX, ballSizeY);

        // Buffer and graphics...
        bs.show();
        g.dispose();
    }

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

    public synchronized void start() {
        if (running) return;

        running = true;

        thread = new Thread(this);
        thread.start();
    }

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