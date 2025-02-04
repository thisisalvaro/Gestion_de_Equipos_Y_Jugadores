package clase.Futbol_App;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class JugadorPanel extends JPanel {

    private JComboBox<String> equiposComboBox;
    private JTable jugadoresTable;
    private DefaultTableModel jugadoresModel;
    private DataBaseConnection dbConn;
    private JLabel equipoImageLabel;
    private JTextField filtroNombreField;
    private JButton filtrarButton;

    public JugadorPanel() {
        setLookAndFeel(); // Establecer el Look and Feel
        dbConn = new DataBaseConnection();
        setLayout(new BorderLayout(10, 10));

        // Panel superior: ComboBox para equipos y imagen del equipo
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel equiposLabel = new JLabel("Selecciona un equipo:");
        equiposLabel.setFont(new Font("Arial", Font.BOLD, 14));

        equiposComboBox = new JComboBox<>();
        equiposComboBox.addActionListener(e -> cargarJugadores());

        equipoImageLabel = new JLabel();
        equipoImageLabel.setPreferredSize(new Dimension(100, 100));

        topPanel.add(equiposLabel, BorderLayout.WEST);
        topPanel.add(equiposComboBox, BorderLayout.CENTER);
        topPanel.add(equipoImageLabel, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel para filtro por nombre de jugador
        JPanel filtroPanel = new JPanel(new FlowLayout());
        filtroPanel.setBorder(BorderFactory.createTitledBorder("Filtrar por nombre de jugador"));
        filtroNombreField = new JTextField(20);
        filtrarButton = new JButton("Filtrar");
        filtrarButton.addActionListener(e -> filtrarJugadoresPorNombre());
        filtroPanel.add(new JLabel("Nombre:"));
        filtroPanel.add(filtroNombreField);
        filtroPanel.add(filtrarButton);

        // Tabla de jugadores
        jugadoresTable = new JTable();
        jugadoresModel = new DefaultTableModel();
        jugadoresTable.setModel(jugadoresModel);
        jugadoresModel.setColumnIdentifiers(new String[]{"ID", "Nombre", "Posición", "Equipo ID", "Imagen"});

        // Configurar altura de las filas de la tabla
        jugadoresTable.setRowHeight(50); // Puedes cambiar el valor 50 a la altura deseada

        // Renderizador de imágenes con escalado
        jugadoresTable.getColumn("Imagen").setCellRenderer(new ImageRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof ImageIcon) {
                    ImageIcon icon = (ImageIcon) value;
                    Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(img));
                }
                return label;
            }
        });

        JScrollPane jugadoresScrollPane = new JScrollPane(jugadoresTable);
        jugadoresScrollPane.setBorder(BorderFactory.createTitledBorder("Jugadores"));

        // Panel inferior: Botones de acción
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addJugadorButton = new JButton("Añadir Jugador");
        addJugadorButton.addActionListener(e -> showAddJugadorDialog());

        JButton updateJugadorButton = new JButton("Actualizar Jugador");
        updateJugadorButton.addActionListener(e -> showUpdateJugadorDialog());

        JButton deleteJugadorButton = new JButton("Eliminar Jugador");
        deleteJugadorButton.addActionListener(e -> deleteJugador());

        buttonPanel.add(addJugadorButton);
        buttonPanel.add(updateJugadorButton);
        buttonPanel.add(deleteJugadorButton);

        // Añadir paneles al contenedor principal
        add(topPanel, BorderLayout.NORTH);
        add(filtroPanel, BorderLayout.WEST);
        add(jugadoresScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        cargarEquipos();

        this.repaint();
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el Look and Feel: " + e.getMessage());
        }
    }

    private void cargarEquipos() {
        try {
            List<Equipo> equipos = dbConn.getListaEquipos();
            equiposComboBox.removeAllItems();

            for (Equipo equipo : equipos) {
                equiposComboBox.addItem(equipo.getNombre());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar equipos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarJugadores() {
        String equipoSeleccionado = (String) equiposComboBox.getSelectedItem();
        if (equipoSeleccionado == null) {
            return;
        }

        jugadoresModel.setRowCount(0); // Limpiar la tabla

        try {
            List<Jugador> jugadores = dbConn.getListaJugadores();
            for (Jugador jugador : jugadores) {
                if (jugador.getEquipoId() == getIdByNameEquipo(equipoSeleccionado)) {
                    ImageIcon icon = null;
                    String urlString = jugador.getUrl();
                    if (urlString != null && !urlString.isEmpty()) {
                        try {
                            URL url = convertToURL(urlString);
                            icon = new ImageIcon(url);
                        } catch (MalformedURLException e) {
                            System.err.println("URL inválida: " + urlString);
                        }
                    }
                    jugadoresModel.addRow(new Object[]{
                            jugador.getId(),
                            jugador.getNombre(),
                            jugador.getPosicion(),
                            jugador.getEquipoId(),
                            icon
                    });
                }
            }
            // Cargar la imagen del equipo
            cargarImagenEquipo(equipoSeleccionado);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar jugadores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarImagenEquipo(String equipoSeleccionado) {
        try {
            List<Equipo> equipos = dbConn.getListaEquipos();
            for (Equipo equipo : equipos) {
                if (equipo.getNombre().equals(equipoSeleccionado)) {
                    ImageIcon icon = null;
                    String urlString = equipo.getUrl();
                    if (urlString != null && !urlString.isEmpty()) {
                        try {
                            URL url = convertToURL(urlString);
                            icon = new ImageIcon(url);
                            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            equipoImageLabel.setIcon(new ImageIcon(img));
                        } catch (MalformedURLException e) {
                            System.err.println("URL inválida: " + urlString);
                        }
                    }
                    break;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen del equipo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getIdByNameEquipo(String nombre) {
        try {
            List<Equipo> equipos = dbConn.getListaEquipos();
            for (Equipo eq : equipos) {
                if (eq.getNombre().equalsIgnoreCase(nombre)) {
                    return eq.getId();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener ID del equipo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }

    private URL convertToURL(String urlString) throws MalformedURLException {
        if (urlString.startsWith("http") || urlString.startsWith("https") || urlString.startsWith("ftp")) {
            return new URL(urlString);
        } else {
            File file = new File(urlString);
            return file.toURI().toURL();
        }
    }

    private void showAddJugadorDialog() {
        JDialog dialog = createJugadorDialog("Añadir Jugador", null);
        dialog.setVisible(true);
    }

    private void showUpdateJugadorDialog() {
        int selectedRow = jugadoresTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un jugador para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Jugador jugador = new Jugador(
            (int) jugadoresModel.getValueAt(selectedRow, 0),
            (String) jugadoresModel.getValueAt(selectedRow, 1),
            (String) jugadoresModel.getValueAt(selectedRow, 2),
            (int) jugadoresModel.getValueAt(selectedRow, 3),
            (ImageIcon) jugadoresModel.getValueAt(selectedRow, 4) != null ? ((ImageIcon) jugadoresModel.getValueAt(selectedRow, 4)).getDescription() : ""
        );

        JDialog dialog = createJugadorDialog("Actualizar Jugador", jugador);
        dialog.setVisible(true);
    }

    private JDialog createJugadorDialog(String title, Jugador jugador) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setLayout(new GridLayout(7, 2, 10, 10)); // Cambiado a GridLayout con 7 filas

        JTextField nombreField = new JTextField(jugador != null ? jugador.getNombre() : "");
        JTextField posicionField = new JTextField(jugador != null ? jugador.getPosicion() : "");
        JComboBox<String> equipoComboBox = new JComboBox<>();
        JTextField urlField = new JTextField(jugador != null ? jugador.getUrl() : "");
        JButton selectImageButton = new JButton("Seleccionar Imagen");

        JLabel imagenLabel = new JLabel();
        if (jugador != null && !jugador.getUrl().isEmpty()) {
            try {
                URL url = convertToURL(jugador.getUrl());
                ImageIcon icon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                imagenLabel.setIcon(icon);
                imagenLabel.setText(""); // Clear any text
            } catch (MalformedURLException e) {
                System.err.println("URL inválida: " + jugador.getUrl());
            }
        }

        try {
            List<Equipo> equipos = dbConn.getListaEquipos();
            for (Equipo equipo : equipos) {
                equipoComboBox.addItem(equipo.getNombre());
            }
            if (jugador != null) {
                equipoComboBox.setSelectedItem(getEquipoNameById(jugador.getEquipoId()));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar equipos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        selectImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                urlField.setText(selectedFile.getAbsolutePath());
                ImageIcon icon = new ImageIcon(new ImageIcon(selectedFile.getAbsolutePath()).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                imagenLabel.setIcon(icon);
                imagenLabel.setText(""); // Clear any text
            }
        });

        dialog.add(new JLabel("Nombre:"));
        dialog.add(nombreField);
        dialog.add(new JLabel("Posición:"));
        dialog.add(posicionField);
        dialog.add(new JLabel("Equipo:"));
        dialog.add(equipoComboBox);
        dialog.add(new JLabel("URL Imagen:"));
        dialog.add(urlField);
        urlField.setEnabled(false);
        dialog.add(new JLabel("Seleccionar Imagen:"));
        dialog.add(selectImageButton);
        dialog.add(new JLabel("Imagen Actual:"));
        dialog.add(imagenLabel); // Añadir la etiqueta de imagen al diálogo

        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String posicion = posicionField.getText().trim();
            String equipoNombre = (String) equipoComboBox.getSelectedItem();
            String urlImagen = urlField.getText().trim();

            if (nombre.isEmpty() || posicion.isEmpty() || equipoNombre == null || urlImagen.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int equipoId = getIdByNameEquipo(equipoNombre);
            if (equipoId == -1) {
                JOptionPane.showMessageDialog(dialog, "Equipo seleccionado no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (jugador == null) { // Añadir nuevo jugador
                try {
                    int newId = dbConn.getMaxJugadorId() + 1; // Obtener el ID más alto actual + 1
                    dbConn.addJugador(new Jugador(newId, nombre, posicion, equipoId, urlImagen));
                    JOptionPane.showMessageDialog(dialog, "Jugador añadido con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarJugadores();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error al añadir jugador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else { // Actualizar jugador existente
                try {
                    dbConn.updateJugador(new Jugador(jugador.getId(), nombre, posicion, equipoId, urlImagen));
                    JOptionPane.showMessageDialog(dialog, "Jugador actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarJugadores();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error al actualizar jugador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            dialog.dispose();
        });

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(saveButton);
        dialog.add(cancelButton);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        return dialog;
    }

    private void deleteJugador() {
        int selectedRow = jugadoresTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un jugador para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int jugadorId = (int) jugadoresModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este jugador?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dbConn.deleteJugador(jugadorId);
                cargarJugadores();
                JOptionPane.showMessageDialog(this, "Jugador eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el jugador: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getEquipoNameById(int equipoId) {
        try {
            List<Equipo> equipos = dbConn.getListaEquipos();
            for (Equipo equipo : equipos) {
                if (equipo.getId() == equipoId) {
                    return equipo.getNombre();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el nombre del equipo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private void filtrarJugadoresPorNombre() {
        String nombreFiltro = filtroNombreField.getText().trim();
        String equipoSeleccionado = (String) equiposComboBox.getSelectedItem();
        if (equipoSeleccionado == null) {
            return;
        }

        jugadoresModel.setRowCount(0); // Limpiar la tabla

        try {
            List<Jugador> jugadores = dbConn.getListaJugadores();
            for (Jugador jugador : jugadores) {
                if (jugador.getEquipoId() == getIdByNameEquipo(equipoSeleccionado) &&
                    jugador.getNombre().toLowerCase().contains(nombreFiltro.toLowerCase())) {
                    ImageIcon icon = null;
                    String urlString = jugador.getUrl();
                    if (urlString != null && !urlString.isEmpty()) {
                        try {
                            URL url = convertToURL(urlString);
                            icon = new ImageIcon(url);
                        } catch (MalformedURLException e) {
                            System.err.println("URL inválida: " + urlString);
                        }
                    }
                    jugadoresModel.addRow(new Object[]{
                            jugador.getId(),
                            jugador.getNombre(),
                            jugador.getPosicion(),
                            jugador.getEquipoId(),
                            icon
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al filtrar jugadores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}