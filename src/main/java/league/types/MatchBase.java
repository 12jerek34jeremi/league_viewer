package league.types;
import java.util.Date;

public abstract class MatchBase {
    public MatchBase(int firstTeamId, int secondTeamId, int match_id, Date date, String score) {
        this.firstTeamId = firstTeamId;
        this.secondTeamId = secondTeamId;
        this.match_id = match_id;
        this.date = date;
        this.score = score;
    }
    public int firstTeamId, secondTeamId, match_id;

    Date date;
    String score;
}
