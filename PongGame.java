import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongGame extends JPanel implements KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 20;
    private static final int PADDLE_HEIGHT = 100;
    private static final int BALL_DIAMETER = 20;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_SPEED = 5;
    private static final int WINNING_SCORE = 5;

    private int paddle1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int paddle2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int ballX = WIDTH / 2 - BALL_DIAMETER / 2;
    private int ballY = HEIGHT / 2 - BALL_DIAMETER / 2;
    private int ballXSpeed = BALL_SPEED;
    private int ballYSpeed = BALL_SPEED;
    private boolean player1Up = false;
    private boolean player1Down = false;
    private boolean player2Up = false;
    private boolean player2Down = false;
    private int player1Score = 0;
    private int player2Score = 0;

    private Font scoreFont = new Font("Arial", Font.BOLD, 40);

    public PongGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        Timer timer = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });
        timer.start();
    }

    public void update() {
        if (player1Up && paddle1Y > 0) {
            paddle1Y -= PADDLE_SPEED;
        }
        if (player1Down && paddle1Y < HEIGHT - PADDLE_HEIGHT) {
            paddle1Y += PADDLE_SPEED;
        }
        if (player2Up && paddle2Y > 0) {
            paddle2Y -= PADDLE_SPEED;
        }
        if (player2Down && paddle2Y < HEIGHT - PADDLE_HEIGHT) {
            paddle2Y += PADDLE_SPEED;
        }

        ballX += ballXSpeed;
        ballY += ballYSpeed;

        if (ballY <= 0 || ballY >= HEIGHT - BALL_DIAMETER) {
            ballYSpeed = -ballYSpeed;
        }

        if (ballX <= PADDLE_WIDTH && ballY + BALL_DIAMETER / 2 >= paddle1Y && ballY + BALL_DIAMETER / 2 <= paddle1Y + PADDLE_HEIGHT) {
            ballXSpeed = -ballXSpeed;
        } else if (ballX >= WIDTH - PADDLE_WIDTH - BALL_DIAMETER && ballY + BALL_DIAMETER / 2 >= paddle2Y && ballY + BALL_DIAMETER / 2 <= paddle2Y + PADDLE_HEIGHT) {
            ballXSpeed = -ballXSpeed;
        }

        if (ballX <= 0) {
            player2Score++;
            resetBall();
        } else if (ballX >= WIDTH - BALL_DIAMETER) {
            player1Score++;
            resetBall();
        }

        if (player1Score >= WINNING_SCORE || player2Score >= WINNING_SCORE) {
            endGame();
        }
    }

    private void resetBall() {
        ballX = WIDTH / 2 - BALL_DIAMETER / 2;
        ballY = HEIGHT / 2 - BALL_DIAMETER / 2;
        ballXSpeed = -ballXSpeed;
    }

    private void endGame() {
        // Print the winner and exit the game
        if (player1Score >= WINNING_SCORE) {
            System.out.println("Player 1 wins!");
        } else {
            System.out.println("Player 2 wins!");
        }
        System.exit(0);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(WIDTH - PADDLE_WIDTH, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillOval(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER);

        // Set paddle colors
        g.setColor(Color.WHITE);
        g.fillRect(0, paddle1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(WIDTH - PADDLE_WIDTH, paddle2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw score
        g.setFont(scoreFont);
        g.drawString(String.format("%02d", player1Score), WIDTH / 2 - 100, 100);
        g.drawString(String.format("%02d", player2Score), WIDTH / 2 + 50, 100);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            player1Up = true;
        } else if (key == KeyEvent.VK_S) {
            player1Down = true;
        } else if (key == KeyEvent.VK_UP) {
            player2Up = true;
        } else if (key == KeyEvent.VK_DOWN) {
            player2Down = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            player1Up = false;
        } else if (key == KeyEvent.VK_S) {
            player1Down = false;
        } else if (key == KeyEvent.VK_UP) {
            player2Up = false;
        } else if (key == KeyEvent.VK_DOWN) {
            player2Down = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new PongGame(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
