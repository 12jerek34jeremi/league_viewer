package league.panels;

import league.conectivity.DataProvider;
import javax.swing.*;

public abstract class LeaguePanel extends JPanel{
    public abstract void changeLeague(DataProvider dataProvider);
}
