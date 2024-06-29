import Reactors.Reactor;
import Reactors.ReactorsOwner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import static ReactorImport.UniversalReactorImporter.createImporterChain;

public class MainWindow extends JFrame {

    private JButton importBtn;
    private JPanel mainPanel;
    private JTree reactorsJTree;
    private ReactorsOwner reactorsOwner = new ReactorsOwner();

    public MainWindow() {
        mainPanel = new JPanel();
        reactorsJTree = new JTree();
        importBtn = new JButton("Импортировать");

        addComponentsListeners();

        DefaultTreeModel treeModel = (DefaultTreeModel) reactorsJTree.getModel();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Реакторы");
        treeModel.setRoot(root);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(importBtn);
        mainPanel.add(new JScrollPane(reactorsJTree));

        setContentPane(mainPanel);
        setTitle("Reactors");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void addComponentsListeners() {
        importBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Файлы .yaml / .xml / .json", "yaml", "xml", "json");
                fileChooser.setFileFilter(filter);
                fileChooser.setCurrentDirectory(new File("./"));

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    if (!(file.getName().toLowerCase().endsWith(".xml") ||
                            file.getName().toLowerCase().endsWith(".yaml") ||
                            file.getName().toLowerCase().endsWith(".json"))) {
                        JOptionPane.showMessageDialog(
                                null, "Выберите файл формата .yaml / .xml / .json", "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    reactorsOwner.getReactorMap().clear();
                    createImporterChain().processFile(file, reactorsOwner);
                    updateTree();
                    importBtn.setText("Файл: " + file.getName());
                }
            }
        });

        reactorsJTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() != 2) return;

                TreePath selectionPath = reactorsJTree.getSelectionPath();
                if (selectionPath == null) return;

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
                String nodeText = selectedNode.getUserObject().toString();
                if ("Реакторы".equals(nodeText)) return;

                Reactor selectedReactor =

                        (Reactor) selectedNode.getUserObject();
                JOptionPane.showMessageDialog(
                        null,
                        selectedReactor.getDetailedDescription(),
                        "Подробности об реакторе",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void updateTree() {
        DefaultTreeModel treeModel = (DefaultTreeModel) reactorsJTree.getModel();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Реакторы");

        for (String type : reactorsOwner.getReactorMap().keySet()) {
            DefaultMutableTreeNode reactorNode = new DefaultMutableTreeNode(reactorsOwner.getReactorMap().get(type));
            root.add(reactorNode);
        }

        treeModel.setRoot(root);
        reactorsJTree.setEnabled(true);
    }
}
