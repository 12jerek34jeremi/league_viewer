package league;
import league.conectivity.DataProvider;
import league.panels.*;
import league.types.League;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class SwingApp{
    JMenu menu;
    JFrame frame;
    ArrayList<JMenuItem> items;
    HashMap<String, Integer> leaguesData;
    JMenuBar mb;
    LeaguePanel[] leaguePanels;
    DataProvider dataProvider;
    String chosenLeagueName;
    JTabbedPane tabbedPane;
    JTextField textField;
    public SwingApp() {
        //creating leagues to run dataProvider

        frame = new JFrame();
        League[] leagues = DataProvider.getLeagues();
        if(leagues == null) showMessageAndExit(frame);

        if(!DataProvider.prepareData()) showMessageAndExit(frame);

        League firstLeague = leagues[0];

        //initializing variables

        mb = new JMenuBar();
        menu = new JMenu("Wybierz Ligę");
        items = new ArrayList<>();
        leaguesData = new HashMap<String, Integer>();

        // creating JTabbedPane and its Pane's
        tabbedPane = new JTabbedPane();
        leaguePanels = new LeaguePanel[]{new MatchesPanel(), new TeamsPanel(), new PlayersPanel(), new AddingPanel()};//LINE MODIFIED BY JCh

        tabbedPane.add("mecze", leaguePanels[0]);
        tabbedPane.add("zespoły", leaguePanels[1]);
        tabbedPane.add("zawodnicy", leaguePanels[2]);
        tabbedPane.add("Dodaj", leaguePanels[3]);

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

                    for (LeaguePanel leaguePanel: leaguePanels){
                        leaguePanel.changeLeague(dataProvider);
                    }
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

