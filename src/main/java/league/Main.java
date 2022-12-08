package league;

import java.util.LinkedList;
import java.util.Scanner;

import league.types.*;
import league.conectivity.DataProvider;

class Main{

    public static void main(String args[]){
        League[] leagues = DataProvider.getLeagues();
        String message = """
                Type 'league  {id}' to view league of given id.
                Type 'refresh' to refresh league list and view it.
                Type 'quit' to exit program.
                    """;
        Scanner scanner = new Scanner(System.in);

        PrettyPrint.printLeagueArray(leagues);
        System.out.println(message);

        while (true){
            System.out.print("<Main menu> Enter command >>>");
            String userInput = scanner.nextLine();
            String[] commands = userInput.split(" ");

            if(commands[0].equals("league")){
                DataProvider dataProvider = DataProvider.getDataProvider(Integer.parseInt(commands[1]));
                if(dataProvider == null){
                    System.out.println("Unable to fetch data from server, try again later!");
                }else{
                    startLeagueViewer(dataProvider);
                }
            }else if(commands[0].equals("refresh")){
                leagues = DataProvider.getLeagues();
                PrettyPrint.printLeagueArray(leagues);
            }else if (commands[0].equals("quit")){
                return;
            }else{
                System.out.println("You did not type correctly one of the methods. Try again");
            }
        }
    }

    public static void startLeagueViewer(DataProvider dataProvider){
        Scanner scanner = new Scanner(System.in);
        String message =    "Choose one from the following options (type by name)\n" +
                            "getPlayer -- returns player by ID given (type: getPlayer {id})\n" +
                            "getPlayers -- returns all of the players (type: getPlayers)\n"+
                            "getMatches -- returns all of the matches (type: getMatches)\n" +
                            "getMatch -- returns match by ID given (type: getMatch {match_id}\n" +
                            "getTeams -- returns all of the teams (type: getTeams)\n" +
                            "getTeam -- returns team by ID given (type: getTeam {team_id}\n" +
                            "refresh {players, matches, teams} -- refreshes some data" +
                            "quit -- go back to main menu (choosing league)";
        System.out.println(message);
        while(true) {
            System.out.print("<League Viewer> Enter command >>>");
            String userInput = scanner.nextLine();
            String[] commands = userInput.split(" ");
            String command = commands[0];

            if(command.equals("getPlayer")){
                PrettyPrint.printFullPlayer(dataProvider.getPlayer(Integer.parseInt(commands[1])));
            }else if (command.equals("getPlayers")){
                PrettyPrint.printSimplePlayerArray(dataProvider.getPlayers());
            }else if (command.equals("getMatches")){
                PrettyPrint.printSimpleMatchArray(dataProvider.getMatches());
            }else if (command.equals("getTeams")){
                PrettyPrint.printSimpleTeamsArray(dataProvider.getTeams());
            }else if (command.equals("getTeam")){
                PrettyPrint.printFullTeam(dataProvider.getTeam(Integer.parseInt(commands[1])));
            }else if (command.equals("quit")){
                return;
            } else if (command.equals("refresh")) {
                boolean success;
                if(commands[1].equals("players")){
                    success = dataProvider.refreshPlayers();
                }else if(commands[1].equals("matches")){
                    success = dataProvider.refreshMatches();
                }else if(commands[1].equals("teams")){
                    success = dataProvider.refreshTeams();
                }else{
                    System.out.println("What is '" + commands[1] + "' ?"); return;
                }

                if(success){
                    System.out.println(commands[1] + " updated successfully!");
                }else{
                    System.out.println("An error occurred while trying to refresh " + commands[1]);
                }

            } else{
                System.out.println("You did not type correctly one of the methods. Try again");
            }

        }

    }
}