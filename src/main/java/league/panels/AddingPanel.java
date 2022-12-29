package league.panels;

import league.conectivity.DataProvider;
import league.types.Indexer;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class AddingPanel extends LeaguePanel implements ItemListener {

    private final CardLayout cardLayout;
    private final JPanel cardContainer;
    private final JComboBox actionsBox;
    String[] layoutsNames;
    InputsPanel newPlayerPanel, newMatchPanel, newLeaguePanel, newTeamPanel;
    private DataProvider dataProvider = null;


    public AddingPanel(){

        //ARRAYS CREATION
        String[] actions = new String[]{
                "Dodaj nowego zawodnika do drużyny.",
                "Dodaj nowy mecz do obecnej ligi.",
                "Utwórz nową drużynę.",
                "Utwórz nową ligę."
        };
        layoutsNames = new String[]{"newPlayerPanel", "newMatchPanel",  "newTeamPanel",  " newLeaguePanel"};

        //CONTAINERS CREATIONS:
        newPlayerPanel = new InputsPanel(
            new String[]{"Podaj imię", "Podaj nazwisko"},
            new String[]{"Podaj wiek", "Podaj wzrost", "Podaj wage"},
            new String[]{"Wybierz drużynę"},
            "Wprowadz zawodnika do systemu",
            25,
            15,
            ((textFields, spinners, autoComboBoxes) ->{
                if(dataProvider == null) return "Wybierz lige aby wybrać zawodnika.";

                String name = textFields[0].getText(), lastName = textFields[1].getText(),
                    ageS  = spinners[0].getValue().toString(), heightS = spinners[1].getValue().toString(),
                    weightS = spinners[2].getValue().toString();
                    Object selectedTeam = autoComboBoxes[0].getSelectedItem();

                if(name.isEmpty() || lastName.isEmpty()) return "Wprowadz imie i nazwisko!";

                if(!StringUtils.isAlphaSpace(name)) return "Nieprawidłowe imię!";
                if(!StringUtils.isAlphaSpace(lastName)) return "Nieprawidłowe nazwisko!";

                int age, height, weight;

                try { age = Integer.parseInt(ageS); }catch (Exception e){ return "Wiek nie jest liczbą naturalną!"; }
                try { height = Integer.parseInt(heightS); }catch (Exception e){ return "Wzrost nie jest liczbą naturalną!"; }
                try { weight = Integer.parseInt(weightS); }catch (Exception e){ return "Waga nie jest liczbą! naturalną"; }

                if(age < 3 || age > 85) return "Wiek zawodnika musi należeć do <3, 85>";
                if(height < 100 || height > 250) return "Wzrost zawodnika musi należeć do <100, 250>";
                if(weight < 30 || weight > 225) return "Waga zawodnika musi należeć do <30, 225>";

                if(selectedTeam == null) return "Wybierz drużnę.";

                Indexer team = findIndexer(dataProvider.getTeams(), selectedTeam.toString());
                if(team == null) return "Wyierz istniejącą drużynę!";

                System.out.println("Do systemu zostanie wprowadzony zawodnik: \n" +
                        "imie: " + name + ", nazwisko: " + lastName + "\n" +
                        "wiek: " + ageS + ", wzrost " + heightS + ", waga" + weightS + "\n"
                        + "drużyna (id / name): " + team.toIndex() + "/" +  team.toString()
                );

                return null;
            })
        );

        newMatchPanel = new InputsPanel(
            new String[]{"Data meczu (format YYYY-MM-YY)"},
            new String[]{"Bramki drużyny pierwszej", "Bramki drużyny drugiej"},
            new String[]{"Wybierz pierwszą drużynę", "Wybierz drugą drużynę", "Wybierz stadion"},
            "Dodaj nowy mecz do ligi.",
            25,
            15,
            (textFields, spinners, autoComboBoxes) -> {
                if(dataProvider == null) return "Wybierz ligę aby wprowadzić nowy mecz do ligi.";

                String dateS = textFields[0].getText(),
                    firstTeamGoalsS = spinners[0].getValue().toString(), secondTeamGoalsS = spinners[1].getValue().toString();
                Object firstTeamO = autoComboBoxes[0].getSelectedItem(),
                    secondTeamO = autoComboBoxes[1].getSelectedItem(),  stadionO = autoComboBoxes[2].getSelectedItem();

                if(dateS.isEmpty()) return "Wprowadz date meczu!";
                try{
                    LocalDate.parse(dateS, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }catch (Exception e){
                    return "Nie rozumiem tej daty!";
                }

                int firstTeamGoals, secondTeamGoals;
                try { firstTeamGoals = Integer.parseInt(firstTeamGoalsS); }
                    catch (Exception e){ return "Bramki pierwszej drużyny nie są liczbą naturalną!"; }
                try { secondTeamGoals = Integer.parseInt(secondTeamGoalsS); }
                    catch (Exception e){ return "Bramki drugiej drużyny nie są liczbą naturalną!"; }

                if(firstTeamGoals<0 || firstTeamGoals>25) return "Aż tyle bramek strzeliła drużyna pierwsza?";
                if(secondTeamGoals<0 || secondTeamGoals>25) return "Aż tyle bramek strzeliła drużyna druga?";

                if(firstTeamO == null) return "Wybierz pierwszą drużynę";
                if(secondTeamO == null) return "Wybierz pierwszą drużynę";

                Indexer firstTeam = findIndexer(dataProvider.getTeams(), firstTeamO.toString());
                if(firstTeam == null) return "Wybierz istniejącą pierwszą drużynę";
                Indexer secondTeam = findIndexer(dataProvider.getTeams(), secondTeamO.toString());
                if(secondTeam == null) return "Wybierz istniejącą drugą drużynę";
                if(firstTeam.toIndex() == secondTeam.toIndex()) return "Drużyna gra sama z sobą?";

                if(stadionO == null) return "Wubierz station.";
                Indexer stadion = findIndexer(DataProvider.getStadiums(), stadionO.toString());
                if(stadion == null) return "Wybierz istniejący staion.";

                System.out.println("Do systemu zostanie wprowadzony taki mecz: \n" +
                    "data: " + dateS + "\n" +
                    "bramki pierwszej drużyny: " + firstTeamGoals + ", bramkiDrugiej " + secondTeamGoals + "\n" +
                    "drużyna 1 (id / name): " + firstTeam.toIndex() + "/" +  firstTeam.toString() + "\n" +
                    "drużyna 2 (id / name): " + secondTeam.toIndex() + "/" +  secondTeam.toString() + "\n" +
                    "stadion(id / name): " + stadion.toIndex() + "/" +  stadion.toString()
                );

                return null;
            }
        );


        newTeamPanel = new InputsPanel(
            new String[]{"Nazwa drużyny"},
            null,
            new String[]{"Wybierz kraj pochodzenia", },
            "Utwórz nową drużynę.",
            25,
            0,
            (textFields, spinners, autoComboBoxes) -> {
                String teamName = textFields[0].getText();
                Object countryO = autoComboBoxes[0].getSelectedItem();

                if (teamName.isEmpty()) return "Wprowadz nazwe druzyny";

                if(countryO == null) return "Wybierz kraj pochodzenia!";
                Indexer country = findIndexer(DataProvider.getCountries(), countryO.toString());
                if(country == null) return "Wybierz kraj z listy.";

                System.out.println("Wprowadzone zostana te dane:\n" +
                    "nazwa drużyny: " + teamName + "\n" +
                    "kraj poch. (id/nazwa): " + country.toIndex() + " / " + country.toString()
                );

                return null;
            }
        );

        newLeaguePanel = new InputsPanel(
            new String[]{"Nazwa ligi"},
            null,
            null,
            "Dodaj nowa lige",
            25,
            0,
            (textFields, spinners, autoComboBoxes) -> {return null;}
        );

        //CARD LAYOUT CREATION
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);

        cardContainer.add(layoutsNames[0], newPlayerPanel);
        cardContainer.add(layoutsNames[1], newMatchPanel);
        cardContainer.add(layoutsNames[2], newTeamPanel);
        cardContainer.add(layoutsNames[3], newLeaguePanel);

        //COMBOBOX CREATION
        actionsBox = new JComboBox(actions);

        //ELEMENTS ADDITION
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(actionsBox);
        add(cardContainer);

        //SETTING LISTENERS AND FINISHING:
        actionsBox.addItemListener(this);
        newTeamPanel.changeLeague(new String[][]{indexerToStrings(DataProvider.getCountries())});
    }

    @Override
    public void changeLeague(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        String[] teamsNames = indexerToStrings(dataProvider.getTeams());

        newPlayerPanel.changeLeague(new String[][]{teamsNames});
        newMatchPanel.changeLeague(new String[][]{teamsNames, teamsNames, indexerToStrings(DataProvider.getStadiums())});
    }

    private static String[] indexerToStrings(Indexer [] indexers){
        LinkedList<String> resultList= new LinkedList<String>();
        for (Indexer indexer : indexers)
            resultList.add(indexer.toString());

        return resultList.toArray(new String[resultList.size()]);
    }
    private static Indexer findIndexer(Indexer [] indexers, String name){
        for (Indexer indexer : indexers)
            if(name.equals(indexer.toString()))
                return indexer;

        return null;
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if(itemEvent.getStateChange() == ItemEvent.SELECTED){
            cardLayout.show(cardContainer, layoutsNames[actionsBox.getSelectedIndex()]);
            System.out.println("SELECTED!!!");
        }
    }
}
