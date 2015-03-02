package com.acharluk.luketris;

import java.util.Random;

/**
 * Created by ACharLuk on 02/03/2015.
 */
public class Forma {

    enum Tetriminos { Sin, Z, S, Linea, T, Cuadrado, L, LInv };

    private Tetriminos formaTetrimino;
    private int[][] coords;
    private int[][][] tablaCoords;

    public Forma() {
        coords = new int[4][2];
        setForma(Tetriminos.Sin);
    }

    public void setForma(Tetriminos forma) {

        tablaCoords = new int[][][] {
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },
                { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },
                { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },
                { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },
                { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }
        };

        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = tablaCoords[forma.ordinal()][i][j];
            }
        }
        formaTetrimino = forma;
    }

    public void setFormaAleatoria() {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Tetriminos[] values = Tetriminos.values();
        setForma(values[x]);
    }

    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    public int getX(int index) {
        return coords[index][0];
    }

    public int getY(int index) {
        return coords[index][1];
    }

    public Tetriminos getShape() {
        return formaTetrimino;
    }

    public int minX() {
        int m = coords[0][0];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }


    public int minY() {
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    public Forma rotarIzquierda() {
        if (formaTetrimino == Tetriminos.Cuadrado)
            return this;

        Forma nuevo = new Forma();
        nuevo.formaTetrimino = formaTetrimino;

        for (int i = 0; i < 4; ++i) {
            nuevo.setX(i, getY(i));
            nuevo.setY(i, - getX(i));
        }
        return nuevo;
    }

    public Forma rotarDerecha() {
        if (formaTetrimino == Tetriminos.Cuadrado)
            return this;

        Forma nuevo = new Forma();
        nuevo.formaTetrimino = formaTetrimino;

        for (int i = 0; i < 4; ++i) {
            nuevo.setX(i, - getY(i));
            nuevo.setY(i, getX(i));
        }
        return nuevo;
    }


}
