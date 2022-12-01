package league.conectivity;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import league.types.*;

import league.PrettyPrint;

public class BaseConector {

    public BaseConector(){}

    public LinkedList<SimplePlayer> getNextN(int n){
        /* This method returns first n players from sorted (ascending) by last name and first name list of all players.
        *  Players in returned LinkedList are sorted by last name and first name ascending.
        *
        * In case if n is bigger than number of players in database, all players are returned.
        * In case of failure (for example internet connection failure) this method returns null.
        *
        * @param n   number of players to return (size of returned LinkedList)
         */
        return getResultList("""
            SELECT P.player_id AS playerId, P.team_id AS teamID,
                P.name AS firstName, P.surname AS lastName, T.name AS teamName
            FROM players P LEFT JOIN teams T ON P.team_id = T.team_id
            ORDER BY P.surname, P.name
            FETCH FIRST ? ROWS ONLY
                """,

            ((resultSet, resultList) -> {
                resultList.add(new SimplePlayer(
                        resultSet.getInt("playerId"),
                        resultSet.getInt("teamID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("teamName")
                ));
            })

            ,n);
    }

    public LinkedList<SimplePlayer> getNextN(int n, int playerId){
        /* This method returns first n players, which are after player of id playerID, in sorted by last name and
            first name list of all players.
         * Players in returned LinkedList are sorted by last name and first name ascending.
         *
         * If after player of id=playerId (arg) are m players and m < n (like if his lastname starts with 'Z'), then list
            of all players after the player of id=playerId is returned (returned LinkedList size is m).
         * If player of id=playerId (arg) is the last player or there is no player of id=playerID, then empty list is returned.
         *
         * In case of failure (for example internet connection failure) this method returns null.
         *
         * @param n   number of players to return (size of returned LinkedList)
         */
        return getResultList("""
            WITH previous_last(names) AS 
                (SELECT surname||name AS names FROM players where player_id = ?)
            
            SELECT P.player_id AS playerId, P.team_id AS teamID, P.name AS firstName, P.surname AS lastName, T.name AS teamName
            FROM players P LEFT JOIN teams T ON P.team_id = T.team_id
            WHERE P.surname||P.name > (SELECT names FROM previous_last)
            ORDER BY P.surname, P.name
            FETCH FIRST ? ROWS ONLY
                    """,

            ((resultSet, resultList) -> {
                resultList.add(new SimplePlayer(
                        resultSet.getInt("playerId"),
                        resultSet.getInt("teamID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("teamName")
                ));
            }),

            playerId,
            n
        );
    }
    public LinkedList<SimplePlayer> getPreviousN(int n, int playerId){
        /* This method returns last n players, which are before player of id playerID, from sorted by last name and
            first name list of all players.
         * Players in returned LinkedList are sorted by last name and first name ascending.
         *
         * If before player of id=playerId (arg) are m players and m < n (like if his lastname starts with 'A'), then list
            of all players after the player is returned (returned LinkedList size is m)
         * If player of id playerId (arg) is the first player or there is no player of id=playerID, then empty list is returned.
         *
         * In case of failure (for example internet connection failure) this method returns null.
         *
         * @param n   number of players to return (size of returned LinkedList)
         */
        return getResultList("""
            WITH previous_last(names) AS
                (SELECT surname||name AS names FROM players where player_id = ?)

            SELECT P.player_id AS playerId, P.team_id AS teamID, P.name AS firstName, P.surname AS lastName, T.name AS teamName
            FROM players P LEFT JOIN teams T ON P.team_id = T.team_id
            WHERE P.surname||P.name < (SELECT names FROM previous_last)
            ORDER BY P.surname DESC, P.name DESC
            FETCH FIRST ? ROWS ONLY
                    """,

            ((resultSet, resultList) -> {
                resultList.addFirst(new SimplePlayer(
                        resultSet.getInt("playerId"),
                        resultSet.getInt("teamID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("teamName")
                ));
            }),

            playerId,
            n
            );
    }

    public FullPlayer getPlayer(int playerId){
        /*
        Returns object representing a player of id specified by argument playerId.

        If there is no player of specified id, then null is returned;
        If an error occurred when trying to get data from database (for example internet connection failure),
         then null is returned.
         */
        LinkedList<FullPlayer> oneElementList =  getResultList( """
            SELECT P.player_id AS playerId, P.team_id AS teamID, P.birth_date as birthDate, P.height AS height, P.weight AS weight,
                   P.name AS firstName, P.surname AS lastName, T.name AS teamName, C.name AS origin
            FROM players P LEFT JOIN teams T
                    ON P.team_id = T.team_id
                LEFT JOIN countries C
                    ON P.birth_country_id = C.country_ID
            WHERE P.player_id = ?
                    """,

                ((resultSet, resultList) -> {
                    Calendar now = Calendar.getInstance();
                    Calendar birthDate = Calendar.getInstance();
                    birthDate.setTime(resultSet.getDate("birthDate"));
                    int age = now.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
                    if (birthDate.get(Calendar.DAY_OF_YEAR) > now.get(Calendar.DAY_OF_YEAR)) {
                        age--;
                    }

                    resultList.add(new FullPlayer(
                            resultSet.getInt("playerID"),
                            resultSet.getInt("teamID"),
                            age,
                            resultSet.getInt("weight"),
                            resultSet.getInt("height"),
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName"),
                            resultSet.getString("teamName"),
                            resultSet.getString("origin")
                    ));
                }),

                playerId
                );

        if(oneElementList == null || oneElementList.size() != 1){
            return null;
        }

        return oneElementList.getFirst();
    }

    public SimpleMatch[] getMatches(){
        /*
        Returns array of all matches in DataBase.

        If an error occurred when trying to get data from database (for example internet connection failure),
         then null is returned.
         */
        LinkedList<SimpleMatch> matchList = getResultList("""
            SELECT  TM1.team_id AS firstTeamId, TM2.team_id AS secondTeamId, M.match_id as matchId,
                    T1.name AS firstTeamName, T2.name AS secondTeamName, 
                    TM1.goal_amount AS firstTeamGoals, TM2.goal_amount AS secondTeamGoals, S.name AS location,
                    M.match_date AS matchDate
                    
            FROM team_plays_in_match TM1 INNER JOIN team_plays_in_match TM2
                    ON TM1.match_id = TM2.match_id AND TM1.team_id > TM2.team_id
                INNER JOIN teams T1
                    ON TM1.team_id = T1.team_id
                INNER JOIN teams T2
                    ON TM2.team_id = T2.team_id
                INNER JOIN matches M
                    ON TM1.match_id = M.match_id
                INNER JOIN stadiums S
                    ON S.stadium_id = M.stadium_id
                
            ORDER BY M.match_date DESC
                    """,

            ((resultSet, resultList) -> {
                resultList.add( new SimpleMatch(
                        resultSet.getInt("firstTeamId"),
                        resultSet.getInt("secondTeamId"),
                        resultSet.getInt("matchId"),
                        resultSet.getString("firstTeamName"),
                        resultSet.getString("secondTeamName"),
                        resultSet.getString("location"),
                        "" + resultSet.getInt("firstTeamGoals") + ":" + resultSet.getInt("secondTeamGoals"),
                        new Date(resultSet.getDate("matchDate").getTime())

                ));
            })
        );
        return matchList.toArray(new SimpleMatch[matchList.size()]);
    }

    public SimpleTeam[] getTeams(){Connection con = getConnection();
        /*
        Returns array of all teams which are in DataBase.

        If an error occurred when trying to get data from database (for example internet connection failure),
         then null is returned.
         */
        LinkedList<SimpleTeam> teamsList = getResultList("""
            SELECT team_id AS teamID, name AS teamName
            FROM TEAMS """,

            ((resultSet, resultList) -> {
                resultList.add(new SimpleTeam(
                        resultSet.getInt("teamID"),
                        resultSet.getString("teamName")

                ));
            })
            );

        return teamsList.toArray(new SimpleTeam[teamsList.size()]);
    }
    public FullTeam getTeam(int teamId){
        /*
        Returns object representing a team of id specified by argument teamId.

        If there is no team of specified id, then null is returned;
        If an error occurred when trying to get data from database (for example internet connection failure),
         then null is returned.
         */
        Connection con = getConnection();
        FullTeam resultTeam = null;
        if(con == null)
            return null;

        try{
            String qr_team = """
            SELECT T.team_id AS teamID, T.name AS teamName, C.name AS origin
            FROM TEAMS T INNER JOIN countries C
                ON T.country_id = C.country_id
            WHERE T.team_id = ?
            """,
                qr_matches = """
            SELECT  TM1.team_id AS firstTeamId, TM2.team_id AS secondTeamId, M.match_id as matchId,
                    T1.name AS firstTeamName, T2.name AS secondTeamName, 
                    TM1.goal_amount AS firstTeamGoals, TM2.goal_amount AS secondTeamGoals, S.name AS location,
                    M.match_date AS matchDate
                    
            FROM team_plays_in_match TM1 INNER JOIN team_plays_in_match TM2
                    ON TM1.match_id = TM2.match_id AND TM1.team_id > TM2.team_id
                INNER JOIN teams T1
                    ON TM1.team_id = T1.team_id
                INNER JOIN teams T2
                    ON TM2.team_id = T2.team_id
                INNER JOIN matches M
                    ON TM1.match_id = M.match_id
                INNER JOIN stadiums S
                    ON S.stadium_id = M.stadium_id
            
            WHERE T1.team_id = ? OR T2.team_id = ?    
            ORDER BY M.match_date DESC
                        """;

            PreparedStatement stmt=con.prepareStatement(qr_matches);
            stmt.setInt(1, teamId);
            stmt.setInt(2, teamId);
            ResultSet resultSet = stmt.executeQuery();

            LinkedList<SimpleMatch> matchList = new LinkedList<SimpleMatch>();
            while (resultSet.next()){
                int firstTeamId = resultSet.getInt("firstTeamId");
                if(firstTeamId == teamId) {
                    matchList.add(new SimpleMatch(
                            firstTeamId,
                            resultSet.getInt("secondTeamId"),
                            resultSet.getInt("matchId"),
                            resultSet.getString("firstTeamName"),
                            resultSet.getString("secondTeamName"),
                            resultSet.getString("location"),
                            "" + resultSet.getInt("firstTeamGoals") + ":" + resultSet.getInt("secondTeamGoals"),
                            new Date(resultSet.getDate("matchDate").getTime())
                    ));
                }else{
                    matchList.add(new SimpleMatch(
                            resultSet.getInt("secondTeamId"),
                            firstTeamId,
                            resultSet.getInt("matchId"),
                            resultSet.getString("secondTeamName"),
                            resultSet.getString("firstTeamName"),
                            resultSet.getString("location"),
                            "" + resultSet.getInt("secondTeamGoals") + ":" + resultSet.getInt("firstTeamGoals"),
                            new Date(resultSet.getDate("matchDate").getTime())
                    ));
                }
            }
            SimpleMatch[] matchArray = matchList.toArray(new SimpleMatch[matchList.size()]);

            stmt.close();
            stmt = con.prepareStatement(qr_team);
            stmt.setInt(1, teamId);
            resultSet.close();
            resultSet = stmt.executeQuery();
            resultSet.next();

            resultTeam = new FullTeam(teamId,
                    resultSet.getString("teamName"),
                    resultSet.getString("origin"),
                    matchArray);

        }catch(Exception e){
            System.out.println("The exception happened while trying get data from database. This is exception: ");
            System.out.println(e);
            resultTeam = null;
        }
        return resultTeam;
    }

    private Connection getConnection(){
        /* METODA NIE TESTOWANA BEZPOSREDNIO!!! */
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con= DriverManager.getConnection("jdbc:oracle:thin:@ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl","z99","29vwr3");
        } catch (Exception e) {
            System.out.println("The exception happened while trying to connect to database. This is exception: ");
            e.printStackTrace();
        }
        return con;
    }

    /*
    FOR TESTING PPURPOSES, UNNESESARY

    private void printResultSet(ResultSet set) throws SQLException {
        ResultSetMetaData rsmd = set.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        final int n = 25;

        for (int i = 1; i <= columnsNumber; i++) {
            System.out.print(PrettyPrint.populateToN(rsmd.getColumnName(i), n));
        }
        System.out.println("");

        while (set.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                String columnValue = set.getString(i);
                System.out.print(PrettyPrint.populateToN(columnValue, n));
            }
            System.out.println("");
        }
    }
     */

    private <ListType> LinkedList<ListType> getResultList(String qr, ListCreator<ListType>listCreator,
                                                              int arg1, int arg2){
        Connection con = getConnection();
        if(con == null)
            return null;

        LinkedList<ListType> resultList = null;

        try{
            PreparedStatement stmt=con.prepareStatement(qr);

            if(arg1 >= 0){
                stmt.setInt(1, arg1);
                if(arg2 >= 0){
                    stmt.setInt(2, arg2);
                }
            }
            ResultSet resultSet = stmt.executeQuery();

            resultList = new LinkedList<ListType>();

            while (resultSet.next()) {
               listCreator.addToList(resultSet, resultList);
            }
            resultSet.close();

        }catch(Exception e){
            System.out.println("The exception happened while trying get data from database. This is exception: ");
            e.printStackTrace();
        }finally {
            try { con.close(); } catch (Exception e) { /* Ignored */ }
        }

        return resultList;
    }
    private <ListType> LinkedList<ListType> getResultList(String qr, ListCreator<ListType>listCreator, int arg1){
        return getResultList(qr, listCreator, arg1,-1);
        }
    private <ListType> LinkedList<ListType> getResultList(String qr, ListCreator<ListType>listCreator){
        return getResultList(qr, listCreator, -1,-1);
    }
}
