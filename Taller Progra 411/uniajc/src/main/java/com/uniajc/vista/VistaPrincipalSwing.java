package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Ventana principal del Sistema de Gestión Académica UNIAJC.
 * Usa un menú lateral con botones y un {@link CardLayout} para navegar
 * entre las vistas de cada entidad.
 */
public class VistaPrincipalSwing extends JFrame {

    // ── Paleta UNIAJC ──────────────────────────────────────────────────────────
    private static final Color C_PRIMARY    = new Color(41,  128, 185);  // #2980B9
    private static final Color C_SECONDARY  = new Color(236, 240, 241);  // #ECF0F1
    private static final Color C_ACCENT     = new Color(231, 76,  60);   // #E74C3C
    private static final Color C_SUCCESS    = new Color(39,  174, 96);   // #27AE60
    private static final Color C_WARNING    = new Color(243, 156, 18);   // #F39C12
    private static final Color C_TEXT_PRI   = new Color(44,  62,  80);   // #2C3E50
    private static final Color C_TEXT_SEC   = new Color(127, 140, 141);  // #7F8C8D
    private static final Color C_MENU_BG    = new Color(33,  97,  140);  // Azul oscuro menú

    // Claves para el CardLayout
    private static final String CARD_HOME         = "INICIO";
    private static final String CARD_ESTUDIANTES  = "ESTUDIANTES";
    private static final String CARD_DOCENTES     = "DOCENTES";
    private static final String CARD_MATERIAS     = "MATERIAS";
    private static final String CARD_GRUPOS       = "GRUPOS";
    private static final String CARD_INSCRIPCIONES= "INSCRIPCIONES";

    private final CardLayout cardLayout = new CardLayout();
    private JPanel panelContenido;

    // Vistas (se instancian una sola vez – lazy no necesario a esta escala)
    private VistaEstudianteSwing  vistaEstudiantes;
    private VistaDocenteSwing     vistaDocentes;
    private VistaMateriaSwing     vistaMaterias;
    private VistaGrupoSwing       vistaGrupos;
    private VistaInscripcionSwing vistaInscripciones;

    public VistaPrincipalSwing() {
        super("Sistema de Gestión Académica – UNIAJC");
        initVentana();
        initVistas();
        initUI();
    }

    // ── Configuración de la ventana ───────────────────────────────────────────

    private void initVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    // ── Instanciar las vistas ─────────────────────────────────────────────────

    private void initVistas() {
        vistaEstudiantes   = new VistaEstudianteSwing();
        vistaDocentes      = new VistaDocenteSwing();
        vistaMaterias      = new VistaMateriaSwing();
        vistaGrupos        = new VistaGrupoSwing();
        vistaInscripciones = new VistaInscripcionSwing();
    }

    // ── Construcción de la interfaz ───────────────────────────────────────────

    private void initUI() {
        add(panelEncabezado(), BorderLayout.NORTH);
        add(panelMenu(),       BorderLayout.WEST);
        add(panelContenidos(), BorderLayout.CENTER);
        add(panelPie(),        BorderLayout.SOUTH);
    }

    /** Barra superior con nombre del sistema y logo/texto institucional. */
    private JPanel panelEncabezado() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, C_TEXT_PRI, getWidth(), 0, C_PRIMARY);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        p.setPreferredSize(new Dimension(0, 65));
        p.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lSistema = new JLabel("Sistema de Gestión Académica");
        lSistema.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lSistema.setForeground(Color.WHITE);

        JLabel lUniajc = new JLabel("UNIAJC – Unidad Central del Valle del Cauca");
        lUniajc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lUniajc.setForeground(new Color(189, 215, 238));
        lUniajc.setHorizontalAlignment(SwingConstants.RIGHT);

        p.add(lSistema, BorderLayout.WEST);
        p.add(lUniajc,  BorderLayout.EAST);
        return p;
    }

    /** Menú lateral izquierdo con botones de navegación. */
    private JPanel panelMenu() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(C_MENU_BG);
        p.setPreferredSize(new Dimension(195, 0));
        p.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Título del menú
        JLabel lMenu = new JLabel("  NAVEGACIÓN", SwingConstants.LEFT);
        lMenu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lMenu.setForeground(new Color(174, 214, 241));
        lMenu.setAlignmentX(CENTER_ALIGNMENT);
        lMenu.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 10));
        p.add(lMenu);

        // Botones de menú
        p.add(botonMenu("Inicio",          CARD_HOME,          C_MENU_BG));
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(botonMenu("Estudiantes",      CARD_ESTUDIANTES,   C_MENU_BG));
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(botonMenu("Docentes",         CARD_DOCENTES,      C_MENU_BG));
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(botonMenu("Materias",          CARD_MATERIAS,      C_MENU_BG));
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(botonMenu("Grupos",            CARD_GRUPOS,        C_MENU_BG));
        p.add(Box.createRigidArea(new Dimension(0, 4)));
        p.add(botonMenu("Inscripciones",     CARD_INSCRIPCIONES, C_MENU_BG));

        p.add(Box.createVerticalGlue());

        // Versión en pie del menú
        JLabel lVersion = new JLabel("  v1.0 – 2026", SwingConstants.LEFT);
        lVersion.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lVersion.setForeground(C_TEXT_SEC);
        lVersion.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 0));
        p.add(lVersion);

        return p;
    }

    /** Área central con CardLayout que alberga todas las vistas. */
    private JPanel panelContenidos() {
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(C_SECONDARY);

        panelContenido.add(panelInicio(),     CARD_HOME);
        panelContenido.add(vistaEstudiantes,  CARD_ESTUDIANTES);
        panelContenido.add(vistaDocentes,     CARD_DOCENTES);
        panelContenido.add(vistaMaterias,     CARD_MATERIAS);
        panelContenido.add(vistaGrupos,       CARD_GRUPOS);
        panelContenido.add(vistaInscripciones,CARD_INSCRIPCIONES);

        cardLayout.show(panelContenido, CARD_HOME);
        return panelContenido;
    }

    /** Pantalla de bienvenida. */
    private JPanel panelInicio() {
        JPanel p = new JPanel(new GridLayout(0, 1, 0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, C_SECONDARY, 0, getHeight(), new Color(213, 230, 245));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        p.setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));

        JLabel lBienvenida = new JLabel("¡Bienvenido al Sistema de Gestión Académica!", SwingConstants.CENTER);
        lBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lBienvenida.setForeground(C_TEXT_PRI);

        JLabel lSub = new JLabel("Seleccione una sección en el menú lateral para comenzar.", SwingConstants.CENTER);
        lSub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lSub.setForeground(C_TEXT_SEC);

        // Tarjetas de resumen de módulos
        

        p.add(lBienvenida);
        p.add(lSub);
        return p;
    }

    private JPanel tarjeta(String icono, String nombre, Color color) {
        JPanel t = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        t.setOpaque(false);
        t.setBorder(BorderFactory.createEmptyBorder(18, 10, 18, 10));

        JLabel lIcono = new JLabel(icono, SwingConstants.CENTER);
        lIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JLabel lNombre = new JLabel(nombre, SwingConstants.CENTER);
        lNombre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lNombre.setForeground(Color.WHITE);

        t.add(lIcono,  BorderLayout.CENTER);
        t.add(lNombre, BorderLayout.SOUTH);
        return t;
    }

    /** Barra de pie de página. */
    private JPanel panelPie() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_TEXT_PRI);
        p.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JLabel lPie = new JLabel("UNIAJC – Práctica MVC  |  Arquitectura: Modelo – Vista – Controlador");
        lPie.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lPie.setForeground(C_TEXT_SEC);

        JLabel lDer = new JLabel("Java + MySQL + Swing  |  2026");
        lDer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lDer.setForeground(C_TEXT_SEC);

        p.add(lPie, BorderLayout.WEST);
        p.add(lDer, BorderLayout.EAST);
        return p;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Crea un botón de navegación para el menú lateral. */
    private JButton botonMenu(String texto, String card, Color bgBase) {
        JButton b = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(C_PRIMARY);
                } else {
                    g2.setColor(bgBase);
                }
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setForeground(Color.WHITE);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        b.setAlignmentX(CENTER_ALIGNMENT);
        b.addActionListener(e -> cardLayout.show(panelContenido, card));
        return b;
    }
}
