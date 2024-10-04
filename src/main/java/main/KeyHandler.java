package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private Snake snake;
    private boolean canChange = true;
    private boolean up, down, left, right, escPressed, quit = false, pause = false, pausePressed=false, reset = false, ini = false;
    final int quitDelay = 30;
    int quitCooldown;
    int debug = 0, auto = 0, move = 0;

    protected KeyHandler(Snake snake) {
        allFalse();
        this.snake = snake;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (canChange) {
            switch (code) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_Z:
                    if (!down) {
                        allFalse();
                        up = true;
                        canChange = false;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if (!up) {
                        allFalse();
                        down = true;
                        canChange = false;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_Q:
                    if (!right) {
                        allFalse();
                        left = true;
                        canChange = false;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    if (!left) {
                        allFalse();
                        right = true;
                        canChange = false;
                    }
                    break;
            }
        }

        if (code == KeyEvent.VK_R) {
            reset = true;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            escPressed = true;
            quitCooldown++;
            if (quitCooldown >= quitDelay) quit = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            if (!pausePressed) pause = !pause;
            pausePressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_ESCAPE) {
            escPressed = false;
            quitCooldown = 0;
        }
        if (code == KeyEvent.VK_SPACE) pausePressed = false;

        if (debug == 0 && code == KeyEvent.VK_C) debug = 1;
        else if (debug == 1 && code == KeyEvent.VK_H) debug = 2;
        else if (debug == 2 && code == KeyEvent.VK_E) debug = 3;
        else if (debug == 3 && code == KeyEvent.VK_A) debug = 4;
        else if (debug == 4 && code == KeyEvent.VK_T) {
            debug = 0;
            snake.debug();
        }
        if (auto == 0 && code == KeyEvent.VK_A) auto = 1;
        else if (auto == 1 && code == KeyEvent.VK_U) auto = 2;
        else if (auto == 2 && code == KeyEvent.VK_T) auto = 3;
        else if (auto == 3 && code == KeyEvent.VK_O) {
            auto = 0;
            if (!snake.getAuto()) snake.food();
            snake.auto();
        }

        if (move == 0 && code == KeyEvent.VK_M) move = 1;
        else if (move == 1 && code == KeyEvent.VK_O) move = 2;
        else if (move == 2 && code == KeyEvent.VK_V) move = 3;
        else if (move == 3 && code == KeyEvent.VK_E) {
            move = 0;
            snake.fast();
        }
    }

    private void allFalse() {
        up = false;
        down = false;
        left = false;
        right = false;
    }

    protected boolean stillDefault() {
        if (ini) return false;
        if (!up && !down && !left && !right && !reset) {
            return true;
        }
        else {
            if (!snake.getAuto()) snake.food();
            ini = true;
            return false;
        }
    }

    protected boolean isUp() {return up;}
    protected boolean isDown() {return down;}
    protected boolean isLeft() {return left;}
    protected boolean isRight() {return right;}
    protected boolean isEscPressed() {return escPressed;}
    protected boolean isPause() {return pause;}
    protected boolean isQuit() {return quit;}
    protected boolean isReset() {return reset;}

    protected void reset() {
        reset = false;
        allFalse();
        ini = false;
    }
    protected void unPause() {
        pause = false;
    }
    protected void canChange() {
        canChange = true;
    }
}
