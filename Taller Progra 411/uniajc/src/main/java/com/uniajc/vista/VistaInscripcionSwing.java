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
import javax.swing.JComboBox;
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

import com.uniajc.controlador.ControladorInscripcion;
import com.uniajc.dao.EstudianteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.InscripcionDao;
import com.uniajc.modelo.Estudiante;
import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.Inscripcion;
import com.uniajc.modelo.Inscripcion.Estado;
import com.uniajc.servicios.InscripcionService;

/**
 * Vista Swing para la gestión de {@link Inscripcion}.
 * Paleta de colores oficial UNIAJC.
 */
public class VistaInscripcionSwing extends JPanel {

    // ── Paleta UNIAJC ──────────────────────────────────────────────────────────
    private static final Color C_PRIMARY   = new Color(41,  128, 185);  // #2980B9
    private static final Color C_SECONDARY = new Color(236, 240, 241);  // #ECF0F1
    private static final Color C_ACCENT    = new Color(231, 76,  60);   // #E74C3C
    private static final Color C_SUCCESS   = new Color(39,  174, 96);   // #27AE60
    private static final Color C_WARNING   = new Color(243, 156, 18);   // #F39C12
    private static final Color C_TEXT_PRI  = new Color(44,  62,  80);   // #2C3E50
    private static final Color C_TEXT_SEC  = new Color(127, 140, 141);  // #7F8C8D

    private static final String[] COLUMNAS =
        {"ID", "Estudiante", "Grupo / Materia", "Nota Final", "Estado", "Creado"};

    private final ControladorInscripcion controlador;
    private final EstudianteDao estudianteDao = new EstudianteDao();
    private final GrupoDao      grupoDao      = new GrupoDao();

    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JTextField txtBusqueda;

    // Campos del diálogo
    private JDialog   dialogo;
    private JComboBox<String> cmbEstudiante, cmbGrupo, cmbEstado;
    private JTextField txtNota;
    private List<Estudiante> listaEstudiantes;
    private List<Grupo>      listaGrupos;
    private boolean    modoEdicion;
    private Inscripcion inscripcionSeleccionada;

    public VistaInscripcionSwing() {
        this.controlador = new ControladorInscripcion(
            new InscripcionService(new InscripcionDao(), estudianteDao, grupoDao));
        initUI();
        cargarTabla();
    }

    // ── Construcción de la interfaz ────────────────────────────────────────────

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(C_SECONDARY);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(panelTitulo(),  BorderLayout.NORTH);
        add(panelTabla(),   BorderLayout.CENTER);
        add(panelBotones(), BorderLayout.SOUTH);
    }

    private JPanel panelTitulo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_PRIMARY);
        p.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titulo = new JLabel("Gestión de Inscripciones");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);

        JPanel busq = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        busq.setOpaque(false);
        txtBusqueda = new JTextField(18);
        txtBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JButton btnBuscar = boton(" Buscar", C_WARNING);
        btnBuscar.addActionListener(e -> filtrarTabla());
        busq.add(txtBusqueda);
        busq.add(btnBuscar);

        p.add(titulo, BorderLayout.WEST);
        p.add(busq,   BorderLayout.EAST);
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
        tabla.getColumnModel().getColumn(3).setMaxWidth(90);
        tabla.getColumnModel().getColumn(4).setMaxWidth(100);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(C_PRIMARY, 2));
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel panelBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        p.setBackground(C_SECONDARY);

        JButton btnNuevo     = boton(" Inscribir",   C_SUCCESS);
        JButton btnEditar    = boton("Editar",        C_PRIMARY);
        JButton btnEliminar  = boton(" Eliminar",     C_ACCENT);
        JButton btnRefrescar = boton(" Refrescar",    C_TEXT_SEC);

        btnNuevo.addActionListener(e -> abrirDialogo(false));
        btnEditar.addActionListener(e -> {
            if (tabla.getSelectedRow() < 0)
                JOptionPane.showMessageDialog(this, "Seleccione una inscripción.", "Aviso", JOptionPane.WARNING_MESSAGE);
            else abrirDialogo(true);
        });
        btnEliminar.addActionListener(e -> eliminar());
        btnRefrescar.addActionListener(e -> cargarTabla());

        p.add(btnNuevo); p.add(btnEditar); p.add(btnEliminar); p.add(btnRefrescar);
        return p;
    }

    // ── Lógica de la vista ────────────────────────────────────────────────────

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        try {
            for (Inscripcion i : controlador.listarInscripciones()) {
                modeloTabla.addRow(new Object[]{
                    i.getId(),
                    i.getNombreEstudiante(),
                    i.getInfoGrupo(),
                    i.getNotaFinal() != null ? i.getNotaFinal() : "—",
                    i.getEstado() != null ? i.getEstado().name() : "ACTIVO",
                    i.getFechaCreacion() != null ? i.getFechaCreacion().toLocalDate() : ""
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarTabla() {
        String busq = txtBusqueda.getText().trim().toLowerCase();
        if (busq.isEmpty()) { cargarTabla(); return; }
        modeloTabla.setRowCount(0);
        try {
            for (Inscripcion i : controlador.listarInscripciones()) {
                String nombreEst  = i.getNombreEstudiante() != null ? i.getNombreEstudiante().toLowerCase() : "";
                String infoGrupo  = i.getInfoGrupo()        != null ? i.getInfoGrupo().toLowerCase()        : "";
                String estadoStr  = i.getEstado()           != null ? i.getEstado().name().toLowerCase()    : "";
                if (nombreEst.contains(busq) || infoGrupo.contains(busq)
                 || estadoStr.contains(busq) || String.valueOf(i.getId()).contains(busq)) {
                    modeloTabla.addRow(new Object[]{
                        i.getId(), i.getNombreEstudiante(), i.getInfoGrupo(),
                        i.getNotaFinal() != null ? i.getNotaFinal() : "—",
                        i.getEstado() != null ? i.getEstado().name() : "ACTIVO",
                        i.getFechaCreacion() != null ? i.getFechaCreacion().toLocalDate() : ""
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirDialogo(boolean edicion) {
        modoEdicion = edicion;
        inscripcionSeleccionada = new Inscripcion();

        if (edicion) {
            int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
            controlador.buscarInscripcion(id).ifPresent(i -> inscripcionSeleccionada = i);
        }

        // Cargar listas para los combos
        listaEstudiantes = estudianteDao.obtenerTodos();
        listaGrupos      = grupoDao.obtenerTodos();

        dialogo = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                edicion ? "Editar Inscripción" : "Nueva Inscripción", true);
        dialogo.setSize(470, 380);
        dialogo.setLocationRelativeTo(this);
        dialogo.setResizable(false);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lTitulo = new JLabel(
            edicion ? " Editar Inscripción" : " Nueva Inscripción", SwingConstants.CENTER);
        lTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lTitulo.setForeground(C_PRIMARY);
        form.add(lTitulo, gbc);
        gbc.gridwidth = 1;

        // Combo Estudiantes
        gbc.gridy = 1; gbc.gridx = 0; form.add(etiqueta("Estudiante:"), gbc);
        cmbEstudiante = new JComboBox<>();
        cmbEstudiante.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        for (Estudiante e : listaEstudiantes)
            cmbEstudiante.addItem(e.getId() + " – " + e.getNombre() + " " + e.getApellido());
        if (edicion && inscripcionSeleccionada.getIdEstudiante() > 0) {
            for (int i = 0; i < listaEstudiantes.size(); i++) {
                if (listaEstudiantes.get(i).getId() == inscripcionSeleccionada.getIdEstudiante()) {
                    cmbEstudiante.setSelectedIndex(i); break;
                }
            }
        }
        gbc.gridx = 1; form.add(cmbEstudiante, gbc);

        // Combo Grupos
        gbc.gridy = 2; gbc.gridx = 0; form.add(etiqueta("Grupo:"), gbc);
        cmbGrupo = new JComboBox<>();
        cmbGrupo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        for (Grupo g : listaGrupos)
            cmbGrupo.addItem(g.getId() + " – " + g.getNombreMateria() + " | " + g.getAula());
        if (edicion && inscripcionSeleccionada.getIdGrupo() > 0) {
            for (int i = 0; i < listaGrupos.size(); i++) {
                if (listaGrupos.get(i).getId() == inscripcionSeleccionada.getIdGrupo()) {
                    cmbGrupo.setSelectedIndex(i); break;
                }
            }
        }
        gbc.gridx = 1; form.add(cmbGrupo, gbc);

        // Nota final (solo en edición)
        gbc.gridy = 3; gbc.gridx = 0; form.add(etiqueta("Nota final (0-5):"), gbc);
        String notaActual = (edicion && inscripcionSeleccionada.getNotaFinal() != null)
                ? inscripcionSeleccionada.getNotaFinal().toPlainString() : "";
        txtNota = campo(notaActual);
        txtNota.setToolTipText("Dejar en blanco si aún no hay nota. Ej: 3.8");
        if (!edicion) txtNota.setEnabled(false);
        gbc.gridx = 1; form.add(txtNota, gbc);

        // Estado (solo en edición)
        gbc.gridy = 4; gbc.gridx = 0; form.add(etiqueta("Estado:"), gbc);
        cmbEstado = new JComboBox<>(new String[]{
            Estado.ACTIVO.name(), Estado.APROBADO.name(),
            Estado.REPROBADO.name(), Estado.CANCELADO.name()
        });
        cmbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        if (edicion && inscripcionSeleccionada.getEstado() != null)
            cmbEstado.setSelectedItem(inscripcionSeleccionada.getEstado().name());
        if (!edicion) cmbEstado.setEnabled(false);
        gbc.gridx = 1; form.add(cmbEstado, gbc);

        // Nota informativa para nueva inscripción
        if (!edicion) {
            gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
            JLabel lInfo = new JLabel("* Nota y estado se pueden editar después de inscribir.", SwingConstants.CENTER);
            lInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lInfo.setForeground(C_TEXT_SEC);
            form.add(lInfo, gbc);
            gbc.gridwidth = 1;
        }

        // Botones
        int filaBtn = edicion ? 5 : 6;
        gbc.gridy = filaBtn; gbc.gridx = 0; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnPanel.setOpaque(false);
        JButton btnGuardar  = boton("💾 Guardar",  C_SUCCESS);
        JButton btnCancelar = boton("✖ Cancelar", C_ACCENT);
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dialogo.dispose());
        btnPanel.add(btnGuardar); btnPanel.add(btnCancelar);
        form.add(btnPanel, gbc);

        dialogo.add(form);
        dialogo.setVisible(true);
    }

    private void guardar() {
        if (listaEstudiantes.isEmpty() || listaGrupos.isEmpty()) {
            JOptionPane.showMessageDialog(dialogo,
                "Debe haber estudiantes y grupos registrados primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idxEst   = cmbEstudiante.getSelectedIndex();
        int idxGrupo = cmbGrupo.getSelectedIndex();
        if (idxEst < 0 || idxGrupo < 0) {
            JOptionPane.showMessageDialog(dialogo, "Seleccione estudiante y grupo.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idEstudiante = listaEstudiantes.get(idxEst).getId();
        int idGrupo      = listaGrupos.get(idxGrupo).getId();

        String resultado;
        if (modoEdicion) {
            String nota   = txtNota.getText().trim();
            String estado = (String) cmbEstado.getSelectedItem();
            resultado = controlador.actualizarInscripcion(
                inscripcionSeleccionada.getId(), idEstudiante, idGrupo, nota, estado);
        } else {
            resultado = controlador.registrarInscripcion(idEstudiante, idGrupo);
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
            JOptionPane.showMessageDialog(this, "Seleccione una inscripción.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this,
                "¿Eliminar la inscripción seleccionada?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            int id = (int) modeloTabla.getValueAt(tabla.getSelectedRow(), 0);
            String r = controlador.eliminarInscripcion(id);
            JOptionPane.showMessageDialog(this, r,
                r.startsWith("✔") ? "Éxito" : "Error",
                r.startsWith("✔") ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
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
        b.setPreferredSize(new Dimension(135, 34));
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
        tf.setPreferredSize(new Dimension(210, 28));
        return tf;
    }
}