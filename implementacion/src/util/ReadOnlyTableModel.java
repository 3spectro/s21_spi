package util;

import javax.swing.table.DefaultTableModel;

public class ReadOnlyTableModel extends DefaultTableModel {
    public ReadOnlyTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
