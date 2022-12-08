package league.conectivity;

import java.sql.Connection;
import java.sql.DriverManager;

class BaseConector {
    static Connection getConnection(){
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
}
