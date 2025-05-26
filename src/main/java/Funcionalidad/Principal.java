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

    double rms, rmax, rmin;
    int nEntradas, nSalidas, nOcultas;

    // Método para inicializar parámetros de la red neuronal
    public void inicializarParametros(double rms, double rangoMin, double rangoMax, int numEntradas, int numOcultas) {
        this.rms = rms;
        this.rmin = rangoMin;
        this.rmax = rangoMax;
        this.nEntradas = numEntradas;
        this.nOcultas = numOcultas;

        // Determinar el número de salidas según las entradas
        if (nEntradas == 5 || nEntradas == 4) {
            this.nSalidas = 3;
        } else if (nEntradas == 3) {
            this.nSalidas = 2;
        } else {
            this.nSalidas = 1; // Por si acaso quieres un valor por defecto
        }
    }
    
    public void inicializarPesos() {
        Operaciones ob=new Operaciones();
        
        wij = ob.inicializarMatriz(nEntradas, nOcultas,rmax,rmin);
        deltawij = ob.inicializarMatrizCeros(nEntradas, nOcultas);

        wjk = ob.inicializarMatriz(nOcultas, nSalidas,rmax,rmin);
        deltawjk = ob.inicializarMatrizCeros(nOcultas, nSalidas);
        
        thetaj=ob.inicializarMatriz(2,1,rmax,rmin);
        deltathetaj=ob.inicializarMatrizCeros(2,1);
        
        thetak=ob.inicializarMatriz(2,1,rmax,rmin);
        deltathetak=ob.inicializarMatrizCeros(2,1);
    }
    

    
    public void Entrenar() {
        Operaciones ob=new Operaciones();
        
        // Datos tomados de la imagen
        double[][] patronesNormalizados = {
            {0.2, 1, 0.82},
            {0.6, 0.22, 1},
            {1, 0.11, 0.35}
        };

        double[][] wij = {
            {-0.257, 0.129},
            {-0.271, -0.044},
            {-0.089, 0.012}
        };

        double[][] wjk = {
            {-0.033, 0.004},
            {0.083, 0.189},
        };
        
        double[] thetaj = {0.255, 0.176};
        double[] thetak = {-0.107, -0.298};

        double[][] rj = ob.calcularRj(patronesNormalizados, wij, thetaj);
        
        double[][] sj = ob.calcularS(rj);
        
        double[][] rk = ob.calcularRk(sj,wjk,thetak,2);
        
        double[][] sk = ob.calcularS(rk);
        
        
        // Mostrar resultados
        for (int p = 0; p < rj.length; p++) {
            for (int j = 0; j < rj[0].length; j++) {
                System.out.println("Rj" + p + j + " = " + rj[p][j]);
            }
        }
        
         // Mostrar resultados
        for (int p = 0; p < sj.length; p++) {
            for (int j = 0; j < sj[0].length; j++) {
                System.out.println("Sj" + p + j + " = " + sj[p][j]);
            }
        }
        
         // Mostrar resultados
        for (int p = 0; p < rk.length; p++) {
            for (int j = 0; j < rk[0].length; j++) {
                System.out.println("Rk" + p + j + " = " + rk[p][j]);
            }
        }
        
        // Mostrar resultados
        for (int p = 0; p < sk.length; p++) {
            for (int j = 0; j < sk[0].length; j++) {
                System.out.println("Sk" + p + j + " = " + sk[p][j]);
            }
        }
        
    }
    
}
