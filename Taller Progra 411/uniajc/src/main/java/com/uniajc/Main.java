package com.uniajc;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.uniajc.vista.VistaPrincipalSwing;

/**
 * Punto de entrada del Sistema de Gestión Académica – UNIAJC.
 */
public class Main {

    public static void main(String[] args) {

        // Look & Feel nativo del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla usa el L&F por defecto de Swing, no es crítico
        }

        // Lanzar la interfaz en el hilo de eventos de Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            VistaPrincipalSwing ventana = new VistaPrincipalSwing();
            ventana.setVisible(true);
        });
    }
}