package com.acharluk.luketris;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ACharLuk on 02/03/2015.
 */
public class Luketris extends JFrame {

    JLabel barraEstado;

    public Luketris() {
        barraEstado = new JLabel(" 0");
        add(barraEstado, BorderLayout.SOUTH);
        Board board = new Board(this);
        add(board);
        board.start();

        setSize(200, 400);
        setTitle("Luketris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main (String[] args) {
        Luketris g = new Luketris();
        g.setLocationRelativeTo(null);
        g.setVisible(true);
    }

    public JLabel getBarraEstado() {
        return barraEstado;
    }

}
