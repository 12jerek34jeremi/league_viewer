package league.panels;

import league.conectivity.DataProvider;
import league.types.Match;
import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MatchesPanel extends LeagueViewingPanel{
    public MatchesPanel(){
        super(
                new String[]{"Drużyna A", "Drużyna B", "Lokacja", "Wynik", "Data"},
                "Tu będzie lista meczy jak użytkownik wybierze ligę.");
    }

    @Override
    protected IndexButton fillElementsPanel(DataProvider dataProvider, JPanel elementsPanel){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Match[] matches  = dataProvider.getMatches();

        for(Match match : matches){
            JPanel matchPanel = new JPanel(new GridLayout(1, 6));
            IndexButton button = new IndexButton("Zobacz mecz.", match.match_id);
            button.addActionListener(this);

            matchPanel.add(new JLabel(match.firstTeamName));
            matchPanel.add(new JLabel(match.secondTeamName));
            matchPanel.add(new JLabel(match.location));
            matchPanel.add(new JLabel(match.score));
            matchPanel.add(new JLabel(dateFormat.format(match.date)));
            matchPanel.add(button);

            elementsPanel.add(matchPanel);
        }

        return (IndexButton) ((JPanel) elementsPanel.getComponent(0)).getComponent(5);
    }

    //Ta metoda jest tylko tymczasowa, żeby sprawdzic czy dziala, nalezy ja napisac od nowa.
    @Override
    void launchNewWindow(int matchIndex){
        System.out.println("W Matches, zostałem kliknięty.");
        System.out.println("Indeks meczu to: " + matchIndex);

        JFrame frame = new JFrame("Test");
        frame.setSize(600, 600);
        frame.add(new JLabel("To jest okno z meczem, które implementuje Paweł."));
        frame.setVisible(true);
    }
}
