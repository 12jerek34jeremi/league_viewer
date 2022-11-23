package league.types;

public class FullMatch extends MatchBase {

    String firstTeamName, secondTeamName, location;
    //location to miejsce rozrywki

   SimplePlayer[] firstTeamPlayers;
   SimplePlayer[] secondTeamPlayers;
   SimplePlayer[] firstTeamSubstitutes;
   SimplePlayer[] secondTeamSubstitutes;
}
