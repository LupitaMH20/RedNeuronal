/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.redneuronal;
<<<<<<< HEAD

import Interfaz.Gui;

=======
import Funcionalidad.Operaciones;
import Funcionalidad.Principal;
import java.util.Scanner;
>>>>>>> 65022d00660b062ffb552e7be71f2f93718e8c71
/**
 *
 * @author Maria Guadalupe MH
 */
public class RedNeuronal {

    public static void main(String[] args) {
<<<<<<< HEAD
        Gui ob=new Gui();
        ob.setVisible(true);
=======
        Operaciones ob= new Operaciones();
        Principal obP=new Principal();
        Scanner leer=new Scanner(System.in);
     
        obP.Entrenar();
        
>>>>>>> 65022d00660b062ffb552e7be71f2f93718e8c71
    }
}
