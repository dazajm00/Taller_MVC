package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.uniajc.controlador.ControladorEstudiante;
import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;
import com.uniajc.servicios.EstudianteService;

/**
 * Vista Swing para la gestión de {@link Estudiante}.
 * Paleta de colores oficial UNIAJC según guía del profesor.
 */
public class VistaEstudianteSwing extends JPanel {

    // ── Paleta UNIAJC ──────────────────────────────────────────────────────────
    private static final Color C_PRIMARY    = new Color(41,  128, 185);   // #2980B9
    private static final Color C_SECONDARY  = new Color(236, 240, 241);   // #ECF0F1
    private static final Color C_ACCENT     = new Color(231, 76,  60);    // #E74C3C
    private static final Color C_SUCCESS    = new Color(39,  174, 96);    // #27AE60
    private static final Color C_WARNING    = new Color(243, 156, 18);    // #F39C12
    private static final Color C_TEXT_PRI   = new Color(44,  62,  80);    // #2C3E50
    private static final Color C_TEXT_SEC   = new Color(127, 140, 141);   // #7F8C8D

    private static final String[] COLUMNAS = {"ID", "Nombre", "Apellido", "Email", "Creado"};

    private final ControladorEstudiante controlador;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JTextField txtBusqueda;

    // Campos del diálogo
    private JDialog dialogo;
    private JTextField txtNombre, txtApellido, txtEmail;
    private boolean modoEdicion;
    private Estudiante estudianteSeleccionado;

    public VistaEstudianteSwing() {
        this.controlador = new ControladorEstudiante(
                new EstudianteService(new EstudianteDao()));
        initUI();
        cargarTabla();
    }

    // ── Construcción de la interfaz ────────────────────────────────────────────

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(C_SECONDARY);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(panelTitulo(),    BorderLayout.NORTH);
        add(panelTabla(),     BorderLayout.CENTER);
        add(panelBotones(),   BorderLayout.SOUTH);
    }

    private JPanel panelTitulo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_PRIMARY);
        p.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titulo = new JLabel("Gestión de Estudiantes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        busqueda.setOpaque(false);
        txtBusqueda = new JTextField(18);
        txtBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JButton btnBuscar = boton(" Buscar", C_WARNING);
        btnBuscar.addActionListener(e -> filtrarTabla());

        busqueda.add(new JLabel("") {{ setForeground(Color.WHITE); }});
        busqueda.add(txtBusqueda);
        busqueda.add(btnBuscar);

        p.add(titulo,   BorderLayout.WEST);
        p.add(busqueda, BorderLayout.EAST);
        return p;
    }

    private JPanel panelTabla() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_SECONDARY);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(26);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setGridColor(new Color(189, 195, 199));
        tabla.setBackground(Color.WHITE);
        tabla.setForeground(C_TEXT_PRI);
        tabla.setSelectionBackground(C_PRIMARY);
        tabla.setSelectionForeground(Color.WHITE);

        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(C_SECONDARY);
        tabla.getTableHeader().setForeground(C_TEXT_PRI);

        // Ajuste de columnas
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(C_PRIMARY, 2));

        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel panelBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        p.setBackground(C_SECONDARY);

        JButton btnNuevo     = boton(" Nuevo",      C_SUCCESS);
        JButton btnEditar    = boton(" Editar",      C_PRIMARY);
        JButton btnEliminar  = boton(" Eliminar",   C_ACCENT);
        JButton btnRefrescar = boton(" Refrescar",  C_TEXT_SEC);

        btnNuevo.addActionListener(e -> abrirDialogo(false));
        btnEditar.addActionListener(e -> {
            if (tabla.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un estudiante.", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                abrirDialogo(true);
            }
        });
        btnEliminar.addActionListener(e -> eliminar());
        btnRefrescar.addActionListener(e -> cargarTabla());

        p.add(btnNuevo); p.add(btnEditar); p.add(btnEliminar); p.add(btnRefrescar);
        return p;
    }

    // ── Lógica de la vista ─────────────────────────────────────────────────────

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<Estudiante> lista = controlador.listarEstudiantes();
            for (Estudiante e : lista) {
                modeloTabla.addRow(new Object[]{
                    e.getId(), e.getNombre(), e.getApellido(), e.getEmail(),
                    e.getFechaCreacion() != null ? e.getFechaCreacion().toLocalDate() : ""
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarTabla() {
        String busq = txtBusqueda.getText().trim().toLowerCase();
        if (busq.isEmpty()) { cargarTabla(); return; }

        modeloTabla.setRowCount(0);
        try {
            List<Estudiante> lista = controlador.listarEstudiantes();
            for (Estudiante e : lista) {
                if (e.getNombre().toLowerCase().contains(busq)
                 || e.getApellido().toLowerCase().contains(busq)
                 || e.getEmail().toLowerCase().contains(busq)
                 || String.valueOf(e.getId()).contains(busq)) {
                    modeloTabla.addRow(new Object[]{
                        e.getId(), e.getNombre(), e.getApellido(), e.getEmail(),
                        e.getFechaCreacion() != null ? e.getFechaCreacion().toLocalDate() : ""
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirDialogo(boolean edicion) {
        modoEdicion = edicion;
        estudianteSeleccionado = new Estudiante();

        if (edicion) {
            int fila = tabla.getSelectedRow();
            int id   = (int) modeloTabla.getValueAt(fila, 0);
            controlador.buscarEstudiante(id).ifPresentOrElse(
                e -> estudianteSeleccionado = e,
                () -> { JOptionPane.showMessageDialog(this, "Estudiante no encontrado.", "Error", JOptionPane.ERROR_MESSAGE); }
            );
        }

        dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                edicion ? "Editar Estudiante" : "Nuevo Estudiante", true);
        dialogo.setSize(420, 300);
        dialogo.setLocationRelativeTo(this);
        dialogo.setResizable(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Título del diálogo
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lTitulo = new JLabel(edicion ? " Editar Estudiante" : " Nuevo Estudiante", SwingConstants.CENTER);
        lTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lTitulo.setForeground(C_PRIMARY);
        form.add(lTitulo, gbc);

        gbc.gridwidth = 1;

        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(etiqueta("Nombre:"), gbc);
        txtNombre = campo(estudianteSeleccionado.getNombre());
        gbc.gridx = 1; form.add(txtNombre, gbc);

        // Apellido
        gbc.gridx = 0; gbc.gridy = 2;
        form.add(etiqueta("Apellido:"), gbc);
        txtApellido = campo(estudianteSeleccionado.getApellido());
        gbc.gridx = 1; form.add(txtApellido, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        form.add(etiqueta("Email:"), gbc);
        txtEmail = campo(estudianteSeleccionado.getEmail());
        gbc.gridx = 1; form.add(txtEmail, gbc);

        // Botones
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnPanel.setOpaque(false);

        JButton btnGuardar   = boton(" Guardar",  C_SUCCESS);
        JButton btnCancelar  = boton(" Cancelar", C_ACCENT);
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dialogo.dispose());

        btnPanel.add(btnGuardar); btnPanel.add(btnCancelar);
        form.add(btnPanel, gbc);

        dialogo.add(form);
        dialogo.setVisible(true);
    }

    private void guardar() {
        String nombre   = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String email    = txtEmail.getText().trim();

        String resultado;
        if (modoEdicion) {
            resultado = controlador.actualizarEstudiante(estudianteSeleccionado.getId(), nombre, apellido, email);
        } else {
            resultado = controlador.registrarEstudiante(nombre, apellido, email);
        }

        if (resultado.startsWith("✔")) {
            JOptionPane.showMessageDialog(dialogo, resultado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(dialogo, resultado, "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminar() {
        if (tabla.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Desea eliminar el estudiante seleccionado?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmar == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
            String resultado = controlador.eliminarEstudiante(id);
            JOptionPane.showMessageDialog(this, resultado,
                resultado.startsWith("✔") ? "Éxito" : "Error",
                resultado.startsWith("✔") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            cargarTabla();
        }
    }

    // ── Helpers de UI ─────────────────────────────────────────────────────────

    private JButton boton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(130, 34));
        return b;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto, SwingConstants.RIGHT);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(C_TEXT_PRI);
        return l;
    }

    private JTextField campo(String valor) {
        JTextField tf = new JTextField(valor != null ? valor : "", 20);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(200, 28));
        return tf;
    }
}
