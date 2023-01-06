package league.panels;

import league.conectivity.DataProvider;
import league.types.SimpleTeam;
import javax.swing.*;
import java.awt.*;

public class TeamsPanel extends LeagueViewingPanel{
    public TeamsPanel(){
        super(
            new String[]{"Nazwa Drużyny"},
            "Tu będzie lista drużyn jak użytkownik wybierze ligę.");
    }

    @Override
    protected IndexButton fillElementsPanel(DataProvider dataProvider, JPanel elementsPanel){
        SimpleTeam[] teams  = dataProvider.getTeams();

        for(SimpleTeam team : teams){
            JPanel teamsPanel = new JPanel(new GridLayout(1, 2));
            IndexButton button = new IndexButton("Zobacz drużynę.", team.teamID);
            button.addActionListener(this);

            teamsPanel.add(new JLabel(team.teamName));
            teamsPanel.add(button);

            elementsPanel.add(teamsPanel);
        }
        return (IndexButton) ((JPanel) elementsPanel.getComponent(0)).getComponent(1);
    }

    @Override
    void launchNewWindow(int teamIndex){
        System.out.println("W Teams, zostałem kliknięty");
        System.out.println("Indeks druzyny to: " + teamIndex);
        FullTeam team = dataProvider.getTeam(teamIndex);

        JFrame frame = new JFrame(team.teamName);
        JPanel teamDataPanel = teamDataPanel(team);
        JPanel teamPlayerPanel = teamPlayerPanel(team, frame);
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Informacje", teamDataPanel);
        tabbedPane.add("Zawodnicy", teamPlayerPanel);
        frame.add(tabbedPane);

        frame.setSize(300, 300);

        frame.setVisible(true);
    }

    private JPanel teamDataPanel(FullTeam team){
        JPanel teamData = new JPanel();
        teamData.add(new JLabel("Nazwa: " + team.teamName));
        teamData.add(new JLabel("Kraj: " + team.origins));
        return teamData;
    }

    private JPanel teamPlayerPanel(FullTeam team, JFrame frame){
        JPanel teamPlayerPanel = new JPanel();
        int teamId = team.teamID;
        JPanel elementsPanel = new JPanel(new GridLayout(20, 1));
        SimplePlayer[] players = dataProvider.getPlayers();
        if(players == null) showMessageAndExit(frame);
        for (SimplePlayer player : players){
            if (player.teamId == teamId){
                JPanel data = new JPanel();
                data.add(new JLabel(player.firstName));
                data.add(new JLabel(player.lastName));
                elementsPanel.add(data);
            }
        }
        JPanel header = new JPanel();
        header.add(new JLabel("Imię"));
        header.add(new JLabel("Nazwisko"));

        teamPlayerPanel.add(header, BorderLayout.PAGE_START);
        teamPlayerPanel.add(elementsPanel, BorderLayout.CENTER);

        return teamPlayerPanel;
    }

    private void showMessageAndExit(JFrame frame){
        JOptionPane.showMessageDialog(frame,"Connection with database lost.\n Check Your internet connection and try again.");
        System.exit(0);
    }
}
