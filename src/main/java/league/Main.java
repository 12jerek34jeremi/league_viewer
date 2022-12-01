package league;

import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;

import league.types.*;
import oracle.jdbc.driver.OracleDriver;
import league.conectivity.BaseConector;

class Main{
    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        BaseConector connector = new BaseConector();
        String message =    "Choose one from the following options (type by name)\n" +
                            "getPlayer -- returns player by ID given (type: getPlayer {id})\n" +
                            "getNextN -- returns N number of players (type: getNextN {N} {player_id} - if you want not to start from the beginning\n" +
                            "getPreviousN -- returns N number of players before given ID (type: getPreviousN {N} {player_id}\n" +
                            "getMatches -- returns all of the matches (type: getMatches)\n" +
                            "getMatch -- returns match by ID given (type: getMatch {match_id}\n" +
                            "getTeams -- returns all of the teams (type: getTeams)\n" +
                            "getTeam -- returns team by ID given (type: getTeam {team_id}\n" +
                            "quit -- exit program";
        System.out.println(message);
        
        while(true) {
            System.out.print("Enter command >>>");
            String userInput = scanner.nextLine();
            String[] commands = userInput.split(" ");
            String command = commands[0];

//
//            System.out.println("Commands length is :" + commands.length);
//            for (int i = 0; i < commands.length; i++) {
//                System.out.println("Command " + i + ": '" + commands[i] + "'");
//            }
//            System.out.println("Your command is : '" + command+"'");

            if(command.equals("getPlayer")){
                int playerId = Integer.parseInt(commands[1]);
                FullPlayer player = connector.getPlayer(playerId);
                PrettyPrint.printFullPlayer(player);
            }else if (command.equals("getNextN")){
                int number = Integer.parseInt(commands[1]);
                LinkedList<SimplePlayer> players;
                if (commands.length > 2) {
                    int playerId = Integer.parseInt(commands[2]);
                    players = connector.getNextN(number, playerId);
                }else{
                    players = connector.getNextN(number);
                }
               PrettyPrint.printSimplePlayerList(players);
            }else if (command.equals("getPreviousN")){
                int number = Integer.parseInt(commands[1]);
                int playerId = Integer.parseInt(commands[2]);
                LinkedList<SimplePlayer> players = connector.getPreviousN(number, playerId);
                PrettyPrint.printSimplePlayerList(players);
            }else if (command.equals("getMatches")){
                SimpleMatch[] matches = connector.getMatches();
                PrettyPrint.printSimpleMatchArray(matches);
            }else if (command.equals("getTeams")){
                SimpleTeam[] teams = connector.getTeams();
                PrettyPrint.printSimpleTeamsArray(teams);
            }else if (command.equals("getTeam")){
                int teamId = Integer.parseInt(commands[1]);
                FullTeam team = connector.getTeam(teamId);
                PrettyPrint.printFullTeam(team);
            }else if (command.equals("quit")){
                return;
            }else{
                System.out.println("You did not type correctly one of the methods. Try again");
            }

        }
    }
}