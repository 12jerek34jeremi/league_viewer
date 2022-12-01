package league.types;

public class FullTeam extends TeamBase{

    public String origins; //kraj z Której Pochodza, dla Jagelonii będzie to "Polska"
    public SimpleMatch[] matches;
    public FullTeam(int teamId, String teamName, String origins, SimpleMatch[] matches){
        super(teamId, teamName);
        this.origins = origins;
        this.matches = matches;
    }

}
