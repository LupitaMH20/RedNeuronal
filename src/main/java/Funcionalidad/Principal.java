package Funcionalidad;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class Principal {

    private Tdamatriz tda;

    public Principal() {
        this.tda = new Tdamatriz();
    }

    int[][] mN;
    int[][] matriz;

    public void valoresImg(int cont, BufferedImage imagen) {
        int width = imagen.getWidth();
        int height = imagen.getHeight();
        int totalValores = width * height;

        // Inicializar si es la primera imagen
        if (tda.getnNormalizados() == null) {
            tda.inicializar(width, height);
        }

        // Verificar que cont no exceda el límite
        if (cont >= tda.getnNormalizados().length) {
            System.err.println("Error: Índice de imagen fuera de rango: " + cont);
            return;
        }

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = imagen.getRGB(x, y);

                // Extraer componentes RGB
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // Convertir a escala de grises o usar promedio
                int grayValue = (int) (0.299 * red + 0.587 * green + 0.114 * blue);

                // Verificar límites del array
                if (index < tda.getnNormalizados()[cont].length) {
                    tda.getnNormalizados()[cont][index] = grayValue;
                    index++;
                }
            }
        }

        System.out.println("Imagen " + cont + " procesada correctamente. Valores guardados: " + index);
    }

    public void entrenarRed(int[][] tk, double niu, double alfa, double rmsDeseado, int maxEpocas, int nOcultas, double rMen, double rMax) {
        Operaciones op = new Operaciones();
        int epoca = 0;
        double error = Double.MAX_VALUE;

        int[][] matriz = tda.getnNormalizados();

//        for (int i = 0; i < matriz.length; i++) {
//            for (int j = 0; j < matriz[0].length; j++) {
//                System.out.print(matriz[i][j] + "\t");
//            }
//            System.out.println(); // Salto de línea por fila
//        }
        System.out.println("Matriz filas " + matriz.length);
        System.out.println("Matriz Columnas " + matriz[0].length);

        // Normalización y configuración inicial
        double[][] patronesNormalizados = op.NormalizarPatrones(tda.getnNormalizados());
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
            // FORWARD
            double[][] rj = op.calcularRj(tda.getNormalizados(), tda.getWij(), tda.getThetaj());
            double[][] sj = op.calcularS(rj);
            double[][] rk = op.calcularRk(sj, tda.getWjk(), tda.getThetak(), nSalidas);
            double[][] sk = op.calcularS(rk);

            // ERROR
            error = op.encontrarE(tk, sk);
            //tda.setVrms(new double[]{error});  // Solo el actual, no histórico
            tda.addVrms(error);
            System.out.printf("Época %d - RMS actual: %.6f%n", epoca + 1, error);

            // BACKWARD
            double[][] errork = op.errorK(tk, sk);
            tda.setErrork(errork); // Actualiza error de salida

            double[][][] ajusteWjk = op.adaptarP(errork, sj, tda.getDeltawjk(), tda.getWjk());
            tda.setDeltawjk(ajusteWjk[0]);
            tda.setWjk(ajusteWjk[1]);

            double[][][] ajusteThetak = op.thetak(errork, tda.getDeltathetak(), tda.getThetak());
            tda.setDeltathetak(ajusteThetak[0]);
            tda.setThetak(ajusteThetak[1]);

            // Adaptar capa oculta y actualizar los pesos/sesgos
            op.errorJ(sj, errork, tda.getNormalizados(), tda.getWjk(), niu, alfa,
                    tda.getDeltawij(), tda.getWij(), tda.getDeltathetaj(), tda.getThetaj());

            epoca++;
        }

        System.out.println("Entrenamiento finalizado. Épocas: " + epoca);
    }

    public void predecir(BufferedImage img, int nOcultas, double rMen, double rMax, int[][] tk) {
        // Paso 1: Convertir imagen a matriz de entrada (mN)
        tomarValores(img);

        Operaciones op = new Operaciones();

        // Paso 2: Normalizar
        double[][] patronesNormalizados = op.NormalizarPatrones(mN);
        int nEntradas = patronesNormalizados[0].length;
        int nSalidas = 3; // ¡Ahora tienes 3 salidas!

        // Paso 3: Inicializar pesos y umbrales (normalmente cargarías los entrenados aquí)
        double[][] wij = tda.getWij();
        double[][] thetaj = tda.getThetaj();

        double[][] wjk = tda.getWjk();
        double[][] thetak = tda.getThetak();

        // Paso 4: Cálculo hacia adelante
        double[][] rj = op.calcularRj(patronesNormalizados, wij, thetaj);
        double[][] sj = op.calcularS(rj);
        double[][] rk = op.calcularRk(sj, wjk, thetak, nSalidas);
        double[][] sk = op.calcularS(rk); // [1][3]

        // Paso 5: Mostrar resultados de sk
        System.out.println("Salidas sk:");
        for (int i = 0; i < nSalidas; i++) {
            System.out.printf("sk[%d] = %.4f%n", i, sk[0][i]);
        }

        // Paso 6: Convertir a binario simple y guardar en tkPredecido
        int[][] tkPredecido = new int[1][nSalidas]; // Matriz de 1x3

        System.out.println("Salidas binarizadas:");
        for (int i = 0; i < nSalidas; i++) {
            int salidaBinaria = (sk[0][i] >= 0.5) ? 1 : 0;
            tkPredecido[0][i] = salidaBinaria;
            System.out.println("Binario[" + i + "] = " + salidaBinaria);
        }
        // Paso 7: Determinar clase esperada y clase predicha

        for (int i = 0; i < tk.length; i++) {
            boolean iguales = true;

            for (int j = 0; j < tk[0].length; j++) {
                if (tkPredecido[0][j] != tk[i][j]) {
                    iguales = false;
                    break;
                }
            }

            if (iguales) {
                System.out.println("Se parece al patrón P" + (i + 1));
                break; // si ya encontró coincidencia, se puede salir del for
            } else {
                System.out.println("No se reconoce el patron");
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

                // Extraer componentes RGB
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // Convertir a escala de grises o usar promedio
                int grayValue = (int) (0.299 * red + 0.587 * green + 0.114 * blue);

                mN[0][index] = grayValue;
                index++;

            }
        }

    }
    
    public Tdamatriz getTda() {
        return this.tda;
    }

   

}
