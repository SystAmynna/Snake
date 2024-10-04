package main;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class Snake extends JPanel implements Runnable {

    private boolean restart;
    private int [][] grid;
    private final int SCALE = 10;
    int normalUpdate = 12;
    private KeyHandler keyH = new KeyHandler(this);
    private Thread gameThread;
    private int resetCooldown = 0;
    private int x, y, l = 3;
    private boolean gameOver = false, auto = false;
    private int autoCount = 0;
    private boolean up = false, down = false, left = false, right = false;

    protected Snake() {
        grid = new int[SCALE][SCALE];
        this.setPreferredSize(new Dimension(500, 500));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(this.keyH);
        this.setFocusable(true);
        this.requestFocusInWindow();
        x = 2;
        y = 2;
        grid[x][y] = l;

        startGame();
    }
    private void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        final int D = 0;
        final int S = 500/SCALE;
        

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (gameOver && !victory()) {
                    if (grid[i][j] > 0) g2.setColor(Color.red);
                    else g2.setColor(Color.DARK_GRAY);
                } else {
                    if (grid[i][j] < 0) g2.setColor(Color.blue);
                    else if (grid[i][j] > 0) g2.setColor(Color.green);
                    else g2.setColor(Color.white);
                    if (i==x && j==y) g2.setColor(Color.cyan);
                }
                g2.fillRect(i*S, j*S+D, S, S);

                //g2.setColor(Color.black);
                //g2.drawString(""+grid[i][j], (i*S+D+15), (j*S+D+25));
            }
        }

        if (keyH.stillDefault() && !gameOver && !auto) {
            int _x = 250;
            g2.setColor(Color.red);
            g2.drawString("Press Z or [ARROW UP] to go up", _x, 300);
            g2.drawString("Press S or [ARROW DOWN] to go down", _x, 315);
            g2.drawString("Press Q or [ARROW LEFT] to go left", _x, 330);
            g2.drawString("Press D or [ARROW RIGHT] to go right", _x, 345);
            g2.drawString("Press R to reset", _x, 370);
            g2.drawString("Press SPACE to pause", _x, 385);
            g2.drawString("Press ESC (keep) to quit the game", _x, 410);
        }

        if (keyH.isEscPressed()) {
            g2.setColor(Color.black);
            g2.drawString("Fermeture...", 1, 10);
        }
        if (keyH.isPause()) {
            g2.setColor(Color.GRAY);
            g2.fillRect(230, 240, 40, 20);
            g2.setColor(Color.red);
            g2.drawString("Pause", 233, 255);
        }
        if (resetCooldown > 0) {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(230, 240, 40, 20);
            g2.setColor(Color.white);
            g2.drawString("Reset", 235, 255);
        }
        if (gameOver) {
            if (victory()) {
                g2.setColor(Color.BLUE);
                g2.fillRect(210, 230, 80, 40);
                g2.setColor(Color.GREEN);
                g2.drawString("! VICTOIRE !", 216, 255);
                g2.setColor(Color.BLACK);
            } else {
                g2.setColor(Color.BLACK);
                g2.fillRect(210, 230, 80, 40);
                g2.setColor(Color.RED);
                g2.drawString("! DEFAITE !", 218, 255);
                g2.setColor(Color.ORANGE);
            }
            g2.drawString("press R for Reset", 205, 300);

            int d = 215;
            int v = (l-3)*100;
            if (v >= 100 && v < 1000) d = 208;
            else if (v >= 1000 && v < 10000) d = 204;

            g2.drawString("score: "+v+"/9700", d, 280);
        }



    }

    private void update() {

        if (keyH.isQuit()) System.exit(0);
        if (keyH.isReset()) reset();
        if (resetCooldown > 0) resetCooldown--;

        keyH.canChange();

        if (!keyH.stillDefault() && !keyH.isPause() && !gameOver && !auto) {
            if (keyH.isUp()) goUp();
            else if (keyH.isDown()) goDown();
            else if (keyH.isLeft()) goLeft();
            else if (keyH.isRight()) goRight();
            redirect();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] > 0) grid[i][j]--;
                }
            }
        }

        if (auto && !gameOver && !keyH.isPause()) {
            auto(autoCount);
            redirect();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] > 0) grid[i][j]--;
                }
            }
        }

        if (victory()) gameOver = true;

    }

    private void goUp() {
        allNotGo();
        up = true;
    }
    private void goDown() {
        allNotGo();
        down = true;
    }
    private void goLeft() {
        allNotGo();
        left = true;
    }
    private void goRight() {
        allNotGo();
        right = true;
    }
    private void allNotGo() {
        up = false;
        down = false;
        left = false;
        right = false;
    }

    private void redirect() {
        if (up) Up();
        else if (down) Down();
        else if (left) Left();
        else if (right) Right();
    }
    private void Up() {
        if (y-1 < 0 || grid[x][y-1] > 0) gameOver = true;
        if (!gameOver) {
            if (grid[x][y-1] == -1) eat();
            grid[x][y-1] = l+1;
            y--;
        }
    }
    private void Down() {
        if (y+1 > SCALE-1 || grid[x][y+1] > 0) gameOver = true;
        if (!gameOver) {
            if (grid[x][y+1] == -1) eat();
            grid[x][y+1] = l+1;
            y++;
        }
    }
    private void Left() {
        if (x-1 < 0 || grid[x-1][y] > 0) gameOver = true;
        if (!gameOver) {
            if (grid[x-1][y] == -1) eat();
            grid[x-1][y] = l+1;
            x--;
        }
    }
    private void Right() {
        if (x+1 > SCALE-1 || grid[x+1][y] > 0) gameOver = true;
        if (!gameOver) {
            if (grid[x+1][y] == -1) eat();
            grid[x+1][y] = l+1;
            x++;
        }
    }

    protected boolean getAuto() {return auto;}

    public void run() {
        while (gameThread.isAlive()) {
            restart = false;

            int normalFps = 60;
            double drawInterval = (double)(1000000000 / normalFps);
            double delta = 0.0;
            long lastTime = System.nanoTime();
            long timer = 0L;
            int fps = 0;
            int update = 0;
            int updateCounter = 0;

            long currentTime;
            while(!restart) {
                currentTime = System.nanoTime();
                delta += (double)(currentTime - lastTime) / drawInterval;
                timer += currentTime - lastTime;
                lastTime = currentTime;
                if (delta >= 1.0) {

                    if (update == normalUpdate) {
                        this.update();
                        update = 0;
                        updateCounter++;
                    }
                    this.repaint();

                    --delta;
                    ++fps;
                    update++;
                }

                if (timer >= 1000000000L) {
                    System.out.println("Updates: " + updateCounter + "/5  |  FPS: " + fps + "/" + normalFps);
                    fps = 0;
                    timer = 0L;
                    updateCounter = 0;
                }
            }
        }




    }

    private boolean victory() {
        for (int[] ints : grid) {
            for (int anInt : ints) {
                if (anInt < 1) return false;
            }
        }
        return true;
    }
    private void eat() {
        l++;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] > 0) grid[i][j]++;
            }
        }
        food();
    }
    protected void food() {
        int [][] list = new int[SCALE*SCALE][2];
        int index = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 0) {
                    list[index][0] = i;
                    list[index][1] = j;
                    index++;
                }
            }
        }
        if (index != 0) {
            Random r = new Random();
            int _r = r.nextInt(0, index);
            grid[list[_r][0]][list[_r][1]] = -1;
        }
    }

    private void reset() {
        keyH.reset();
        auto = false;
        autoCount = 0;
        normalUpdate = 12;
        resetCooldown = 4;
        for (int[] ints : grid) Arrays.fill(ints, 0);
        x = 2;
        y = 2;
        l = 3;
        grid[x][y] = l;
        gameOver = false;
        keyH.unPause();
    }

    protected void debug() {
        for (int[] ints : grid) {
            Arrays.fill(ints, 16);
        }
    }
     protected void auto() {
        reset();
        food();
        auto = true;
     }
     protected void auto(int in) {
        switch (in) {
            case 0:
            case 1: goUp(); break;
            case 2:
            case 3: goLeft(); break;
        }

        if (in == 4 || in == 32 || in == 50 || in == 68 || in == 86) goDown();
        if (in == 13) goRight();
        if (in == 22 || in == 41 || in == 59 || in == 77 || in == 95) goUp();
        if (in == 31 || in == 40 || in == 49 || in == 58 || in == 67 || in == 76 || in == 85 || in == 94 || in == 103) goLeft();
        if (in == 103) autoCount = 3;

        autoCount++;
     }
     protected void fast() {
        normalUpdate = 1;
        restart = true;
     }
}
