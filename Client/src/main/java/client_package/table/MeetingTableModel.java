package client_package.table;

import client_package.model.EmployeeDTO;
import client_package.model.MeetingDTO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MeetingTableModel extends AbstractTableModel {
    private List<MeetingDTO> meetings = new ArrayList<MeetingDTO>();
    private final String[] columns = {"ID", "ФИО клиента", "ФИО работника" , "Статус", "Место", "Комментарий"};

    public void setMeetings(List<MeetingDTO> meetings) {
        this.meetings = meetings;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return meetings.size();
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
        MeetingDTO c = meetings.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> c.getId();
            case 1 -> c.getClientFio();
            case 2 -> c.getEmployeeFio();
            case 3 -> c.getDatetime();
            case 4 -> c.getStatus();
            case 5 -> c.getPlace();
            case 6 -> c.getComment();
            default -> null;
        };
    }
}
