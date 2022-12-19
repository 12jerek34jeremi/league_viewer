package league.panels;

import league.conectivity.DataProvider;
import league.types.SimplePlayer;
import javax.swing.*;
import java.awt.*;

public class PlayersPanel extends LeaguePanel{

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

    //Ta metoda jest tylko tymczasowa, żeby sprawdzic czy dziala, nalezy ja napisac od nowa.
    @Override
    void launchNewWindow(int playerIndex){
        System.out.println("W Players, zostałem kliknięty.");
        System.out.println("Indeks gracza to: " + playerIndex);

        JFrame frame = new JFrame("Test");
        frame.setSize(600, 600);
        frame.add(new JLabel("To jest okno z graczem, które implementuje Paweł."));
        frame.setVisible(true);
    }
}
