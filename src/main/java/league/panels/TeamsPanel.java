package league.panels;

import league.conectivity.DataProvider;
import league.types.SimpleTeam;
import javax.swing.*;
import java.awt.*;

public class TeamsPanel extends LeaguePanel{
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

    //Ta metoda jest tylko tymczasowa, żeby sprawdzic czy dziala, nalezy ja napisac od nowa.
    @Override
    void launchNewWindow(int teamIndex){
        System.out.println("W Teams, zostałem kliknięty");
        System.out.println("Indeks druzyny to: " + teamIndex);

        JFrame frame = new JFrame("Test");
        frame.setSize(600, 600);
        frame.add(new JLabel("To jest okno z drużyną, które implementuje Paweł."));
        frame.setVisible(true);
    }
}
