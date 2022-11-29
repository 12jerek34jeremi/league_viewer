package league.types;

import java.util.Date;

public class FullMatch extends MatchBase {
    String firstTeamName, secondTeamName, location; //location to miejsce rozrywki
    SimplePlayer[] firstTeamPlayers;
    SimplePlayer[] secondTeamPlayers;
    SimplePlayer[] firstTeamSubstitutes;
    SimplePlayer[] secondTeamSubstitutes;

    public FullMatch(String firstTeamName, String secondTeamName, String location, SimplePlayer[] firstTeamPlayers, SimplePlayer[] secondTeamPlayers, SimplePlayer[] firstTeamSubstitutes, SimplePlayer[] secondTeamSubstitutes, int firstTeamId, int secondTeamId, int match_id, Date date, String score) {
        super(firstTeamId, secondTeamId, match_id, date, score);
        this.firstTeamName = firstTeamName;
        this.secondTeamName = secondTeamName;
        this.location = location;
        this.firstTeamPlayers = firstTeamPlayers;
        this.secondTeamPlayers = secondTeamPlayers;
        this.firstTeamSubstitutes = firstTeamSubstitutes;
        this.secondTeamSubstitutes = secondTeamSubstitutes;
    }
}
