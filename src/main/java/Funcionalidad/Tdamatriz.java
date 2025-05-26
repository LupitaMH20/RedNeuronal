package Funcionalidad;
public class Tdamatriz {
    private double[][] normalizados;
    private double[][] wij;
    private double[][] wjk;
    private double[][] thetaj;
    private double[][] thetak;
    private double[][] deltawij;
    private double[][] deltawjk;
    private double[][] deltathetaj;
    private double[][] deltathetak;
    private double[][] errorj;
    private double[][] errork;
    private double[] vrms;
    private int[][] tks;

    // Getters y Setters
    public double[][] getNormalizados() { return normalizados; }
    public void setNormalizados(double[][] normalizados) { this.normalizados = normalizados; }

    public double[][] getWij() { return wij; }
    public void setWij(double[][] wij) { this.wij = wij; }

    public double[][] getWjk() { return wjk; }
    public void setWjk(double[][] wjk) { this.wjk = wjk; }

    public double[][] getThetaj() { return thetaj; }
    public void setThetaj(double[][] thetaj) { this.thetaj = thetaj; }

    public double[][] getThetak() { return thetak; }
    public void setThetak(double[][] thetak) { this.thetak = thetak; }

    public double[][] getDeltawij() { return deltawij; }
    public void setDeltawij(double[][] deltawij) { this.deltawij = deltawij; }

    public double[][] getDeltawjk() { return deltawjk; }
    public void setDeltawjk(double[][] deltawjk) { this.deltawjk = deltawjk; }

    public double[][] getDeltathetaj() { return deltathetaj; }
    public void setDeltathetaj(double[][] deltathetaj) { this.deltathetaj = deltathetaj; }

    public double[][] getDeltathetak() { return deltathetak; }
    public void setDeltathetak(double[][] deltathetak) { this.deltathetak = deltathetak; }

    public double[][] getErrorj() { return errorj; }
    public void setErrorj(double[][] errorj) { this.errorj = errorj; }

    public double[][] getErrork() { return errork; }
    public void setErrork(double[][] errork) { this.errork = errork; }

    public double[] getVrms() { return vrms; }
    public void setVrms(double[] vrms) { this.vrms = vrms; }
    
    
    
    public void addVrms(double nuevoError) {
        if (vrms == null) {
            vrms = new double[]{nuevoError};
        } else {
            double[] nuevoArray = new double[vrms.length + 1];
            System.arraycopy(vrms, 0, nuevoArray, 0, vrms.length);
            nuevoArray[vrms.length] = nuevoError;
            vrms = nuevoArray;
        }
    }


    public int[][] getTks() { return tks; }
    public void setTks(int[][] tks) { this.tks = tks; }

    // Método para imprimir un resumen de qué datos están inicializados
    public void imprimirResumen() {
        System.out.println("Resumen de RedNeuronalData:");
        System.out.println("normalizados: " + (normalizados != null));
        System.out.println("wij: " + (wij != null));
        System.out.println("wjk: " + (wjk != null));
        System.out.println("thetaj: " + (thetaj != null));
        System.out.println("thetak: " + (thetak != null));
        System.out.println("deltawij: " + (deltawij != null));
        System.out.println("deltawjk: " + (deltawjk != null));
        System.out.println("deltathetaj: " + (deltathetaj != null));
        System.out.println("deltathetak: " + (deltathetak != null));
        System.out.println("errorj: " + (errorj != null));
        System.out.println("errork: " + (errork != null));
        System.out.println("vrms: " + (vrms != null));
        System.out.println("tks: " + (tks != null));
    }
}