package client_package.table;

import client_package.model.ClientDTO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ClientTableModel extends AbstractTableModel {
    private List<ClientDTO> clients = new ArrayList<>();
    private final String[] columns = {"ID", "ФИО", "Телефон", "Email", "Задача"};

    public void setClients(List<ClientDTO> clients) {
        this.clients = clients;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return clients.size();
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
        ClientDTO c = clients.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> c.getId();
            case 1 -> c.getFio();
            case 2 -> c.getPhone();
            case 3 -> c.getEmail();
            case 4 -> c.getNote();
            default -> null;
        };
    }
}
