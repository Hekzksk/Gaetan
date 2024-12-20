package gaetan.nana;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class Movie {
    
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<ObjectItem> objectList;
    private static final String FILE_NAME = "C:\\Users\\FALCON COMPUTERS\\eclipse-workspace\\gaetan\\src\\gaetan\\nana\\objects.txt";

    public Movie() {
        objectList = new ArrayList<>();
        model = new DefaultTableModel(new Object[]{"Titre", "Auteur"}, 0);
        table = new JTable(model);
        loadObjectsFromFile();

        frame = new JFrame("Gestion de Location");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Gestion de Location de Films", JLabel.CENTER);
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        frame.add(headerLabel, BorderLayout.NORTH);

        table.setFillsViewportHeight(true);
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.setRowHeight(30);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Ajouter");
        styleButton(addButton);
        addButton.addActionListener(this::addObject);

        JButton removeButton = new JButton("Supprimer");
        styleButton(removeButton);
        removeButton.addActionListener(this::removeObject);

        JButton editButton = new JButton("Modifier");
        styleButton(editButton);
        editButton.addActionListener(this::editObject);

        panel.add(addButton);
        panel.add(removeButton);
        panel.add(editButton);

        frame.add(panel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(120, 40));
    }

    private void loadObjectsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    String title = parts[0].replace("Titre : ", "");
                    String author = parts[1].replace("Auteur : ", "");
                    objectList.add(new ObjectItem(title, author));
                    model.addRow(new Object[]{title, author});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveObjectsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (ObjectItem object : objectList) {
                bw.write("Titre : " + object.getTitle() + ", Auteur : " + object.getAuthor());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addObject(ActionEvent e) {
        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 10, 10));
        inputPanel.add(new JLabel("Titre:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Auteur:"));
        inputPanel.add(authorField);

        int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Ajouter un film", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String author = authorField.getText();
            if (!title.isEmpty() && !author.isEmpty()) {
                ObjectItem object = new ObjectItem(title, author);
                objectList.add(object);
                model.addRow(new Object[]{title, author});
                saveObjectsToFile();
            }
        }
    }

    private void removeObject(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Voulez-vous vraiment supprimer ce film ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                objectList.remove(selectedRow);
                model.removeRow(selectedRow);
                saveObjectsToFile();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Sélectionnez un objet à supprimer.");
        }
    }

    private void editObject(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            JTextField titleField = new JTextField((String) model.getValueAt(selectedRow, 0), 20);
            JTextField authorField = new JTextField((String) model.getValueAt(selectedRow, 1), 20);

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(2, 2, 10, 10));
            inputPanel.add(new JLabel("Titre:"));
            inputPanel.add(titleField);
            inputPanel.add(new JLabel("Auteur:"));
            inputPanel.add(authorField);

            int result = JOptionPane.showConfirmDialog(frame, inputPanel, "Modifier un film", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String newTitle = titleField.getText();
                String newAuthor = authorField.getText();
                if (!newTitle.isEmpty() && !newAuthor.isEmpty()) {
                    ObjectItem object = objectList.get(selectedRow);
                    object.setTitle(newTitle);
                    object.setAuthor(newAuthor);
                    model.setValueAt(newTitle, selectedRow, 0);
                    model.setValueAt(newAuthor, selectedRow, 1);
                    saveObjectsToFile();
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Sélectionnez un objet à modifier.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Movie::new);
    }
}
