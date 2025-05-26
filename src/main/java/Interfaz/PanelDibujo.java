package Interfaz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class PanelDibujo extends JPanel {

    private ArrayList<ArrayList<Point>> trazos = new ArrayList<>();
    private ArrayList<Point> trazoActual = null;

    public PanelDibujo() {
        setBackground(Color.BLACK);

        // Cuando se presiona el mouse: empieza un nuevo trazo
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                trazoActual = new ArrayList<>();
                trazoActual.add(e.getPoint());
                trazos.add(trazoActual);
            }
        });

        // Mientras se arrastra: agrega puntos al trazo actual
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (trazoActual != null) {
                    trazoActual.add(e.getPoint());
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);

        for (ArrayList<Point> trazo : trazos) {
            for (int i = 1; i < trazo.size(); i++) {
                Point p1 = trazo.get(i - 1);
                Point p2 = trazo.get(i);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    public void limpiar() {
        trazos.clear();
        repaint();
    }
}
