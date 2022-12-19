package league;
import league.conectivity.DataProvider;
import league.panels.LeaguePanel;
import league.panels.MatchesPanel;
import league.panels.PlayersPanel;
import league.panels.TeamsPanel;
import league.types.League;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class SwingApp{
    JMenu menu;
    JFrame frame;
    ArrayList<JMenuItem> items;
    HashMap<String, Integer> leaguesData;
    JMenuBar mb;
    LeaguePanel teams, matches, players;
    DataProvider dataProvider;
    String chosenLeagueName;
    JTabbedPane tabbedPane;
    JTextField textField;
    public SwingApp() {
        //creating leagues to run dataProvider

        frame = new JFrame();
        League[] leagues = DataProvider.getLeagues();
        if(leagues == null) showMessageAndExit(frame);

        League firstLeague = leagues[0];
        chosenLeagueName = firstLeague.leagueName;

        //initializing variables

        mb = new JMenuBar();
        menu = new JMenu(chosenLeagueName + " (zmień ligę)");
        items = new ArrayList<>();
        leaguesData = new HashMap<String, Integer>();

        // creating JTabbedPane and its Pane's

        tabbedPane = new JTabbedPane();
        teams = new TeamsPanel();  //LINE MODIFIED BY JCh
        matches = new MatchesPanel();  //LINE MODIFIED BY JCh
        players = new PlayersPanel();  //LINE MODIFIED BY JCh
        tabbedPane.add("mecze", matches);
        tabbedPane.add("zespoły", teams);
        tabbedPane.add("zawodnicy", players);
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedComponent().equals(matches)) {
                    textField = new JTextField("mecze");
                    textField.setVisible(true);
                    matches.add(textField);
                } else if (tabbedPane.getSelectedComponent().equals(teams)) {
                    textField = new JTextField("zespoły");
                    textField.setVisible(true);
                    teams.add(textField);
                } else if (tabbedPane.getSelectedComponent().equals(players)) {
                    textField = new JTextField("zawodnicy");
                    textField.setVisible(true);
                    players.add(textField);
                }
            }
        });

        //creating a MenuBar with its items

        for (League league : leagues) {
            JMenuItem menuItem = new JMenuItem(league.leagueName);
            items.add(menuItem);
            leaguesData.put(league.leagueName, league.leagueId);
        }

        for (JMenuItem item : items) {
            menu.add(item);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JMenuItem item = (JMenuItem) e.getSource();
                    String name = item.getText();
                    Integer leagueID = leaguesData.get(name);
                    dataProvider = DataProvider.getDataProvider(leagueID);
                    if(dataProvider == null) showMessageAndExit(frame);
                    menu.setText(name + " (zmień ligę)");

                    players.changeLeague(dataProvider);
                    players.changeLeague(dataProvider);
                    players.changeLeague(dataProvider);
                }
            });
        }

        mb.add(menu);
        frame.add(tabbedPane);
        frame.setTitle("Ligi piłkarskie");
        frame.setJMenuBar(mb);
        frame.setLayout(new GridLayout());
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public static void main(String args[]) {
        new SwingApp();
    }
    private void showMessageAndExit(JFrame frame){
        JOptionPane.showMessageDialog(frame,"Connection with database lost.\n Check Your internet connection and try again.");
        System.exit(0);
    }
}

