package Funcionalidad;

import Interfaz.PanelDibujo;
import java.awt.image.BufferedImage;

public class Principal {

    private Tdamatriz tda;
    private int[][] mN;

    public Principal() {
        this.tda = new Tdamatriz();
    }

    public void valoresImg(int cont, BufferedImage imagen, BufferedImage imagenOriginal) {
        int width = imagen.getWidth(); // 20
        int height = imagen.getHeight();
        int widthOrig = imagenOriginal.getWidth(); // 150
        int heightOrig = imagenOriginal.getHeight();

        if (tda.getnNormalizados() == null || tda.getOriginales() == null) {
            tda.inicializar(); 
        }
        

        if (cont >= tda.getnNormalizados().length || cont >= tda.getOriginales().length) {
            System.err.println("Error: Índice de imagen fuera de rango o estructura no inicializada.");
            return;
        }
        
        // Guardar en nNormalizados (20x20)
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = imagen.getRGB(x, y);
                int gray = convertirAGris(pixel);
                tda.getnNormalizados()[cont][index++] = gray;
            }
        }

        System.out.println("Imagen " + cont + " normalizada. Valores: " + index);

        // Guardar en originales (150x150)
        index = 0;
        for (int y = 0; y < heightOrig; y++) {
            for (int x = 0; x < widthOrig; x++) {
                int pixel = imagenOriginal.getRGB(x, y);
                int gray = convertirAGris(pixel);
                tda.getOriginales()[cont][index++] = gray;
            }
        }

        
    }

    public void tomarValores(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int totalValores = width * height;

        mN = new int[1][totalValores];
        int index = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = img.getRGB(x, y);
                int grayValue = convertirAGris(pixel); 
                mN[0][index++] = grayValue;
            }
        }
        System.out.println("Matriz a reconocer "+mN[0].length);
        
    }

    private int convertirAGris(int pixel) {
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;
        return (int) (0.299 * red + 0.587 * green + 0.114 * blue);
    }

    public void entrenarRed(int[][] tk, double niu, double alfa, double rmsDeseado, int maxEpocas, int nOcultas, double rMen, double rMax) {
        Operaciones op = new Operaciones();
        int epoca = 0;
        double error = Double.MAX_VALUE;

        int[][] matriz = tda.getnNormalizados();

        double[][] patronesNormalizados = op.NormalizarPatrones(matriz);
        int nEntradas = patronesNormalizados[0].length;
        int nSalidas = tk[0].length;

        tda.setNormalizados(patronesNormalizados);
        tda.setWij(op.inicializarMatriz(nEntradas, nOcultas, rMen, rMax));
        tda.setDeltawij(op.inicializarMatrizCeros(nEntradas, nOcultas));
        tda.setWjk(op.inicializarMatriz(nOcultas, nSalidas, rMen, rMax));
        tda.setDeltawjk(op.inicializarMatrizCeros(nOcultas, nSalidas));
        tda.setThetaj(op.inicializarMatriz(nOcultas, 1, rMen, rMax));
        tda.setDeltathetaj(op.inicializarMatrizCeros(nOcultas, 1));
        tda.setThetak(op.inicializarMatriz(nSalidas, 1, rMen, rMax));
        tda.setDeltathetak(op.inicializarMatrizCeros(nSalidas, 1));

        while (epoca < maxEpocas && error > rmsDeseado) {
            double[][] rj = op.calcularRj(tda.getNormalizados(), tda.getWij(), tda.getThetaj());
            double[][] sj = op.calcularS(rj);
            double[][] rk = op.calcularRk(sj, tda.getWjk(), tda.getThetak(), nSalidas);
            double[][] sk = op.calcularS(rk);

            error = op.encontrarE(tk, sk);
            tda.addVrms(error);
            System.out.printf("Época %d - RMS: %.6f%n", epoca + 1, error);

            double[][] errork = op.errorK(tk, sk);
            tda.setErrork(errork);

            double[][][] ajusteWjk = op.adaptarP(errork, sj, tda.getDeltawjk(), tda.getWjk());
            tda.setDeltawjk(ajusteWjk[0]);
            tda.setWjk(ajusteWjk[1]);

            double[][][] ajusteThetak = op.thetak(errork, tda.getDeltathetak(), tda.getThetak());
            tda.setDeltathetak(ajusteThetak[0]);
            tda.setThetak(ajusteThetak[1]);

            op.errorJ(sj, errork, tda.getNormalizados(), tda.getWjk(), niu, alfa,
                      tda.getDeltawij(), tda.getWij(), tda.getDeltathetaj(), tda.getThetaj());

            epoca++;
        }

        System.out.println("Entrenamiento finalizado. Épocas: " + epoca);
    }

    public int predecir(BufferedImage img, int nOcultas, double rMen, double rMax, int[][] tk) {
        tomarValores(img);
        boolean iguales = false;
        int fila = 0;

        Operaciones op = new Operaciones();
        

        double[][] patronesNormalizados = op.NormalizarPatrones(mN);
        int nEntradas = patronesNormalizados[0].length;
        int nSalidas = tk[0].length;

        double[][] wij = tda.getWij();
        double[][] thetaj = tda.getThetaj();
        double[][] wjk = tda.getWjk();
        double[][] thetak = tda.getThetak();

        double[][] rj = op.calcularRj(patronesNormalizados, wij, thetaj);
        double[][] sj = op.calcularS(rj);
        double[][] rk = op.calcularRk(sj, wjk, thetak, nSalidas);
        double[][] sk = op.calcularS(rk);

        System.out.println("Salidas sk:");
        for (int i = 0; i < nSalidas; i++) {
            System.out.printf("sk[%d] = %.4f%n", i, sk[0][i]);
        }

        int[][] tkPredecido = new int[1][nSalidas];
        System.out.println("Salidas binarizadas:");
        for (int i = 0; i < nSalidas; i++) {
            int salidaBinaria = (sk[0][i] >= 0.5) ? 1 : 0;
            tkPredecido[0][i] = salidaBinaria;
            System.out.println("Binario[" + i + "] = " + salidaBinaria);
        }

        for (int i = 0; i < tk.length; i++) {
            iguales = true;
            for (int j = 0; j < tk[0].length; j++) {
                if (tkPredecido[0][j] != tk[i][j]) {
                    iguales = false;
                    break;
                }
            }
            if (iguales) {
                System.out.println("Se parece al patrón P" + (i + 1));
                fila = i;
                break;
            }
        }

        if (iguales) {
            return fila;
        } else {
            System.out.println("No se reconoce el patrón.");
            return -1;
        }
    }
    public int predecir2(Tdamatriz tda, int nOcultas, double rMen, double rMax, int[][] tk, int filaP) {

        boolean iguales = false;
        int fila = 0;

        Operaciones op = new Operaciones();

        double[][] patronesNormalizados = new double[1][];
        patronesNormalizados[0] = tda.getNormalizados()[filaP];

        int nEntradas = patronesNormalizados[0].length;
        int nSalidas = tk[0].length;

        double[][] wij = tda.getWij();
        double[][] thetaj = tda.getThetaj();
        double[][] wjk = tda.getWjk();
        double[][] thetak = tda.getThetak();

        double[][] rj = op.calcularRj(patronesNormalizados, wij, thetaj);
        double[][] sj = op.calcularS(rj);
        double[][] rk = op.calcularRk(sj, wjk, thetak, nSalidas);
        double[][] sk = op.calcularS(rk);

        System.out.println("Salidas sk:");
        for (int i = 0; i < nSalidas; i++) {
            System.out.printf("sk[%d] = %.4f%n", i, sk[0][i]);
        }

        int[][] tkPredecido = new int[1][nSalidas];
        System.out.println("Salidas binarizadas:");
        for (int i = 0; i < nSalidas; i++) {
            int salidaBinaria = (sk[0][i] >= 0.5) ? 1 : 0;
            tkPredecido[0][i] = salidaBinaria;
            System.out.println("Binario[" + i + "] = " + salidaBinaria);
        }

        for (int i = 0; i < tk.length; i++) {
            iguales = true;
            for (int j = 0; j < tk[0].length; j++) {
                if (tkPredecido[0][j] != tk[i][j]) {
                    iguales = false;
                    break;
                }
            }
            if (iguales) {
                System.out.println("Se parece al patrón P" + (i + 1));
                fila = i;
                break;
            }
        }

        if (iguales) {
            return fila;
        } else {
            System.out.println("No se reconoce el patrón.");
            return -1;
        }
    }

    
    
    public Tdamatriz getTda() {
        return this.tda;
    }
}
