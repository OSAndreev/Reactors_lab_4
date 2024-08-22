
import ReactorImport.DBReactorsImporter;
import Reactors.ReactorHolder;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MainWindow {

    private ReactorHolder reactorHolder = new ReactorHolder();
    private DBReactorsImporter reactorsImporter = null;

    public void ShowFrame() throws URISyntaxException, ClassNotFoundException {
        JFrame frame = new JFrame("Reactors");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JButton selectFileButton = new JButton("Выбрать файл");
        JButton countryButton = new JButton("Аггрегация по странам");
        JButton operatorButton = new JButton("Аггрегация по операторам");
        JButton regionButton = new JButton("Аггрегация по регионам");
        countryButton.setEnabled(false);
        operatorButton.setEnabled(false);
        regionButton.setEnabled(false);
        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                try {
                    fileChooser = new JFileChooser(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile());
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
                fileChooser.setDialogTitle("Выберите файл SQLite");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("SQLite Database (*.sqlite, *.db)", "sqlite", "db");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        reactorsImporter = new DBReactorsImporter(selectedFile.getAbsolutePath());
                        reactorsImporter.readReactorsInfo(reactorHolder);
                        JOptionPane.showMessageDialog(null, "Данные считаны");
                        reactorsImporter.getResult("Страна");
                        countryButton.setEnabled(true);
                        operatorButton.setEnabled(true);
                        regionButton.setEnabled(true);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Ошибка: " + ex.getMessage(), "Внимание", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        countryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String, HashMap<Integer, Double>> agregatePerCountryMap = null;
                try {
                    agregatePerCountryMap = reactorsImporter.getResult("Страна");
                } catch (SQLException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                String[] columnNames = {"Страна", "Год", "Объем ежегодного потребления"};
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);
                for (HashMap.Entry<String, HashMap<Integer, Double>> entry : agregatePerCountryMap.entrySet()) {
                    String country = entry.getKey();
                    HashMap<Integer, Double> yearData = entry.getValue();
                    int startYear = Collections.min(yearData.keySet());
                    for (int year = startYear; year <= Collections.max(yearData.keySet()); year++) {
                        Object[] rowData = new Object[3];
                        rowData[0] = country;
                        rowData[1] = year;
                        rowData[2] = yearData.getOrDefault(year, 0.0);
                        model.addRow(rowData);
                    }
                }
                JTable table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);
                JFrame tableFrame = new JFrame("Суммарное потребление по уровню аггрегации: страна");
                tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                tableFrame.setSize(800, 600);
                tableFrame.add(scrollPane);
                tableFrame.setLocationRelativeTo(null);
                tableFrame.setVisible(true);
            }
        });
        operatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String, HashMap<Integer, Double>> agregatePerCountryMap = null;
                try {
                    agregatePerCountryMap = reactorsImporter.getResult("Компания");
                } catch (SQLException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                String[] columnNames = {"Оператор", "Год", "Объем ежегодного потребления"};
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);
                for (HashMap.Entry<String, HashMap<Integer, Double>> entry : agregatePerCountryMap.entrySet()) {
                    String country = entry.getKey();
                    HashMap<Integer, Double> yearData = entry.getValue();
                    int startYear = Collections.min(yearData.keySet());
                    for (int year = startYear; year <= Collections.max(yearData.keySet()); year++) {
                        Object[] rowData = new Object[3];
                        rowData[0] = country;
                        rowData[1] = year;
                        rowData[2] = yearData.getOrDefault(year, 0.0);
                        model.addRow(rowData);
                    }
                }
                JTable table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);
                JFrame tableFrame = new JFrame("Суммарное потребление по уровню аггрегации: оператор");
                tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                tableFrame.setSize(800, 600);
                tableFrame.add(scrollPane);
                tableFrame.setLocationRelativeTo(null);
                tableFrame.setVisible(true);
            }
        });

        regionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String, HashMap<Integer, Double>> agregatePerCountryMap = null;
                try {
                    agregatePerCountryMap = reactorsImporter.getResult("Регион");
                } catch (SQLException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                String[] columnNames = {"Регион", "Год", "Объём ежегодного потребления"};
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);
                for (HashMap.Entry<String, HashMap<Integer, Double>> entry : agregatePerCountryMap.entrySet()) {
                    String country = entry.getKey();
                    HashMap<Integer, Double> yearData = entry.getValue();
                    int startYear = Collections.min(yearData.keySet());
                    for (int year = startYear; year <= Collections.max(yearData.keySet()); year++) {
                        Object[] rowData = new Object[3];
                        rowData[0] = country;
                        rowData[1] = year;
                        rowData[2] = yearData.getOrDefault(year, 0.0);
                        model.addRow(rowData);
                    }
                }
                JTable table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);
                JFrame tableFrame = new JFrame("Суммарное потребление по уровню аггрегации: регион");
                tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                tableFrame.setSize(800, 600);
                tableFrame.add(scrollPane);
                tableFrame.setLocationRelativeTo(null);
                tableFrame.setVisible(true);
            }
        });
        panel.add(selectFileButton);
        panel.add(countryButton);
        panel.add(operatorButton);
        panel.add(regionButton);
        frame.getContentPane().add(panel);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
