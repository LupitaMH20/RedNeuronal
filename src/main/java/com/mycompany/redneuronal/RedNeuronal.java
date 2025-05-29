package com.mycompany.redneuronal;

import Funcionalidad.Operaciones;
import Funcionalidad.Principal;
import Funcionalidad.Tdamatriz;

import Interfaz.Gui;
import java.util.Scanner;

/**
 *
 * @author Maria Guadalupe MH
 */
public class RedNeuronal {

    public static void main(String[] args) {

        Gui ob = new Gui();
       ob.setVisible(true);

//        Principal red = new Principal();
        Tdamatriz tda = new Tdamatriz();
//
//        int[][] patrones = {
//            {10, 90, 70},
//            {30, 20, 85},
//            {50, 10, 30}
//        };
//
//        int[][] tk = {
//            {0, 1},
//            {1, 0},
//            {1, 1}
//        };
//
//        tda.setnNormalizados(patrones);
//        
//        red.entrenarRed(tk, 0.35, 0.2, 0.1, 10000000, 3);
//
//        // Ver estado final de la red
         tda.imprimirResumen();
//        System.out.println("Último RMS: " + tda.getVrms()[0]);
     
    }
}
