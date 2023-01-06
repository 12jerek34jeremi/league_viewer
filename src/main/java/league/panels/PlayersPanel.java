package league.panels;

import league.conectivity.DataProvider;
import league.types.SimplePlayer;
import javax.swing.*;
import java.awt.*;

public class PlayersPanel extends LeagueViewingPanel{

    public PlayersPanel(){
        super(
                new String[]{"Imię", "Nazwisko", "Drużyna"},
                "Tu będzie lista piłkarzy jak uzytkownik wybierze lige");
    }


    @Override
    protected IndexButton fillElementsPanel(DataProvider dataProvider, JPanel elementsPanel){
        SimplePlayer[] players  = dataProvider.getPlayers();

        for(SimplePlayer player : players){
            JPanel playerPanel = new JPanel(new GridLayout(1, 4));
            IndexButton button = new IndexButton("See player", player.playerId);
            button.addActionListener(this);

            playerPanel.add(new JLabel(player.firstName));
            playerPanel.add(new JLabel(player.lastName));
            playerPanel.add(new JLabel(player.teamName));
            playerPanel.add(button);

            elementsPanel.add(playerPanel);
        }

        return (IndexButton) ((JPanel) elementsPanel.getComponent(0)).getComponent(3);
    }

 
    void launchNewWindow(int playerIndex){
        System.out.println("W Players, zostałem kliknięty.");
        System.out.println("Indeks gracza to: " + playerIndex);

        JFrame frame = new JFrame("Zawodnik " + playerIndex);

        Match[] matches = findMatches(playerIndex, frame);
        JPanel playerMatchesPanel = playerMatchesPanel(matches);

        JPanel playerDataPanel = playerDataPanel(playerIndex, frame);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Informacje", playerDataPanel);
        tabbedPane.add("Mecze", playerMatchesPanel);
        frame.add(tabbedPane);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }

    private Match[] findMatches(int playerIndex, JFrame frame){
        FullPlayer player = dataProvider.getPlayer(playerIndex);
        if(player == null) showMessageAndExit(frame);
        int teamId = player.teamId;
        FullTeam team = dataProvider.getTeam(teamId);
        if(team == null) showMessageAndExit(frame);
        return team.matches;
    }

    private JPanel playerMatchesPanel(Match[] matches){
        JPanel matchesPanel = new JPanel();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String[] columnsName = {"Drużyna A", "Drużyna B", "Lokacja", "Wynik", "Data"};
        JPanel header = new JPanel(new GridLayout(1, columnsName.length));
        for (String name : columnsName){
            header.add(new JLabel(name));
        }
        JPanel elementsPanel = new JPanel(new GridLayout());
        LinkedList<JPanel> matchesPanels = new LinkedList<JPanel>();
        for (Match match : matches){
            JPanel matchPanel = new JPanel(new GridLayout(1, 6));
            matchPanel.add(new JLabel(match.firstTeamName));
            matchPanel.add(new JLabel(match.secondTeamName));
            matchPanel.add(new JLabel(match.location));
            matchPanel.add(new JLabel(match.score));
            matchPanel.add(new JLabel(dateFormat.format(match.date)));
            matchesPanels.add(matchPanel);
        }

        for (JPanel matchPanel : matchesPanels){
            elementsPanel.add(matchPanel);
            System.out.println("dodaję panel z meczem");
        }

        JScrollPane scrollPane = new JScrollPane(elementsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        matchesPanel.add(header, BorderLayout.PAGE_START);
        matchesPanel.add(scrollPane, BorderLayout.CENTER);

        return matchesPanel;
    }

    private JPanel playerDataPanel(int playerIndex, JFrame frame){
        FullPlayer player = dataProvider.getPlayer(playerIndex);
        if(player == null) showMessageAndExit(frame);
        JPanel dataPanel = new JPanel(new GridLayout(7, 1));
        dataPanel.add(new JLabel("Imię " + player.firstName));
        dataPanel.add(new JLabel("Nazwisko " + player.lastName));
        dataPanel.add(new JLabel("Zespół " + player.teamName));
        dataPanel.add(new JLabel("Pochodzenie " + player.origin));
        dataPanel.add(new JLabel("Wzrost " + player.height));
        dataPanel.add(new JLabel("Waga " + player.weight));
        dataPanel.add(new JLabel("Wiek " + player.age));

        return dataPanel;
    }

    private void showMessageAndExit(JFrame frame){
        JOptionPane.showMessageDialog(frame,"Connection with database lost.\n Check Your internet connection and try again.");
        System.exit(0);
    }
}
