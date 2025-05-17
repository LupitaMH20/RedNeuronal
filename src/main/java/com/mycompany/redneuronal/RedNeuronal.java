/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.redneuronal;
import Funcionalidad.Operaciones;
import Funcionalidad.Principal;
import java.util.Scanner;
/**
 *
 * @author Maria Guadalupe MH
 */
public class RedNeuronal {

    public static void main(String[] args) {
        Operaciones ob= new Operaciones();
        Principal obP=new Principal();
        Scanner leer=new Scanner(System.in);
     
        obP.Entrenar();
        
    }
}
