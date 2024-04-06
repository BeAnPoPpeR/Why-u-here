import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class ChromeDinoGame extends JFrame implements KeyListener {

    private final int WIDTH = 800;
    private final int HEIGHT = 300;
    private final int GROUND_Y = 220;
    private final int DINO_WIDTH = 40;
    private final int DINO_HEIGHT = 40;
    private final int GRAVITY = 1;
    private final int CACTUS_SPEED = 7;

    private boolean isJumping = false;
    private int dinoY = GROUND_Y - DINO_HEIGHT;
    private int dinoVelocity = 0;
    private ArrayList<Rectangle> cacti = new ArrayList<>();
    private Timer timer;

    public ChromeDinoGame() {
        setTitle("Chrome Dino Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(this);

        timer = new Timer(10, e -> {
            update();
            repaint();
        });
        timer.start();

        setVisible(true);
    }

    private void update() {
        if (isJumping) {
            dinoVelocity -= GRAVITY;
            dinoY -= dinoVelocity;

            if (dinoY >= GROUND_Y - DINO_HEIGHT) {
                dinoY = GROUND_Y - DINO_HEIGHT;
                isJumping = false;
            }
        }

        // Update cactus positions
        for (Rectangle cactus : cacti) {
            cactus.x -= CACTUS_SPEED;

            if (cactus.x + cactus.width <= 0) {
                cacti.remove(cactus);
                break;
            }

            if (cactus.intersects(new Rectangle(50, dinoY, DINO_WIDTH, DINO_HEIGHT))) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over!");
                System.exit(0);
            }
        }

        // Add new cactus with balanced spawning
        if (cacti.isEmpty() || (cacti.get(cacti.size() - 1).x < WIDTH - 200 && Math.random() < 0.03)) {
            // Randomly select one of the cactus types
            int cactusType = new Random().nextInt(3);
            int cactusHeight = 40 + (cactusType * 10);
            int cactusWidth = 20 + (cactusType * 10);
            cacti.add(new Rectangle(WIDTH, GROUND_Y - cactusHeight, cactusWidth, cactusHeight));
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw ground
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, GROUND_Y, WIDTH, HEIGHT - GROUND_Y);

        // Draw dinosaur
        g.setColor(Color.GRAY);
        g.fillRect(50, dinoY, DINO_WIDTH, DINO_HEIGHT);

        // Draw cacti
        g.setColor(Color.GREEN);
        for (Rectangle cactus : cacti) {
            g.fillRect(cactus.x, cactus.y, cactus.width, cactus.height);
        }
    }

    public static void main(String[] args) {
        new ChromeDinoGame();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !isJumping) {
            isJumping = true;
            dinoVelocity = 15;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

