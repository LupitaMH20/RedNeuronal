package Funcionalidad;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JPanel;

public class Operaciones {

//    public double[][] NormalizarPatrones(int[][] patrones) {
//        double[][] normalizados = new double[patrones.length][patrones[0].length];
//        for (int i = 0; i < patrones.length; i++) {
//            for (int j = 0; j < patrones[0].length; j++) {
//                normalizados[i][j] = patrones[i][j] / 255.0; 
//            }
//        }
//        return normalizados;
//    }
    
    
    //Metodo para normalizar los patrones 
    public double[][] NormalizarPatrones(int[][] matrizn) {
        int filas = matrizn.length;
        int columnas = matrizn[0].length;
        double[][] normalizados = new double[filas][columnas];

        for (int j = 0; j < columnas; j++) {
            int max = matrizn[0][j];
            int vi = 0;
            for (int i = 1; i < filas; i++) {
                if (matrizn[i][j] > max) {
                    max = matrizn[i][j];
                    vi = i;
                }
            }

            // Evitar división por cero
            if (max == 0) {
                max = 1;
            }

            // Dividir todos los valores de la columna j entre el máximo
            for (int i = 0; i < filas; i++) {
                normalizados[i][j] = (double) matrizn[i][j] / max;
            }

        }
        return normalizados;
    }

    public double[][] inicializarMatriz(int filas, int columnas, double rmax, double rmin) {
        double[][] matriz = new double[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = Math.round((Math.random() * (rmax - rmin) + rmin) * 10000.0) / 10000.0;
            }
        }
        return matriz;
    }

    public double[][] inicializarMatrizCeros(int filas, int columnas) {
        return new double[filas][columnas]; // Ya inicializa con ceros por defecto
    }

    public double[][] calcularRj(double[][] patrones, double[][] wij, double[][] thetaj) {
        int nPatrones = patrones.length;
        int nEntradas = patrones[0].length;
        int nOcultas = wij[0].length;
        double[][] rj = new double[nPatrones][nOcultas];

        for (int p = 0; p < nPatrones; p++) {
            for (int j = 0; j < nOcultas; j++) {
                double suma = 0;
                for (int i = 0; i < nEntradas; i++) {
                    suma += patrones[p][i] * wij[i][j];
                }
                rj[p][j] = ((suma + thetaj[j][0]) * 10000.0) / 10000.0;
            }
        }

        return rj;
    }

    public double[][] calcularS(double[][] rj) {
        int filas = rj.length;
        int columnas = rj[0].length;

        double[][] s = new double[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                s[i][j] = (1 / (1 + Math.exp(-rj[i][j])));
            }
        }

        return s;
    }

    public double[][] calcularRk(double[][] sj, double[][] wjk, double[][] thetak, int nS) {
        int nPatrones = sj.length;
        int nEntradas = sj[0].length;
        int nSalidas = nS;

        double[][] rk = new double[nPatrones][nSalidas];

        for (int p = 0; p < nPatrones; p++) {
            for (int j = 0; j < nSalidas; j++) {
                double suma = 0;
                for (int i = 0; i < nEntradas; i++) {
                    suma += sj[p][i] * wjk[i][j];
                }
                rk[p][j] = ((suma + thetak[j][0]) * 10000.0) / 10000.0;
            }
        }

        return rk;
    }


    public double encontrarE(int[][] tk, double[][] sk) {
        double errorRms;
        double suma = 0;
        int nPatrones = tk.length;
        int nSalidas = tk[0].length;

        for (int i = 0; i < nPatrones; i++) {
            for (int j = 0; j < nSalidas; j++) {
                suma = suma + (Math.pow((tk[i][j] - (sk[i][j])), 2));
            }

        }

        errorRms = Math.sqrt((suma / (nPatrones * nSalidas)));

        return errorRms;
    }

    public double[][] errorK(int[][] tk, double[][] sk) {
        int nPatrones = tk.length;
        int nSalidas = tk[0].length;
        double[][] errork = new double[nPatrones][nSalidas];

        for (int i = 0; i < nPatrones; i++) {
            for (int j = 0; j < nSalidas; j++) {
                errork[i][j] = ((tk[i][j] - sk[i][j]) * (sk[i][j] * (1 - sk[i][j])));
            }
        }
        return errork;
    }

    public double[][][] adaptarP(double[][] errorK, double[][] sj, double[][] deltawjkAnterior, double[][] wjkanterior) {
        int nPatrones = errorK.length;
        int nOcultas = sj[0].length;
        int nSalidas = errorK[0].length;

        // Arreglo para los nuevos deltawjk acumulados
        double[][] deltawjk = new double[nOcultas][nSalidas];
        double[][] wjk = new double[nOcultas][nSalidas];

        double niu = 0.35;
        double alfa = 0.2;

        // Recorre todos los patrones
        for (int p = 0; p < nPatrones; p++) {
            for (int i = 0; i < nOcultas; i++) {
                for (int j = 0; j < nSalidas; j++) {
                    // Calcula el nuevo delta para este patrón y conexión
                    double delta = (niu * errorK[p][j] * sj[p][i]) + (alfa * deltawjkAnterior[i][j]);
                    deltawjk[i][j] = delta;
                    wjk[i][j] = wjkanterior[i][j] + deltawjk[i][j];
                }
            }

            for (int i = 0; i < nOcultas; i++) {
                for (int j = 0; j < nSalidas; j++) {
                    deltawjkAnterior[i][j] = deltawjk[i][j];
                    wjkanterior[i][j] = wjk[i][j];
                }
            }
        }

        // Retorna ambas matrices en un arreglo 3D
        return new double[][][]{deltawjk, wjk};
    }


    public double[][][] thetak(double[][] errork, double[][] deltathetakanterior, double[][] thetakanterior) {
        int nSalidas = errork[0].length; // Calcula el número real de salidas
        double[][] deltathetak = new double[nSalidas][1];
        double[][] thetak = new double[nSalidas][1];

        int nPatrones = errork.length;
        double niu = 0.35;
        double alfa = 0.2;

        for (int p = 0; p < nPatrones; p++) {
            for (int i = 0; i < nSalidas; i++) {
                double delta = (niu * errork[p][i]) + (alfa * deltathetakanterior[i][0]);
                deltathetak[i][0] = delta;
                thetak[i][0] = thetakanterior[i][0] + deltathetak[i][0];

                // Acumuladores para la siguiente época
                deltathetakanterior[i][0] = deltathetak[i][0];
                thetakanterior[i][0] = thetak[i][0];
            }
        }

        return new double[][][]{deltathetak, thetak};
    }

    
    public void errorJ(double[][] sj, double[][] errork, double[][] patronesNormalizados, double[][] wjk, double niu, double alfa,
            double[][] deltawijanterior, double[][] wijanterior, double[][] deltathetajanterior, double[][] thetajanterior) {

        int nPatrones = patronesNormalizados.length;
        int nEntradas = patronesNormalizados[0].length;
        int nOcultas = sj[0].length;
        int nSalidas = errork[0].length;

        double[][] errorj = new double[nPatrones][nOcultas];
        double[][] deltawij = new double[nEntradas][nOcultas];
        double[][] wij = new double[nEntradas][nOcultas];
        double[][] deltathetaj = new double[nOcultas][1];
        double[][] thetaj = new double[nOcultas][1];

        for (int p = 0; p < nPatrones; p++) {
            for (int j = 0; j < nOcultas; j++) {
                double sumak = 0.0;
                for (int k = 0; k < nSalidas; k++) {
                    sumak += errork[p][k] * wjk[j][k];
                }

                // Calcula el error de la neurona oculta j para el patrón p
                errorj[p][j] = sj[p][j] * (1 - sj[p][j]) * sumak;

                // Actualiza pesos de entrada a la capa oculta
                for (int i = 0; i < nEntradas; i++) {
                    deltawij[i][j] = niu * errorj[p][j] * patronesNormalizados[p][i] + alfa * deltawijanterior[i][j];
                    wij[i][j] = wijanterior[i][j] + deltawij[i][j];
                }

                // Actualiza el sesgo (theta) para la neurona j
                deltathetaj[j][0] = niu * errorj[p][j] + alfa * deltathetajanterior[j][0];
                thetaj[j][0] = thetajanterior[j][0] + deltathetaj[j][0];
            }
        }

      
    }

    // Método auxiliar para imprimir cualquier matriz 2D
    public void imprimirMatriz(double[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.print(matriz[i][j] + "\t");
            }
            System.out.println();
        }
    }

    // Método que genera una matriz 5x5 con valores aleatorios entre 0 y 255
    //Filas i
    //Columnas j
    public int[][] generarMatriz5x5Aleatoria() {
        int[][] matriz = new int[3][3];
        Random rand = new Random();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matriz[i][j] = rand.nextInt(256); // Genera valor entre 0 y 255
            }
        }

        return matriz;
    }

}
