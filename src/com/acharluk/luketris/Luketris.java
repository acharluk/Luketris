package com.acharluk.luketris;

import javax.swing.*;

/**
 * Created by ACharLuk on 02/03/2015.
 */
public class Luketris extends JFrame {

    public Luketris() {
        Forma f = new Forma();
        f.setFormaAleatoria();
        System.out.println(f.getX(1));
    }

    public static void main (String[] args) {
        new Luketris();
    }

}
