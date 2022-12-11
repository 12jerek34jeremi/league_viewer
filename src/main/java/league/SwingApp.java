package league;
import league.conectivity.DataProvider;
import league.types.League;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class SwingApp extends JFrame {
    private JComboBox<String> comboBox;
    private JLabel label;
    private String selectedItem;

    public SwingApp() {
        setTitle("Ligi piłkarskie");
        setSize(800, 600);

        // Pobranie danych o ligach
        League[] leagues = DataProvider.getLeagues();
        ArrayList <String> names = new ArrayList<>();
        for (League league : leagues) {
            names.add(league.leagueName);
        }

        // Ustawienie listy rozwijanej z nazwami lig
        String[] data = names.toArray(new String[names.size()]);
        comboBox = new JComboBox<>(data);

        // Ustawienie etykiety z tekstem
        label = new JLabel("To jest liga " + data[0]);
        selectedItem = data[0];

        add(label, BorderLayout.NORTH);
        add(comboBox, BorderLayout.EAST);

        comboBox.addActionListener(e -> updateText());

        setLayout(new GridLayout(3, 1));

        // Utworzenie trzech Buttonów
        JButton matchesButton = new JButton("Mecze");
        JButton teamsButton = new JButton("Zespoły");
        JButton playersButton = new JButton("Zawodnicy");

        add(matchesButton);
        add(teamsButton);
        add(playersButton);

        matchesButton.addActionListener(e -> label.setText("To są mecze " + selectedItem));
        teamsButton.addActionListener(e -> label.setText("To są zespoły " + selectedItem));
        playersButton.addActionListener(e -> label.setText("To są zawodnicy " + selectedItem));

        // Ustawienie okna jako widocznego
        setVisible(true);
    }

    private void updateText() {
        //Zmiana wybranego elementu
        selectedItem = (String) comboBox.getSelectedItem();
        label.setText("To jest " + selectedItem);
    }


    public static void main(String[] args) {
        new SwingApp();
    }
}

