package com.wipro.bank.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    public static Connection getDBConnection() {
    	try {
    	Class.forName("oracle.jdbc.driver.OracleDriver");
    	String url="jdbc:oracle:thin:@localhost:1521:xe";
    	String user="System";
    	String pass="guru1";
    	Connection connection=DriverManager.getConnection(url,user,pass);
    	return connection;
    	}
    	catch(SQLException e) {
    		System.out.print(e);
    		return null;
    	}
    	catch(ClassNotFoundException e) {
    		System.out.print(e);
    		return null;
    	}
    }
}