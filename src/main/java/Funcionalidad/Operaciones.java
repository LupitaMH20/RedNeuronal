package Funcionalidad;

import java.util.Random;

public class Operaciones {

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

    
    public double[][] inicializarMatriz(int filas, int columnas,double rmax,double rmin) {
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
    
    
    public double[][] calcularRj(double[][] patrones, double[][] wij, double[] thetaj) {
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
                rj[p][j] = ((suma + thetaj[j]) * 10000.0) / 10000.0;
            }
        }

        return rj;
    }
    
    public double [][] calcularS(double [][] rj)
    {
        int filas=rj.length;
        int columnas=rj[0].length;
                
        double[][] s = new double[filas][columnas];
        
        for(int i=0;i<filas;i++)
        {
            for (int j = 0; j < columnas; j++) {
                s[i][j]=(1/(1+Math.exp(-rj[i][j])));
            }
        }
        
        return s;  
    }

    public double[][] calcularRk(double[][] sj, double[][] wjk, double[] thetak, int nS) {
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
                rk[p][j] = ((suma + thetak[j]) * 10000.0) / 10000.0;
            }
        }

        return rk;
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
