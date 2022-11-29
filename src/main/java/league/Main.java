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
        int playerId, number;
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
            String userInput = scanner.next();
            String[] commands = userInput.split(" ");

            switch(commands[0]) {
                case "getPlayer":
                    playerId = Integer.parseInt(commands[1]);
                    FullPlayer player = connector.get_player(playerId);
                    //trzeba tu dodać komendę printPlayer, gdy będzie napisana
                case "getNextN":
                    playerId = 1; //zakładam, że minimalne player_id to 1
                    number = Integer.parseInt(commands[1]);
                    if (commands.length > 2) {
                        playerId = Integer.parseInt(commands[2]);
                    }
                    LinkedList<SimplePlayer> players = connector.getNextN(number, playerId);
                    //do zastanowienia się, czy nie zadeklarować przed switchem
                    for(var a : players) {
                        System.out.println(a);
                    }
                case "getPreviousN":
                    playerId = Integer.parseInt(commands[1]);
                    number = Integer.parseInt(commands[2]);
                    players = connector.getPreviousN(number, playerId);
                    for(var a : players) {
                        System.out.println(a);
                    }
                case "getMatches":
                    SimpleMatch[] matches = connector.getMatches();
                    for(var a : matches) {
                        System.out.println(a);
                    }
                case "getMatch":
                    int matchId = Integer.parseInt(commands[1]);
                    FullMatch match = connector.getMatch(matchId);
                    //printMatch do dodania, kiedy napiszemy
                case "getTeams":
                    SimpleTeam[] teams = connector.getTeams();
                    for(var a : teams) {
                        System.out.println(a);
                    }
                case "getTeam":
                    int teamId = Integer.parseInt(commands[1]);
                    FullTeam team = connector.getTeam(teamId);
                    //printTeam do dodania, kiey napiszemy
                case "quit":
                    return;
                default:
                    System.out.println("You did not type correctly one of the methods. Try again");
            }
        }
    }
}