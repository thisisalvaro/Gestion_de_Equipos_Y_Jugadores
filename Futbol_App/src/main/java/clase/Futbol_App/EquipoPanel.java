package clase.Futbol_App;

import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class EquipoPanel extends JPanel {

    private JTable equiposTable;
    private DefaultTableModel equiposModel;
    private DataBaseConnection dbConn;

    public EquipoPanel() {
        setLookAndFeel(); // Establecer el Look and Feel
        dbConn = new DataBaseConnection();
        setLayout(new BorderLayout(10, 10));

        // Tabla de equipos
        equiposTable = new JTable();
        equiposModel = new DefaultTableModel();
        equiposTable.setModel(equiposModel);
        equiposModel.setColumnIdentifiers(new String[]{"ID", "Nombre", "Ciudad", "Estadio", "Imagen"});

        // Renderizador de imágenes con escalado
        equiposTable.getColumn("Imagen").setCellRenderer(new ImageRenderer() {
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

        JScrollPane equiposScrollPane = new JScrollPane(equiposTable);
        equiposScrollPane.setBorder(BorderFactory.createTitledBorder("Equipos"));

        // Panel inferior: Botones de acción
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addEquipoButton = new JButton("Añadir Equipo");
        addEquipoButton.addActionListener(e -> showAddEquipoDialog());

        JButton updateEquipoButton = new JButton("Actualizar Equipo");
        updateEquipoButton.addActionListener(e -> showUpdateEquipoDialog());

        JButton deleteEquipoButton = new JButton("Eliminar Equipo");
        deleteEquipoButton.addActionListener(e -> deleteEquipo());

        buttonPanel.add(addEquipoButton);
        buttonPanel.add(updateEquipoButton);
        buttonPanel.add(deleteEquipoButton);

        // Añadir paneles al contenedor principal
        add(equiposScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        cargarEquipos();
        // Configurar anchos de las columnas
        equiposTable.setRowHeight(20); // Altura fija de las filas (puedes ajustar a tu preferencia)

        repaint();
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
        equiposModel.setRowCount(0); // Limpiar la tabla

        try {
            List<Equipo> equipos = dbConn.getListaEquipos();
            for (Equipo equipo : equipos) {
                ImageIcon icon = null;
                String urlString = equipo.getUrl();
                if (urlString != null && !urlString.isEmpty()) {
                    try {
                        URL url = convertToURL(urlString);
                        icon = new ImageIcon(url);
                        icon.setDescription(urlString); // Establecer la descripción con la URL
                    } catch (MalformedURLException e) {
                        System.err.println("URL inválida: " + urlString);
                    }
                }
                equiposModel.addRow(new Object[]{
                    equipo.getId(),
                    equipo.getNombre(),
                    equipo.getCiudad(),
                    equipo.getEstadio(),
                    icon != null ? icon : urlString // Si no hay icono, usar la URL como String
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar equipos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private URL convertToURL(String urlString) throws MalformedURLException {
        if (urlString.startsWith("http") || urlString.startsWith("https") || urlString.startsWith("ftp")) {
            return new URL(urlString);
        } else {
            File file = new File(urlString);
            return file.toURI().toURL();
        }
    }

    private void showAddEquipoDialog() {
        JDialog dialog = createEquipoDialog("Añadir Equipo", null);
        dialog.setVisible(true);
    }

    private void showUpdateEquipoDialog() {
        int selectedRow = equiposTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un equipo para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener la imagen
        Object imageValue = equiposModel.getValueAt(selectedRow, 4);
        String urlImagen = "";

        if (imageValue instanceof ImageIcon) {
            ImageIcon icon = (ImageIcon) imageValue;
            urlImagen = icon.getDescription(); // Asumimos que la URL está en la descripción del icono
        } else if (imageValue instanceof String) {
            urlImagen = (String) imageValue;
        }

        // Crear un objeto Equipo con los valores obtenidos de la tabla
        Equipo equipo = new Equipo(
            (int) equiposModel.getValueAt(selectedRow, 0),
            (String) equiposModel.getValueAt(selectedRow, 1),
            (String) equiposModel.getValueAt(selectedRow, 2),
            (String) equiposModel.getValueAt(selectedRow, 3),
            urlImagen
        );

        JDialog dialog = createEquipoDialog("Actualizar Equipo", equipo);
        dialog.setVisible(true);
    }

    private JDialog createEquipoDialog(String title, Equipo equipo) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));

        JTextField nombreField = new JTextField(equipo != null ? equipo.getNombre() : "");
        JTextField ciudadField = new JTextField(equipo != null ? equipo.getCiudad() : "");
        JTextField estadio = new JTextField(equipo != null ? String.valueOf(equipo.getEstadio()) : "");
        JTextField urlField = new JTextField(equipo != null && equipo.getUrl() != null ? equipo.getUrl() : "");
        JButton selectImageButton = new JButton("Seleccionar Imagen");

        selectImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                urlField.setText(selectedFile.getAbsolutePath());
            }
        });

        dialog.add(new JLabel("Nombre:"));
        dialog.add(nombreField);
        dialog.add(new JLabel("Ciudad:"));
        dialog.add(ciudadField);
        dialog.add(new JLabel("Estadio:"));
        dialog.add(estadio);
        JLabel urlImagenLabel = new JLabel("URL Imagen:");
        dialog.add(urlImagenLabel);
        dialog.add(urlField);
        urlField.setEnabled(false);
        dialog.add(new JLabel("Seleccionar Imagen:"));
        dialog.add(selectImageButton);

        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String ciudad = ciudadField.getText().trim();
            String estadioU = estadio.getText().trim();
            String urlImagen = urlField.getText().trim();

            if (nombre.isEmpty() || ciudad.isEmpty() || urlImagen.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (equipo == null) { // Añadir nuevo equipo
                try {
                    int newId = dbConn.getMaxEquipoId() + 1; // Obtener el ID más alto actual + 1
                    dbConn.addEquipo(new Equipo(newId, nombre, ciudad, estadioU, urlImagen));
                    JOptionPane.showMessageDialog(dialog, "Equipo añadido con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarEquipos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error al añadir equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else { // Actualizar equipo existente
                try {
                    dbConn.updateEquipo(new Equipo(equipo.getId(), nombre, ciudad, estadioU, urlImagen));
                    JOptionPane.showMessageDialog(dialog, "Equipo actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarEquipos();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error al actualizar equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private void deleteEquipo() {
        int selectedRow = equiposTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un equipo para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int equipoId = (int) equiposModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar este equipo?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dbConn.deleteEquipo(equipoId);
                cargarEquipos();
                JOptionPane.showMessageDialog(this, "Equipo eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el equipo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}