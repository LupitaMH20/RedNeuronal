package Interfaz;

import Funcionalidad.Tdamatriz;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class PanelVisualizacion extends JPanel {

    private BufferedImage imagenDesdeFila = null;

    public PanelVisualizacion() {
        setBackground(Color.BLACK);
    }

    public void dibujarDesdeFila(int fila, Tdamatriz tda) {
    int[][] datos = tda.getOriginales();

    if (datos == null || fila < 0 || fila >= datos.length) {
        System.out.println("Fila inválida o matriz nula.");
        return;
    }

    int[] filaDatos = datos[fila];
    if (filaDatos.length != 22500) { // 150x150 = 22500
        System.out.println("La fila no tiene 22500 elementos.");
        return;
    }

    int anchoPanel = getWidth();
    int altoPanel = getHeight();
    double escalaX = anchoPanel / 150.0;
    double escalaY = altoPanel / 150.0;

    int umbral = 50;

    imagenDesdeFila = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = imagenDesdeFila.createGraphics();
    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, anchoPanel, altoPanel);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setColor(Color.RED);

    for (int y = 0; y < 150; y++) {
        for (int x = 0; x < 150; x++) {
            int valor = filaDatos[y * 150 + x];
            if (valor > umbral) {
                int xReal = (int) (x * escalaX + escalaX / 2);
                int yReal = (int) (y * escalaY + escalaY / 2);
                int radio = 2; // Opcionalmente más pequeño para mayor densidad
                g2d.fillOval(xReal - radio / 2, yReal - radio / 2, radio, radio);
            }
        }
    }

    g2d.dispose();
    repaint(); // Llama a paintComponent para mostrar la imagen
}


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenDesdeFila != null) {
            g.drawImage(imagenDesdeFila, 0, 0, null);
        }
    }

    public void limpiar() {
        imagenDesdeFila = null;
        repaint();
    }
}
