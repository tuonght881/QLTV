package com.raven.component;

import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.Color;
import javax.swing.JPanel;

public class Form_Flatlaf extends JPanel {

    public Form_Flatlaf() {
        setOpaque(false);
        FlatIntelliJLaf.setup();
    }

    public void changeColor(Color color) {

    }
}
