package league.connectivity;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

class InputData {
    public inputPlayer(int playerid, String name, String surname, String bdate, int height, int weight, int country_id, int team_id) {
        Connection con = BaseConector.getConnection();
        if (con == null)
            return null;
        try {
            String query = "INSERT INTO players (player_id, name, surname, birth_date, height, weight, birth_country_id, team_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt=con.prepareStatement(query);
            stmt.setInt(1, playerid);
            stmt.setString(2, name);
            stmt.setString(3, surname);
            stmt.setDate(4, java.sql.Date.valueOf(bdate));
            stmt.setInt(5, height);
            stmt.setInt(6, weight);
            stmt.setInt(7, country_id;
            stmt.setInt(8, team_id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            // Prints what exception has been thrown
            System.out.println(e);
        } finally {
            try { con.close(); } 
            catch (Exception e) { }
        }
    }

    public inputTeam(String name, int team_id, String acronym, int country_id) {
        Connection con = BaseConector.getConnection();
        if(con == null)
            return null;
        try {
            String query = "INSERT INTO teams (name, team_id, acronym, country_id) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt=con.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, team_id);
            stmt.setString(3, acronym);
            stmt.setInt(4, country_id);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { }
        }
    }
    public inputMatch(String date, int goals1, int goals2, int team1_id, int team2_id, int stadium_id, int match_id, int league_id) {
        Connection con = BaseConector.getConnection();
        if(con == null)
            return null;
        try {
            String queryToMatches = "INSERT INTO matches (match_date, match_id, stadium_id, league_id) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(queryToMatches);
            stmt.setDate(1, java.sql.Date.valueOf(date));
            stmt.setInt(2, match_id);
            stmt.setInt(3, stadium_id);
            stmt.setInt(4, league_id);
            String queryTo_team_plays_in_match1 = "INSERT INTO team_plays_in_match (team_id, match_id, goal_amount) VALUES (?, ?, ?)";
            PreparedStatement stmt1 = con.prepareStatement(queryTo_team_plays_in_match1);
            stmt1.setInt(1, team1_id);
            stmt1.setInt(2, match_id);
            stmt1.setInt(3, goals1);
            String queryTo_team_plays_in_match2 = "INSERT INTO team_plays_in_match (team_idm match_id, goal_amount) VALUES (?, ?, ?)";
            PreparedStatement stmt2 = con.prepareStatement(queryTo_team_plays_in_match2);
            stmt2.setInt(1, team2_id);
            stmt2.setInt(2, match_id);
            stmt2.setInt(3, goals2);
            stmt.executeUpdate();
            stmt1.executeUpdate();
            stmt2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        } finally {
            try { con.close(); }
            catch (Exception e) { }
        }
    }

}