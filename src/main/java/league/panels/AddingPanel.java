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
        createInputsPanels();

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
        actionsBox.setSelectedIndex(3);
    }

    @Override
    public void changeLeague(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
        String[] teamsNames = indexerToStrings(dataProvider.getTeams());

        newPlayerPanel.changeLeague(new String[][]{teamsNames});
        newMatchPanel.changeLeague(new String[][]{teamsNames, teamsNames, indexerToStrings(DataProvider.getStadiums())});
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if(itemEvent.getStateChange() == ItemEvent.SELECTED){
            int index = actionsBox.getSelectedIndex();
            if( (index == 0 || index == 1) && dataProvider == null)
                JOptionPane.showMessageDialog(this,
                        "Aby wybrać tą kartę należy najpierw wybrać ligę z górnego menu.",
                        "Niewybrana liga", JOptionPane.WARNING_MESSAGE);
            else
                cardLayout.show(cardContainer, layoutsNames[index]);
        }
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

    private void createInputsPanels(){
        newPlayerPanel = new InputsPanel(
                new String[]{"Podaj imię", "Podaj nazwisko"},
                new String[]{"Podaj wiek", "Podaj wzrost", "Podaj wage"},
                new String[]{"Wybierz drużynę"},
                "Wprowadz zawodnika do systemu",
                25,
                15,
                ((textFieldsValues, spinnersValues, autoComboBoxesValues) ->{
                    if(dataProvider == null) return "Wybierz lige aby wybrać zawodnika.";

                    String name = textFieldsValues[0], lastName = textFieldsValues[1];
                    int age = spinnersValues[0],  height = spinnersValues[1], weight = spinnersValues[2];
                    String selectedTeam = autoComboBoxesValues[0];


                    if(!StringUtils.isAlphaSpace(name)) return "Nieprawidłowe imię!";
                    if(!StringUtils.isAlphaSpace(lastName)) return "Nieprawidłowe nazwisko!";

                    if(age < 3 || age > 85) return "Wiek zawodnika musi należeć do <3, 85>";
                    if(height < 100 || height > 250) return "Wzrost zawodnika musi należeć do <100, 250>";
                    if(weight < 30 || weight > 225) return "Waga zawodnika musi należeć do <30, 225>";


                    Indexer team = findIndexer(dataProvider.getTeams(), selectedTeam.toString());
                    if(team == null) return "Wybierz istniejącą drużynę!";

                    System.out.println("Do systemu zostanie wprowadzony zawodnik: \n" +
                            "imie: " + name + ", nazwisko: " + lastName + "\n" +
                            "wiek: " + age + ", wzrost " + height + ", waga" + weight + "\n"
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
                (textFieldsValues, spinnersValues, autoComboBoxesValues) -> {
                    if(dataProvider == null) return "Wybierz ligę aby wprowadzić nowy mecz do ligi.";

                    String dateS = textFieldsValues[0];
                    int firstTeamGoals = spinnersValues[0], secondTeamGoals = spinnersValues[1];
                    String firstTeamS = autoComboBoxesValues[0], secondTeamS = autoComboBoxesValues[1],
                            stadionS = autoComboBoxesValues[2];

                    try{
                        LocalDate.parse(dateS, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }catch (Exception e){
                        return "Nie rozumiem tej daty!";
                    }

                    if(firstTeamGoals<0 || firstTeamGoals>25) return "Aż tyle bramek strzeliła drużyna pierwsza?";
                    if(secondTeamGoals<0 || secondTeamGoals>25) return "Aż tyle bramek strzeliła drużyna druga?";

                    Indexer firstTeam = findIndexer(dataProvider.getTeams(), firstTeamS);
                    if(firstTeam == null) return "Wybierz istniejącą pierwszą drużynę";
                    Indexer secondTeam = findIndexer(dataProvider.getTeams(), secondTeamS);
                    if(secondTeam == null) return "Wybierz istniejącą drugą drużynę";
                    if(firstTeam.toIndex() == secondTeam.toIndex()) return "Drużyna gra sama z sobą?";

                    Indexer stadion = findIndexer(DataProvider.getStadiums(), stadionS);
                    if(stadion == null) return "Wybierz istniejący stadion.";

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
                (textFieldsValues, spinnersValues, autoComboBoxesValues) -> {
                    String teamName = textFieldsValues[0];
                    String countryS = autoComboBoxesValues[0];

                    Indexer country = findIndexer(DataProvider.getCountries(), countryS);
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
                (textFieldsValues, spinnersValues, autoComboBoxesValues) -> {return null;}
        );
    }
}
