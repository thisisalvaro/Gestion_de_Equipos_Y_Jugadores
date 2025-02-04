package clase.Futbol_App;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ImageRenderer extends JLabel implements TableCellRenderer {

    public ImageRenderer() {
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        if (value != null) {
            setIcon((Icon) value);
            // Ajusta la altura de la fila según la altura de la imagen
            table.setRowHeight(row, ((ImageIcon) value).getIconHeight());
        } else {
            setIcon(null);
        }
        return this;
    }
}