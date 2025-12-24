package client_package.table;

import client_package.model.EmployeeDTO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class EmployeeTableModel extends AbstractTableModel {
    private List<EmployeeDTO> employees = new ArrayList<EmployeeDTO>();
    private final String[] columns = {"ID", "ФИО", "Должность", "Email", "Телефон"};

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        EmployeeDTO c = employees.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> c.getId();
            case 1 -> c.getFio();
            case 2 -> c.getPosition();
            case 3 -> c.getEmail();
            case 4 -> c.getPhone();
            default -> null;
        };
    }
}
