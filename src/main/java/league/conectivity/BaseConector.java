package league.conectivity;

import java.sql.*;
import java.util.LinkedList;

import league.types.*;

public class BaseConector {

    public LinkedList<SimplePlayer> getNextN(int n){return null;}
    public LinkedList<SimplePlayer> getNextN(int n, int player_id){return null;}
    public LinkedList<SimplePlayer> getPreviousN(int n, int player_id){return null;}

    public FullPlayer get_player(int player_id){return null;}


    public SimpleMatch[] getMatches(){return null;}
    public FullMatch getMatch(int match_id){return null;}


    public SimpleTeam[] getTeams(){return null;}
    public FullTeam getTeam(int teamId){return null;}



    public void simpleTest(){
        Connection con = getConnection();
        try{
            PreparedStatement stmt=con.prepareStatement("SELECT name, surname FROM Employees WHERE name = ?");
            stmt.setString(1, "Luis");
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
                System.out.println("Imie i nazwisko: " + rs.getString(1)+"  "+rs.getString(2));
            con.close();
        }catch(Exception e){
            System.out.println("This is exception:");
            System.out.println(e);
        }
    }


    private Connection getConnection(){
        /* METODA NIE TESTOWANA BEZPOSREDNIO!!! */
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con= DriverManager.getConnection("jdbc:oracle:thin:@ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl","jchmiel","jchmiel");
        } catch (Exception e) {
            System.out.println("The exception happened while trying to connect to database. This is exception: ");
            System.out.println(e);
        }
        return con;
    }
}
