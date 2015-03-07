package com.acharluk.luketris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.acharluk.luketris.Forma.Tetriminos;

/**
 * Created by ACharLuk on 02/03/2015.
 */
public class Board extends JPanel implements ActionListener {

    int anchura = 10;
    int altura = 22;

    Timer timer;

    boolean haCaido = false;
    boolean haEmpezado = false;
    boolean pausado = false;

    int lineasEliminadas = 0;
    int curX = 0;
    int curY = 0;

    JLabel statusbar;
    Forma piezaActual;
    Tetriminos[] board;


    public Board(Luketris juego) {
        setFocusable(true);
        piezaActual = new Forma();
        timer = new Timer(400, this);
        timer.start();

        statusbar =  juego.getBarraEstado();
        board = new Tetriminos[anchura * altura];
        addKeyListener(new Input());
        limpiar();
    }

    public void actionPerformed(ActionEvent e) {
        if (haCaido) {
            haCaido = false;
            nuevaPieza();
        } else {
            bajarLinea();
        }
    }

    int anchoCuadrado() { return (int) getSize().getWidth() / anchura; }
    int altoCuadrado() { return (int) getSize().getHeight() / altura; }
    Tetriminos formaEn(int x, int y) { return board[(y * anchura) + x]; }

    public void start() {
        if (pausado) return;

        haEmpezado = true;
        haCaido = false;
        lineasEliminadas = 0;
        limpiar();

        nuevaPieza();
        timer.start();
    }

    public void pausar() {
        if (!haEmpezado)
            return;

        pausado = !pausado;
        if (pausado) {
            timer.stop();
            statusbar.setText("Pausado");
        } else {
            timer.start();
            statusbar.setText(String.valueOf(lineasEliminadas));
        }
        repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - altura * altoCuadrado();


        for (int i = 0; i < altura; i++) {
            for (int j = 0; j < anchura; ++j) {
                Tetriminos forma = formaEn(j, altura - i - 1);
                if (forma != Tetriminos.Sin)
                    dibujarC(g, 0 + j * anchoCuadrado(), boardTop + i * altoCuadrado(), forma);
            }
        }

        if (piezaActual.getForma() != Tetriminos.Sin) {
            for (int i = 0; i < 4; i++) {
                int x = curX + piezaActual.getX(i);
                int y = curY - piezaActual.getY(i);
                dibujarC(g, 0 + x * anchoCuadrado(), boardTop + (altura - y - 1) * altoCuadrado(), piezaActual.getForma());
            }
        }
    }

    private void bajar() {
        int nuevaY = curY;
        while (nuevaY > 0) {
            if (!intentaMover(piezaActual, curX, nuevaY - 1))
                break;
            --nuevaY;
        }
        piezaLanzada();
    }

    private void bajarLinea() {
        if (!intentaMover(piezaActual, curX, curY - 1))
            piezaLanzada();
    }


    private void limpiar() {
        for (int i = 0; i < altura * anchura; i++)
            board[i] = Tetriminos.Sin;
    }

    private void piezaLanzada() {
        for (int i = 0; i < 4; i++) {
            int x = curX + piezaActual.getX(i);
            int y = curY - piezaActual.getY(i);
            board[(y * anchura) + x] = piezaActual.getForma();
        }

        eliminarLineaCompleta();

        if (!haCaido)
            nuevaPieza();
    }

    private void nuevaPieza() {
        piezaActual.setFormaAleatoria();
        curX = anchura / 2 + 1;
        curY = altura - 1 + piezaActual.minY();

        if (!intentaMover(piezaActual, curX, curY)) {
            piezaActual.setForma(Tetriminos.Sin);
            timer.stop();
            haEmpezado = false;
            statusbar.setText("GAME OVER");
        }
    }

    private boolean intentaMover(Forma nuevaPieza, int nuevaX, int nuevaY) {
        for (int i = 0; i < 4; i++) {
            int x = nuevaX + nuevaPieza.getX(i);
            int y = nuevaY - nuevaPieza.getY(i);
            if (x < 0 || x >= anchura || y < 0 || y >= altura)
                return false;
            if (formaEn(x, y) != Tetriminos.Sin)
                return false;
        }

        piezaActual = nuevaPieza;
        curX = nuevaX;
        curY = nuevaY;
        repaint();
        return true;
    }

    private void eliminarLineaCompleta() {
        int lineasCompletas = 0;

        for (int i = altura - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < anchura; ++j) {
                if (formaEn(j, i) == Tetriminos.Sin) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++lineasCompletas;
                for (int k = i; k < altura - 1; ++k) {
                    for (int j = 0; j < anchura; ++j)
                        board[(k * anchura) + j] = formaEn(j, k + 1);
                }
            }
        }

        if (lineasCompletas > 0) {
            lineasEliminadas += lineasCompletas;
            statusbar.setText(String.valueOf(lineasEliminadas));
            haCaido = true;
            piezaActual.setForma(Tetriminos.Sin);
            repaint();
        }
    }

    private void dibujarC(Graphics g, int x, int y, Tetriminos forma) {
        Color colores[] = { new Color(0, 0, 0), new Color(204, 102, 102),
                new Color(102, 204, 102), new Color(102, 102, 204),
                new Color(204, 204, 102), new Color(204, 102, 204),
                new Color(102, 204, 204), new Color(218, 170, 0)
        };


        Color color = colores[forma.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, anchoCuadrado() - 2, altoCuadrado() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + altoCuadrado() - 1, x, y);
        g.drawLine(x, y, x + anchoCuadrado() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + altoCuadrado() - 1,
                x + anchoCuadrado() - 1, y + altoCuadrado() - 1);
        g.drawLine(x + anchoCuadrado() - 1, y + altoCuadrado() - 1,
                x + anchoCuadrado() - 1, y + 1);
    }

    class Input extends KeyAdapter {
        public void keyPressed(KeyEvent e) {

            if (!haEmpezado || piezaActual.getForma() == Forma.Tetriminos.Sin) {
                return;
            }

            int tecla = e.getKeyCode();

            if (tecla == 'p' || tecla == 'P') {
                pausar();
                return;
            }

            if (pausado)
                return;

            switch (tecla) {
                case KeyEvent.VK_LEFT:
                    intentaMover(piezaActual, curX - 1, curY);
                    break;
                case KeyEvent.VK_RIGHT:
                    intentaMover(piezaActual, curX + 1, curY);
                    break;
                case KeyEvent.VK_DOWN:
                    intentaMover(piezaActual.rotarDerecha(), curX, curY);
                    break;
                case KeyEvent.VK_UP:
                    intentaMover(piezaActual.rotarIzquierda(), curX, curY);
                    break;
                case KeyEvent.VK_SPACE:
                    bajar();
                    break;
                case 'd':
                    bajarLinea();
                    break;
                case 'D':
                    bajarLinea();
                    break;
            }

        }
    }

}
