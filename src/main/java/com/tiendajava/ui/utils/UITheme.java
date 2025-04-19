// UITheme.java
package com.tiendajava.ui.utils;

import java.awt.Color;

public class UITheme {


    public static Color getPrimaryColor() {
        return new Color(30, 30, 30); // Gris más claro  para el fondo principal 
    }

    public static Color getSecondaryColor() {
        return new Color(25, 25, 25); // Gris oscuro para elementos secundarios
    }

    public static Color getTertiaryColor() {
        return new Color(68, 74, 80); // Gris aún más claro para elementos terciarios
    }

    public static Color getBorderColor() {
        return new Color(143, 147, 143); // Color claro para border
    }

    public static Color getTextColor() {
        return new Color(220, 220, 220); // Blanco suave para el texto
    }

    public static Color getButtonColor(){
        return new Color(73, 93, 75); // Verde para boton
    }

    public static Color getDangerColor(){
        return new Color(200, 60, 60); // rojo para Ganger
    }

    public static Color getSecodaryButtonColor(){
        return new Color(100, 100, 100); 
    }

    public static Color getPrimaryButtonColor(){
        return new Color(70, 130, 180);
    }

    public static Color getFocusColor() {
        return new Color(0x004D61); // Verde azulado oscuro para foco
    }

    public static Color getErrorColor() {
        return new Color(231, 76, 60); // Rojo para mensajes de error
    }

    public static Color getWarningColor() {
        return new Color(245, 183, 65); // Naranja para mensajes de advertencia
    }

    public static Color getInfoColor() {
        return new Color(46, 204, 113); // Verde para mensajes informativos
    }

    public static Color getSuccessColor() {
        return new Color(52, 152, 219); // Azul para mensajes de éxito
    }
}