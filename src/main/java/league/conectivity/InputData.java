package league.conectivity;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

class InputData {
    public static Connection inputPlayer(String name, String surname, String bdate, int height, int weight, String country_name, String team_name) {
        Connection con = BaseConector.getConnection();
        if (con == null)
            return null;
        try {
            // getting country_id by its name from database
            String get_country_id = "select country_id from countries where name like '"+ country_name + " ';";
            PreparedStatement stmt = con.prepareStatement(get_country_id);
            ResultSet rs = stmt.executeQuery(get_country_id);
            int country_id = 0;
            if(rs.next())
                country_id = rs.getInt(1);
            
            // finding the biggest player_id and increasing the value by 1
            String get_player_id = "select max(player_id) from players;";
            stmt = con.prepareStatement(get_player_id);
            rs = stmt.executeQuery(get_player_id);
            int player_id = 0;
            if(rs.next())
                player_id = rs.getInt(1);
            player_id += 1;

            // getting team_id by its name from database
            String get_team_id = "select team_id from teams where name = '" + team_name + "';";
            stmt = con.prepareStatement(get_team_id);
            rs = stmt.executeQuery(get_team_id);
            int team_id = 0;
            if(rs.next())
                team_id = rs.getInt(1);


            String query = "INSERT INTO players (player_id, name, surname, birth_date, height, weight, birth_country_id, team_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt=con.prepareStatement(query);
            stmt.setInt(1, player_id);
            stmt.setString(2, name);
            stmt.setString(3, surname);
            stmt.setDate(4, java.sql.Date.valueOf(bdate));
            stmt.setInt(5, height);
            stmt.setInt(6, weight);
            stmt.setInt(7, country_id);
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
        return null;
    }

    public static Connection inputTeam(String name, String acronym, int country_name) {
        Connection con = BaseConector.getConnection();
        if(con == null)
            return null;
        try {
            // finding the biggest team_id and increasing the value by 1
            String get_team_id = "select max(team_id) from teams;";
            PreparedStatement stmt = con.prepareStatement(get_team_id);
            ResultSet rs = stmt.executeQuery(get_team_id);
            int team_id = 0;
            if(rs.next())
                team_id = rs.getInt(1);
            team_id += 1;

            // getting country_id by its name 
            String get_country_id = "select country_id from countries where name like '"+ country_name + " ';";
            stmt = con.prepareStatement(get_country_id);
            rs = stmt.executeQuery(get_country_id);
            int country_id = 0;
            if(rs.next())
                country_id = rs.getInt(1);

            String query = "INSERT INTO teams (name, team_id, acronym, country_id) VALUES (?, ?, ?, ?)";
            stmt = con.prepareStatement(query);
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
        return null;
    }
    public static Connection inputMatch(String date, int goals1, int goals2, String team1_name, String team2_name, String stadium_name, int league_id) {
        // you need to pass the league_id from calling function
        Connection con = BaseConector.getConnection();
        if(con == null)
            return null;
        try {
            // getting ids of the two teams from database
            String get_team1_id = "select team_id from teams where name = '" + team1_name + "';";
            PreparedStatement stmt = con.prepareStatement(get_team1_id);
            ResultSet rs = stmt.executeQuery(get_team1_id);
            int team1_id = 0;
            if(rs.next())
                team1_id = rs.getInt(1);

            String get_team2_id = "select team_id from teams where name = '" + team2_name + "';";
            stmt = con.prepareStatement(get_team2_id);
            rs = stmt.executeQuery(get_team2_id);
            int team2_id = 0;
            if(rs.next())
                team2_id = rs.getInt(1);

            // getting stadium_id by its name
            String get_stadium_id = "select stadium_id from stadiums where name = '" + stadium_name + "';";
            stmt = con.prepareStatement(get_stadium_id);
            rs = stmt.executeQuery(get_stadium_id);
            int stadium_id = 0;
            if(rs.next())
                stadium_id = rs.getInt(1);

            // finding the biggest match_id in the database and increasing by 1
            String get_match_id = "select max(match_id) from matches;";
            stmt = con.prepareStatement(get_match_id);
            rs = stmt.executeQuery(get_match_id);
            int match_id = 0;
            if(rs.next())
                match_id = rs.getInt(1);
            match_id += 1;

            String queryToMatches = "INSERT INTO matches (match_date, match_id, stadium_id, league_id) VALUES (?, ?, ?, ?)";
            stmt = con.prepareStatement(queryToMatches);
            stmt.setDate(1, java.sql.Date.valueOf(date));
            stmt.setInt(2, match_id);
            stmt.setInt(3, stadium_id);
            stmt.setInt(4, league_id);
            String queryTo_team_plays_in_match1 = "INSERT INTO team_plays_in_match (team_id, match_id, goal_amount) VALUES (?, ?, ?)";
            PreparedStatement stmt1 = con.prepareStatement(queryTo_team_plays_in_match1);
            stmt1.setInt(1, team1_id);
            stmt1.setInt(2, match_id);
            stmt1.setInt(3, goals1);
            String queryTo_team_plays_in_match2 = "INSERT INTO team_plays_in_match (team_id, match_id, goal_amount) VALUES (?, ?, ?)";
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
        return null;
    }


}