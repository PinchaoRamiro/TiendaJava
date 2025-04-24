package com.tiendajava.ui.utils;

import java.awt.Font;

public class Fonts {
    private static final String FONT_FAMILY = "Poppins"; // Puedes usar también "Poppins", "Roboto", etc.

    // === TITULARES Y SECCIONES ===
    public static final Font TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 22);
    public static final Font SECTION_HEADER_FONT = new Font(FONT_FAMILY, Font.BOLD, 18);
    public static final Font SUBTITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 16);

    // === TEXTO GENERAL ===
    public static final Font NORMAL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 12);

    // === BOTONES Y ACCIONES ===
    public static final Font BUTTON_FONT = new Font(FONT_FAMILY, Font.BOLD, 15);

    // === MENSAJES Y ESTADOS ===
    public static final Font ERROR_FONT = new Font(FONT_FAMILY, Font.ITALIC, 14);
    public static final Font WARNING_FONT = new Font(FONT_FAMILY, Font.BOLD | Font.ITALIC, 14);
    public static final Font INFO_FONT = NORMAL_FONT;
    public static final Font SUCCESS_FONT = new Font(FONT_FAMILY, Font.BOLD, 14);

    // === OTROS USOS ===
    public static final Font BOLD_NFONT = new Font(FONT_FAMILY, Font.BOLD, 14);
}
