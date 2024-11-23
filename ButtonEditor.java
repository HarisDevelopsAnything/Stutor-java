package stutor; // Same package as TeacherHome or a sub-package

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);

        // Set up the button action listener to open the file
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the selected row index
                JTable table = (JTable) getComponent().getParent();
                int row = table.getSelectedRow();
                String filePath = (String) table.getValueAt(row, 1); // Assuming file path is in column 1 (Date of Upload)
                try {
                    java.awt.Desktop.getDesktop().open(new java.io.File(filePath)); // Open file using default desktop app
                } catch (java.io.IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText("Open File"); // Set the button text
        return button;
    }
}
