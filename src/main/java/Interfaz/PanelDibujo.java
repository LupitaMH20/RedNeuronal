package Interfaz;

import Funcionalidad.Tdamatriz;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.awt.image.BufferedImage;
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

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3)); // Grosor del trazo en pantalla

        for (ArrayList<Point> trazo : trazos) {
            for (int i = 1; i < trazo.size(); i++) {
                Point p1 = trazo.get(i - 1);
                Point p2 = trazo.get(i);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    public void limpiar() {
        trazos.clear();
        repaint();
    }

    public void verificarTrazos() {
        System.out.println("Cantidad de trazos: " + trazos.size());
        for (int i = 0; i < trazos.size(); i++) {
            System.out.println("Trazo " + i + " tiene " + trazos.get(i).size() + " puntos");
        }
    }

    public BufferedImage crearImagen20x20() {
        int ancho = getWidth();
        int alto = getHeight();

        if (ancho == 0 || alto == 0) {
            System.out.println("El tamaño del lienzo es 0, no se puede escalar");
            return null;
        }

        BufferedImage imagen = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagen.createGraphics();

        // Fondo negro
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 20, 20);

        // Dibujar trazos en rojo con trazo grueso
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2)); // Grosor del trazo en la imagen

        // Calcular factor de escala
        double escalaX = 20.0 / ancho;
        double escalaY = 20.0 / alto;

        for (ArrayList<Point> trazo : trazos) {
            for (int i = 1; i < trazo.size(); i++) {
                Point p1 = trazo.get(i - 1);
                Point p2 = trazo.get(i);

                int x1 = (int) (p1.x * escalaX);
                int y1 = (int) (p1.y * escalaY);
                int x2 = (int) (p2.x * escalaX);
                int y2 = (int) (p2.y * escalaY);

                g.drawLine(x1, y1, x2, y2);
            }
        }

        g.dispose();
        return imagen;
    }

    public void dibujarDesdeFila(int fila, Tdamatriz tda) {
    int[][] datos = tda.getnNormalizados();

    if (datos == null || fila < 0 || fila >= datos.length) {
        System.out.println("Fila inválida o matriz nula.");
        return;
    }

    int[] filaDatos = datos[fila]; // Debe tener 400 elementos (20x20)
    if (filaDatos.length != 400) {
        System.out.println("La fila no tiene 400 elementos.");
        return;
    }

    trazos.clear();
    trazoActual = new ArrayList<>();

    int anchoPanel = getWidth();
    int altoPanel = getHeight();

    double escalaX = anchoPanel / 20.0;
    double escalaY = altoPanel / 20.0;

    int umbral = 50;

    // Crear una imagen temporal
    BufferedImage imagen = new BufferedImage(anchoPanel, altoPanel, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = imagen.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2d.setColor(Color.RED); // Color rojo para los puntos

    for (int y = 0; y < 20; y++) {
        for (int x = 0; x < 20; x++) {
            int valor = filaDatos[y * 20 + x];

            if (valor > umbral) {
                int xReal = (int) (x * escalaX + escalaX / 2);
                int yReal = (int) (y * escalaY + escalaY / 2);
                int radio = 4; // Tamaño del punto
                g2d.fillOval(xReal - radio / 2, yReal - radio / 2, radio, radio);
            }
        }
    }

    g2d.dispose();

    // Dibuja la imagen en el panel directamente
    Graphics g = getGraphics();
    if (g != null) {
        g.drawImage(imagen, 0, 0, null);
        g.dispose();
    }
}


}
