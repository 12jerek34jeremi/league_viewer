package league.tests;

import league.conectivity.DataProvider;
import league.types.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import league.conectivity.InputData;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.SortedMap;

public class InputTest {
    static private Random generator;

    @Test
    public void testInput(){
        generator = new Random();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String sufix = "T" + generator.nextInt(1000);
        String newLeagueName = "LigaTestowa"+sufix;


        System.out.println("START: Uploading new league");
        if(!InputData.inputLeague(newLeagueName)) no_connection();
        System.out.println("FINISH: Uploading new league");


        System.out.println("START: Downloading leagues");
        League[] leagues = DataProvider.getLeagues();
        if(leagues == null) no_connection();
//        if(leagues.length == 0)
        System.out.println("FINISH: Downloading leagues");


        League newLeague = null;
        for(League league : leagues){
            if(league.leagueName.equals(newLeagueName)){
                newLeague = league;
                break;
            }
        }
        if(newLeague == null)
            print_and_exit("After uploading new leage '" + newLeagueName +
                    "', there is no league of this name in database.");


        System.out.println("START: downloading stadiums and country data");
        if(!DataProvider.prepareData()) no_connection();
        System.out.println("END: downloading stadiums and country data");


        String fristTeamCountry = rand_element(DataProvider.getCountries()).countryName,
            secondTeamContry = rand_element(DataProvider.getCountries()).countryName,
            matchDate = formatter.format(new Date()),
            playerBirthday = "" + (generator.nextInt(50) + 1960) + "-0" + generator.nextInt(9) +
                    "-" + generator.nextInt(2) + generator.nextInt(9),
            stadiumName = rand_element(DataProvider.getStadiums()).stadiumName;

        int firstTeamGoals = generator.nextInt(10), secondTeamGoals = generator.nextInt(10),
            playerHeight = generator.nextInt(100) + 100,
            playerWeight = generator.nextInt(180) + 30;

        Calendar now = Calendar.getInstance(), birthDate = Calendar.getInstance();
        try {
            birthDate.setTime(formatter.parse(playerBirthday));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        int playerAge = now.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (birthDate.get(Calendar.DAY_OF_YEAR) > now.get(Calendar.DAY_OF_YEAR)) {
            playerAge--;
        }


        System.out.println("START: uploadingFirstTeam");
        InputData.inputTeam(newLeague.leagueId, "FirstTeam", "FTA", fristTeamCountry);
        System.out.println("END: uploadingFirstTeam");


        System.out.println("START: uploadingSecondTeam");
        InputData.inputTeam(newLeague.leagueId, "SecondTeam", "STA", secondTeamContry);
        System.out.println("END: uploadingSecondTeam");


        System.out.println("START: uploadingMatch");
        InputData.inputMatch(firstTeamGoals, secondTeamGoals, newLeague.leagueId, matchDate, "FirstTeam",
                "SecondTeam", stadiumName);
        System.out.println("END: uploadingMatch");


        System.out.println("START: uploadingPlayer");
        InputData.inputPlayer(playerHeight, playerWeight, "Jan", "Kowalski", playerBirthday,
            fristTeamCountry, "FirstTeam");
        System.out.println("END: uploadingPlayer");


        System.out.println("START: Downloading general info about newly created league");
        DataProvider dataProvider = DataProvider.getDataProvider(newLeague.leagueId);
        System.out.println("END: Downloading general info about newly created league");


        SimpleTeam firstTeam = findIndexer(dataProvider.getTeams(), "FirstTeam");
        if(firstTeam == null) print_and_exit("There is no team of name 'FirstTeam' in downloaded SimpleTeam array");
        SimpleTeam secondTeam = findIndexer(dataProvider.getTeams(), "SecondTeam");
        if(firstTeam == null) print_and_exit("There is no team of name 'SecondTeam' in downloaded SimpleTeam array");


        System.out.println("START: Downloading full teams");
        FullTeam firstTeamFull = dataProvider.getTeam(firstTeam.teamID);
        FullTeam secondTeamFull = dataProvider.getTeam(secondTeam.teamID);
        if(firstTeamFull == null || secondTeamFull == null) no_connection();
        System.out.println("END: Downloading full teams");


        System.out.println("START: Checking teams");
        assertEquals(firstTeamFull.teamName, "FirstTeam");
        assertEquals(firstTeamFull.origins, fristTeamCountry);
        assertEquals(firstTeamFull.matches.length, 1);
        assertEquals(secondTeamFull.teamName, "SecondTeam");
        assertEquals(secondTeamFull.origins, secondTeamContry);
        assertEquals(secondTeamFull.matches.length, 1);
        System.out.println("END: Checking teams");



        System.out.println("START: checking matches");
        Match[] matches = dataProvider.getMatches();
        assertEquals(matches.length, 1);
        Match match = matches[0];
        assertEquals(match.score, ""+firstTeamGoals+":"+secondTeamGoals);
        assertEquals(formatter.format(match.date), matchDate);
        assertEquals(match.firstTeamName, "FirstTeam");
        assertEquals(match.secondTeamName, "SecondTeam");
        assertEquals(match.location, stadiumName);
        assertEquals(match.firstTeamId, firstTeam.teamID);
        assertEquals(match.secondTeamId, secondTeam.teamID);
        System.out.println("END: checking matches");


        System.out.println("START: checking simple players");
        SimplePlayer[] players = dataProvider.getPlayers();
        assertEquals(matches.length, 1);
        SimplePlayer player = players[0];
        assertEquals(player.firstName, "Jan");
        assertEquals(player.lastName, "Kowalski");
        assertEquals(player.teamId, firstTeam.teamID);
        assertEquals(player.teamName, "FirstTeam");
        System.out.println("END: checking simple players");


        System.out.println("START: Downloading full player");
        FullPlayer fullPlayer = dataProvider.getPlayer(player.playerId);
        if(fullPlayer == null) no_connection();
        System.out.println("END: Downloading full player");


        System.out.println("START: checking full player");
        assertEquals(fullPlayer.height, playerHeight);
        assertEquals(fullPlayer.weight, playerHeight);
        assertEquals(fullPlayer.firstName, "Jan");
        assertEquals(fullPlayer.lastName, "Kowalski");
        assertEquals(fullPlayer.age, playerAge);
        assertEquals(fullPlayer.origin, fristTeamCountry);
        assertEquals(fullPlayer.teamName, "FirstTeam");
        assertEquals(fullPlayer.teamName, "SecondTeam");
        System.out.println("END: checking full player");

        System.out.println("\n\nLeagues");
        PrettyPrint.printLeagueArray(leagues);
        System.out.println("\n\nMatches");
        PrettyPrint.printSimpleMatchArray(matches);
        System.out.println("\n\nTeams");
        PrettyPrint.printSimpleTeamsArray(dataProvider.getTeams());
        System.out.println("\n\nFullTeam1");
        PrettyPrint.printFullTeam(firstTeamFull);
        System.out.println("\n\nFullTeam2");
        PrettyPrint.printFullTeam(secondTeamFull);
        System.out.println("\n\nSimplePlayers");
        System.out.println(players);
        System.out.println("\n\nFullPlayer");
        PrettyPrint.printFullPlayer(fullPlayer);

    }

    static void print_and_exit(String msg){
        System.out.println(msg);
        System.out.println("Test not passed");
        fail();
        System.exit(0);
    }

    static void no_connection(){
        System.out.println("Cant execute test because no internet connection.\n" +
                "Please check connection and run test later.");
        System.exit(0);
    }

    static <T> T rand_element(T[] elements){
        return elements[generator.nextInt(elements.length)];
    }

    private static <T extends Indexer>  T findIndexer(T [] indexers, String name){
        for (T indexer : indexers)
            if(name.equals(indexer.toString()))
                return indexer;

        return null;
    }


}

/*

        League[] leagues = DataProvider.getLeagues();
        if(leagues == null) print_and_exit();

        if(leagues.length == 0){
            System.out.println("There are no leagues in database. Can't run test");
            System.exit(0);
        }

        DataProvider dataProvider = DataProvider.getDataProvider(leagues[0].leagueId);
 */