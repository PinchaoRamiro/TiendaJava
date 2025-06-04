package com.tiendajava.ui.utils;

import java.awt.Color;

public class UITheme {

    // === Background & Layout ===
    public static Color getPrimaryColor() {
        return new Color(30, 30, 31); // Fondo principal: gris azulado oscuro
    }

    public static Color getSecondaryColor() {
        return new Color(38, 39, 41); // Elementos secundarios (sidebar, cards)
    }

    public static Color getTertiaryColor() {
        // mas claro que el secundario que resalte un poco mas claro
        return new Color(50, 53, 60); // Elementos terciarios (divisiones, bordes)
    }

    public static Color getBorderCardsColor() {
        return new Color(223, 240, 255); // Bordes de tarjetas y contenedores
    }

    public static Color getBackgroundContrast() {
        return new Color( 52, 56, 78  ); // Para overlays o modales con contraste claro
    }

    // === Text ===
    public static Color getTextColor() {
        return new Color(235, 235, 235); // Texto principal, muy legible sobre fondo oscuro
    }

    public static Color getBorderColor() {
        return new Color(85, 90, 100); // Gris para bordes y divisiones
    }

    // === Buttons ===
    public static Color getPrimaryButtonColor() {
        return new Color(0, 168, 232); // Azul moderno (call-to-action principal)
    }

    public static Color getSecodaryButtonColor() {
        return new Color(112, 122, 137); // Gris neutro para botones secundarios
    }

    public static Color getButtonColor() {
        return new Color(0, 123, 255); // Azul para botones de acción
    }

    public static Color getDangerColor() {
        return new Color(231, 76, 60); // Rojo fuerte para acciones destructivas
    }

    // === Focus & Accents ===
    public static Color getFocusColor() {
        return new Color(255, 195, 0); // Amarillo dorado para enfoque de inputs
    }

    // === Alerts & Status ===
    public static Color getErrorColor() {
        return new Color(220, 53, 69); // Rojo más suave que el de Danger
    }

    public static Color getWarningColor() {
        return new Color(255, 193, 7); // Amarillo suave para advertencias
    }

    public static Color getInfoColor() {
        return new Color(0, 123, 255); // Azul claro para información útil
    }

    public static Color getSuccessColor() {
        return new Color(40, 167, 69); // Verde confiable para éxito
    }

    public static Color getAccentColor() {
        return new Color(255, 87, 34); // Naranja vibrante para acentos
    }
}