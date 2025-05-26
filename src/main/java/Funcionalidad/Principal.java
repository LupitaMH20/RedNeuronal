package Funcionalidad;

public class Principal 
{
    double[][] normalizados;
    double[][] wij;
    double[][] wjk;
    double[][] thetaj;
    double[][] thetak;
    double[][] deltawij;
    double[][] deltawjk;
    double[][] deltathetaj;
    double[][] deltathetak;
    double[][] errorj;
    double[][] errork;
    double[] vrms;
    double[][] tks;
    
    double rms, rmax, rmin;
    int nEntradas, nSalidas, nOcultas;

//    // Método para inicializar parámetros de la red neuronal
//    public void inicializarParametros(double rms, double rangoMin, double rangoMax, int numEntradas, int numOcultas) {
//        this.rms = rms;
//        this.rmin = rangoMin;
//        this.rmax = rangoMax;
//        this.nEntradas = numEntradas;
//        this.nOcultas = numOcultas;
//
//        // Determinar el número de salidas según las entradas
//        if (nEntradas == 5 || nEntradas == 4) {
//            this.nSalidas = 3;
//        } else if (nEntradas == 3) {
//            this.nSalidas = 2;
//        } else {
//            this.nSalidas = 1; // Por si acaso quieres un valor por defecto
//        }
//    }
    
    
    

    
    
    public void entrenarRed(int[][] patronesOriginales, int[][] tk, double niu, double alfa, double rmsDeseado, int maxEpocas, Tdamatriz tda, int nOcultas) {
        Operaciones op = new Operaciones();
        int epoca = 0;
        double error = Double.MAX_VALUE;

        // Normalización y configuración inicial
        double[][] patronesNormalizados = op.NormalizarPatrones(patronesOriginales);
        int nEntradas = patronesNormalizados[0].length;
        int nSalidas = tk[0].length;

        tda.setNormalizados(patronesNormalizados);
        tda.setWij(op.inicializarMatriz(nEntradas, nOcultas, -0.3, 0.3));
        tda.setDeltawij(op.inicializarMatrizCeros(nEntradas, nOcultas));
        tda.setWjk(op.inicializarMatriz(nOcultas, nSalidas, -0.3, 0.3));
        tda.setDeltawjk(op.inicializarMatrizCeros(nOcultas, nSalidas));
        tda.setThetaj(op.inicializarMatriz(nOcultas, 1, -0.3, 0.3));
        tda.setDeltathetaj(op.inicializarMatrizCeros(nOcultas, 1));
        tda.setThetak(op.inicializarMatriz(nSalidas, 1, -0.3, 0.3));
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

    
    
}
