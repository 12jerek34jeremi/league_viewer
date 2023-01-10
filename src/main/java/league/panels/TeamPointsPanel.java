package league.panels;

import league.conectivity.DataProvider;
import league.types.*;
import javax.swing.*;
import java.awt.*;

public class TeamPointsPanel extends LeagueViewingPanel{

    public TeamPointsPanel() {
        super(new String[]{"Nazwa drużyny", "Liczba punktów", "Rozegrane mecze", "Wygrane", "Zremisowane", "Przegrane"},
                "Tu będzie tabela ligi, jak użytkownik wybierze ligę");
    }

    @Override
    protected IndexButton fillElementsPanel(DataProvider dataProvider, JPanel elementsPanel){
        SimpleTeam[] teams = dataProvider.getTeams();
        if(teams.length == 0){
            elementsPanel.add(new JLabel("W tej lidze nie ma żadnych drużyn."));
            return new IndexButton("", -1);
        }

        for(SimpleTeam team : teams){
            TeamPoints teamPoints = dataProvider.count_team_points(team.teamID);
            JPanel teamPointsPanel = new JPanel(new GridLayout(1, 7));
            IndexButton button = new IndexButton("Zobacz wyniki", team.teamID);
            button.addActionListener(this);

            teamPointsPanel.add(new JLabel(team.teamName));
            teamPointsPanel.add(new JLabel(String.valueOf(teamPoints.points)));
            teamPointsPanel.add(new JLabel(String.valueOf(teamPoints.wonMatches + teamPoints.drawMatches + teamPoints.lostMatches)));
            teamPointsPanel.add(new JLabel(String.valueOf(teamPoints.wonMatches)));
            teamPointsPanel.add(new JLabel(String.valueOf(teamPoints.drawMatches)));
            teamPointsPanel.add(new JLabel(String.valueOf(teamPoints.lostMatches)));
            teamPointsPanel.add(button);

            elementsPanel.add(teamPointsPanel);
        }
        return (IndexButton) ((JPanel) elementsPanel.getComponent(0)).getComponent(6);
    }

    @Override
    void launchNewWindow(int teamIndex) {
        JFrame frame = new JFrame();
        FullTeam fullTeam = dataProvider.getTeam(teamIndex);
        frame.setTitle(fullTeam.teamName + " - mecze");
        Match[] matches = fullTeam.matches;
        JTabbedPane tabbedPane = matchesTabbedPane(matches, teamIndex);

        frame.add(tabbedPane);

        frame.setSize(300,300);
        frame.setVisible(true);
    }

    private JTabbedPane matchesTabbedPane(Match[] matches, int teamIndex){
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel winHeader = new JPanel(new GridLayout(1,3));
        winHeader.add(new JLabel("Przeciwnik"));
        winHeader.add(new JLabel("Bramki strzelone"));
        winHeader.add(new JLabel("Bramki stracone"));

        JPanel drawHeader = new JPanel(new GridLayout(1,3));
        drawHeader.add(new JLabel("Przeciwnik"));
        drawHeader.add(new JLabel("Bramki strzelone"));
        drawHeader.add(new JLabel("Bramki stracone"));

        JPanel lostHeader = new JPanel(new GridLayout(1,3));
        lostHeader.add(new JLabel("Przeciwnik"));
        lostHeader.add(new JLabel("Bramki strzelone"));
        lostHeader.add(new JLabel("Bramki stracone"));

        JPanel winMatchesPanel = new JPanel(new BorderLayout());
        JPanel drawMatchesPanel = new JPanel(new BorderLayout());
        JPanel lostMatchesPanel = new JPanel(new BorderLayout());

        JPanel elementsWinPanel = new JPanel();
        BoxLayout elementsWinPanelLayout = new BoxLayout(elementsWinPanel, BoxLayout.PAGE_AXIS);
        elementsWinPanel.setLayout(elementsWinPanelLayout);

        JPanel elementsDrawPanel = new JPanel();
        BoxLayout elementsDrawPanelLayout = new BoxLayout(elementsDrawPanel, BoxLayout.PAGE_AXIS);
        elementsDrawPanel.setLayout(elementsDrawPanelLayout);

        JPanel elementsLostPanel = new JPanel();
        BoxLayout elementsLostPanelLayout = new BoxLayout(elementsLostPanel, BoxLayout.PAGE_AXIS);
        elementsLostPanel.setLayout(elementsLostPanelLayout);

        int current_goals = 0, other_goals = 0;

        for(Match match : matches){
            String[] goals = match.score.split(":");
            if(match.firstTeamId == teamIndex){
                current_goals = Integer.parseInt(goals[0]);
                other_goals = Integer.parseInt(goals[1]);
            } else {
                current_goals = Integer.parseInt(goals[1]);
                other_goals = Integer.parseInt(goals[0]);
            }

            JPanel data = new JPanel(new GridLayout(1, 3));

            if (match.firstTeamId == teamIndex) data.add(new JLabel(match.secondTeamName));
            else if (match.secondTeamId == teamIndex) data.add(new JLabel(match.firstTeamName));
            data.add(new JLabel(String.valueOf(current_goals)));
            data.add(new JLabel(String.valueOf(other_goals)));

            if(current_goals > other_goals) elementsWinPanel.add(data);
            else if (current_goals == other_goals) elementsDrawPanel.add(data);
            else if (current_goals < other_goals) elementsLostPanel.add(data);
        }

        JScrollPane winScrollPane = new JScrollPane(elementsWinPanel);
        JScrollPane drawScrollPane = new JScrollPane(elementsDrawPanel);
        JScrollPane lostScrollPane = new JScrollPane(elementsLostPanel);

        winScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        winScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        drawScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        drawScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        lostScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        lostScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        winMatchesPanel.add(winHeader, BorderLayout.PAGE_START);
        winMatchesPanel.add(winScrollPane, BorderLayout.CENTER);

        drawMatchesPanel.add(drawHeader, BorderLayout.PAGE_START);
        drawMatchesPanel.add(drawScrollPane, BorderLayout.CENTER);

        lostMatchesPanel.add(lostHeader, BorderLayout.PAGE_START);
        lostMatchesPanel.add(lostScrollPane, BorderLayout.CENTER);

        tabbedPane.add("Wygrane", winMatchesPanel);
        tabbedPane.add("Zremisowane", drawMatchesPanel);
        tabbedPane.add("Przegrane", lostMatchesPanel);

        return tabbedPane;
    }
}
