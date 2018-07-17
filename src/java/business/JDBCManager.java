package business;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class JDBCManager {

	private Connection con;

	public JDBCManager(String db) throws ClassNotFoundException, SQLException {
		//Vengono settati i parametri di connessione
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver);
		String url = "jdbc:mysql://localhost:3306/"+db;
		con = DriverManager.getConnection (url, "root", "");
	}

	//Esegue la query q
	public ResultSet query(String q) throws SQLException{
		Statement cmd = con.createStatement();
		return cmd.executeQuery(q);
	}

	//Esegue la query q
	public boolean update(String q) throws SQLException{
		Statement cmd = con.createStatement();
		return cmd.execute(q);
	}
}
