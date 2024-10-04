package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 520);
        frame.setTitle("Snake   |   DERANCOURT Louis  06/05/2024");
        frame.setResizable(false);

        Snake snake = new Snake();
        frame.add(snake);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        System.err.println("ICI -------------");
    }
}
