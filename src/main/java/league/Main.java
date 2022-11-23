package league;

import java.sql.*;
import oracle.jdbc.driver.OracleDriver;
import league.conectivity.BaseConector;

class Main{
    public static void main(String args[]){
        new BaseConector().simpleTest();
    }
}