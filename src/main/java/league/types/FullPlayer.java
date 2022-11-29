package league.types;

public class FullPlayer extends  PlayerBase{
    public int age;
    public String origin;
    public float height, weight;
    SimpleMatch[] matches;

    public FullPlayer(int age, String origin, float height, float weight, SimpleMatch[] matches, int playerId, int teamId, String firstName, String lastName, String teamName) {
        super(playerId, teamId, firstName, lastName, teamName);
        this.age = age;
        this.origin = origin;
        this.height = height;
        this.weight = weight;
        this.matches = matches;
    }
}