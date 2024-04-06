import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Tetris extends JPanel implements KeyListener, ActionListener {

    private static final long serialVersionUID = 1L;
    private final int WIDTH = 10;
    private final int HEIGHT = 20;
    private final int CELL_SIZE = 30;
    private final int DELAY = 300;
    private Timer timer;
    private boolean[][] board;
    private int score;
    private int currentPieceX, currentPieceY;
    private int currentPieceType;
    private int[][][] pieces = {
            { { 1, 1, 1, 1 } }, // I
            { { 1, 1, 1 }, { 0, 1, 0 } }, // T
            { { 1, 1, 1 }, { 1, 0, 0 } }, // L
            { { 1, 1, 1 }, { 0, 0, 1 } }, // J
            { { 1, 1 }, { 1, 1 } }, // O
            { { 1, 1, 0 }, { 0, 1, 1 } }, // Z
            { { 0, 1, 1 }, { 1, 1, 0 } } // S
    };
    private JLabel instructionsLabel;

    public Tetris() {
        board = new boolean[HEIGHT][WIDTH];
        timer = new Timer(DELAY, this);
        timer.start();
        score = 0;
        currentPieceX = WIDTH / 2 - 1;
        currentPieceY = 0;
        currentPieceType = (int) (Math.random() * pieces.length);
        setPreferredSize(new Dimension(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setFocusable(true);
        addKeyListener(this);

        instructionsLabel = new JLabel("Press UP arrow to rotate the tetrominos");
        instructionsLabel.setForeground(Color.WHITE);
        instructionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(instructionsLabel, BorderLayout.NORTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPiece(g);
    }

    private void drawBoard(Graphics g) {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j]) {
                    g.setColor(Color.GREEN);
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void drawPiece(Graphics g) {
        g.setColor(Color.RED);
        int[][] currentPiece = pieces[currentPieceType];
        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[i].length; j++) {
                if (currentPiece[i][j] == 1) {
                    g.fillRect((currentPieceX + j) * CELL_SIZE, (currentPieceY + i) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void dropPiece() {
        while (canMove(currentPieceX, currentPieceY + 1)) {
            currentPieceY++;
        }
        placePiece();
        removeFullLines();
        currentPieceX = WIDTH / 2 - 1;
        currentPieceY = 0;
        currentPieceType = (int) (Math.random() * pieces.length);
        if (!canMove(currentPieceX, currentPieceY)) {
            gameOver();
        }
    }

    private void placePiece() {
        int[][] currentPiece = pieces[currentPieceType];
        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[i].length; j++) {
                if (currentPiece[i][j] == 1) {
                    board[currentPieceY + i][currentPieceX + j] = true;
                }
            }
        }
    }

    private void removeFullLines() {
        for (int i = HEIGHT - 1; i >= 0; i--) {
            boolean fullLine = true;
            for (int j = 0; j < WIDTH; j++) {
                if (!board[i][j]) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                score++;
                for (int k = i; k > 0; k--) {
                    for (int j = 0; j < WIDTH; j++) {
                        board[k][j] = board[k - 1][j];
                    }
                }
                i++;
            }
        }
    }

    private boolean canMove(int x, int y) {
        int[][] currentPiece = pieces[currentPieceType];
        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[i].length; j++) {
                if (currentPiece[i][j] == 1) {
                    int newX = x + j;
                    int newY = y + i;
                    if (newX < 0 || newX >= WIDTH || newY >= HEIGHT || (newY >= 0 && board[newY][newX])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void gameOver() {
        timer.stop();
        System.out.println("Game Over! Your score is: " + score);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (canMove(currentPieceX, currentPieceY + 1)) {
            currentPieceY++;
        } else {
            placePiece();
            removeFullLines();
            currentPieceX = WIDTH / 2 - 1;
            currentPieceY = 0;
            currentPieceType = (int) (Math.random() * pieces.length);
            if (!canMove(currentPieceX, currentPieceY)) {
                gameOver();
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (canMove(currentPieceX - 1, currentPieceY)) {
                    currentPieceX--;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (canMove(currentPieceX + 1, currentPieceY)) {
                    currentPieceX++;
                }
                break;
            case KeyEvent.VK_DOWN:
                dropPiece();
                break;
            case KeyEvent.VK_UP:
                rotatePiece();
                break;
        }
        repaint();
    }

    private void rotatePiece() {
        int[][] currentPiece = pieces[currentPieceType];
        int[][] rotatedPiece = new int[currentPiece[0].length][currentPiece.length];
        for (int i = 0; i < currentPiece.length; i++) {
            for (int j = 0; j < currentPiece[i].length; j++) {
                rotatedPiece[j][currentPiece.length - i - 1] = currentPiece[i][j];
            }
        }
        if (canRotate(rotatedPiece, currentPieceX, currentPieceY)) {
            pieces[currentPieceType] = rotatedPiece;
        }
    }

    private boolean canRotate(int[][] rotatedPiece, int x, int y) {
        for (int i = 0; i < rotatedPiece.length; i++) {
            for (int j = 0; j < rotatedPiece[i].length; j++) {
                if (rotatedPiece[i][j] == 1) {
                    int newX = x + j;
                    int newY = y + i;
                    if (newX < 0 || newX >= WIDTH || newY >= HEIGHT || (newY >= 0 && board[newY][newX])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        Tetris tetris = new Tetris();
        frame.add(tetris);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
