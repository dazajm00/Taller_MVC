package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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

import com.uniajc.controlador.ControladorDocente;
import com.uniajc.dao.DocenteDao;
import com.uniajc.modelo.Docente;
import com.uniajc.servicios.DocenteService;

/**
 * Vista Swing para la gestión de {@link Docente}.
 */
public class VistaDocenteSwing extends JPanel {

    // Paleta UNIAJC
    private static final Color C_PRIMARY   = new Color(41,  128, 185);
    private static final Color C_SECONDARY = new Color(236, 240, 241);
    private static final Color C_ACCENT    = new Color(231, 76,  60);
    private static final Color C_SUCCESS   = new Color(39,  174, 96);
    private static final Color C_WARNING   = new Color(243, 156, 18);
    private static final Color C_TEXT_PRI  = new Color(44,  62,  80);
    private static final Color C_TEXT_SEC  = new Color(127, 140, 141);

    private static final String[] COLUMNAS = {"ID", "Nombre", "Especialidad", "Email", "Creado"};

    private final ControladorDocente controlador;
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JTextField txtBusqueda;

    private JDialog dialogo;
    private JTextField txtNombre, txtEspecialidad, txtEmail;
    private boolean modoEdicion;
    private Docente docenteSeleccionado;

    public VistaDocenteSwing() {
        this.controlador = new ControladorDocente(new DocenteService(new DocenteDao()));
        initUI();
        cargarTabla();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(C_SECONDARY);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(panelTitulo(), BorderLayout.NORTH);
        add(panelTabla(),  BorderLayout.CENTER);
        add(panelBotones(),BorderLayout.SOUTH);
    }

    private JPanel panelTitulo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_PRIMARY);
        p.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titulo = new JLabel("Gestión de Docentes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        busqueda.setOpaque(false);
        txtBusqueda = new JTextField(18);
        txtBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JButton btnBuscar = boton(" Buscar", C_WARNING);
        btnBuscar.addActionListener(e -> filtrarTabla());
        busqueda.add(txtBusqueda);
        busqueda.add(btnBuscar);

        p.add(titulo, BorderLayout.WEST);
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
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(C_PRIMARY, 2));
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel panelBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        p.setBackground(C_SECONDARY);

        JButton btnNuevo     = boton(" Nuevo",     C_SUCCESS);
        JButton btnEditar    = boton(" Editar",     C_PRIMARY);
        JButton btnEliminar  = boton(" Eliminar",  C_ACCENT);
        JButton btnRefrescar = boton(" Refrescar", C_TEXT_SEC);

        btnNuevo.addActionListener(e -> abrirDialogo(false));
        btnEditar.addActionListener(e -> {
            if (tabla.getSelectedRow() < 0)
                JOptionPane.showMessageDialog(this, "Seleccione un docente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            else abrirDialogo(true);
        });
        btnEliminar.addActionListener(e -> eliminar());
        btnRefrescar.addActionListener(e -> cargarTabla());

        p.add(btnNuevo); p.add(btnEditar); p.add(btnEliminar); p.add(btnRefrescar);
        return p;
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            for (Docente d : controlador.listarDocentes()) {
                modeloTabla.addRow(new Object[]{
                    d.getId(), d.getNombre(), d.getEspecialidad(), d.getEmail(),
                    d.getFechaCreacion() != null ? d.getFechaCreacion().toLocalDate() : ""
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
            for (Docente d : controlador.listarDocentes()) {
                if (d.getNombre().toLowerCase().contains(busq)
                 || d.getEspecialidad().toLowerCase().contains(busq)
                 || d.getEmail().toLowerCase().contains(busq)
                 || String.valueOf(d.getId()).contains(busq)) {
                    modeloTabla.addRow(new Object[]{
                        d.getId(), d.getNombre(), d.getEspecialidad(), d.getEmail(),
                        d.getFechaCreacion() != null ? d.getFechaCreacion().toLocalDate() : ""
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirDialogo(boolean edicion) {
        modoEdicion = edicion;
        docenteSeleccionado = new Docente();

        if (edicion) {
            int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
            controlador.buscarDocente(id).ifPresent(d -> docenteSeleccionado = d);
        }

        dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                edicion ? "Editar Docente" : "Nuevo Docente", true);
        dialogo.setSize(430, 310);
        dialogo.setLocationRelativeTo(this);
        dialogo.setResizable(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lTitulo = new JLabel(edicion ? " Editar Docente" : " Nuevo Docente", SwingConstants.CENTER);
        lTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lTitulo.setForeground(C_PRIMARY);
        form.add(lTitulo, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 1; gbc.gridx = 0; form.add(etiqueta("Nombre:"), gbc);
        txtNombre = campo(docenteSeleccionado.getNombre());
        gbc.gridx = 1; form.add(txtNombre, gbc);

        gbc.gridy = 2; gbc.gridx = 0; form.add(etiqueta("Especialidad:"), gbc);
        txtEspecialidad = campo(docenteSeleccionado.getEspecialidad());
        gbc.gridx = 1; form.add(txtEspecialidad, gbc);

        gbc.gridy = 3; gbc.gridx = 0; form.add(etiqueta("Email:"), gbc);
        txtEmail = campo(docenteSeleccionado.getEmail());
        gbc.gridx = 1; form.add(txtEmail, gbc);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnPanel.setOpaque(false);
        JButton btnGuardar  = boton(" Guardar",  C_SUCCESS);
        JButton btnCancelar = boton(" Cancelar", C_ACCENT);
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dialogo.dispose());
        btnPanel.add(btnGuardar); btnPanel.add(btnCancelar);
        form.add(btnPanel, gbc);

        dialogo.add(form);
        dialogo.setVisible(true);
    }

    private void guardar() {
        String resultado = modoEdicion
            ? controlador.actualizarDocente(docenteSeleccionado.getId(),
                txtNombre.getText().trim(), txtEspecialidad.getText().trim(), txtEmail.getText().trim())
            : controlador.registrarDocente(
                txtNombre.getText().trim(), txtEspecialidad.getText().trim(), txtEmail.getText().trim());

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
            JOptionPane.showMessageDialog(this, "Seleccione un docente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar el docente seleccionado?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
            String r = controlador.eliminarDocente(id);
            JOptionPane.showMessageDialog(this, r,
                r.startsWith("✔") ? "Éxito" : "Error",
                r.startsWith("✔") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            cargarTabla();
        }
    }

    private JButton boton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(color); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false); b.setOpaque(true);
        b.setPreferredSize(new Dimension(130, 34));
        return b;
    }

    private JLabel etiqueta(String t) {
        JLabel l = new JLabel(t, SwingConstants.RIGHT);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(C_TEXT_PRI);
        return l;
    }

    private JTextField campo(String v) {
        JTextField tf = new JTextField(v != null ? v : "", 20);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setPreferredSize(new Dimension(200, 28));
        return tf;
    }
}