import java.sql.*;

public class db_config {
		
	private dbconnection_pool dbConnectionPool;

	public db_config(){
		String dburl = "jdbc:mysql://localhost/checkers";
		String driver = "com.mysql.jdbc.Driver";
		String sUser = "root";
		String sPwd = "";
	
		dbconnection_pool dbConnectionPool=null;
		try {
		
			dbConnectionPool = new dbconnection_pool(driver, dburl, sUser, sPwd);

		} catch (Exception e) {
			e.printStackTrace();

		}
		this.dbConnectionPool=dbConnectionPool;
	}
	public Connection accessconn(){
		
		Connection c1, c2,c3,c4,c5,c6;

		c1 = dbConnectionPool.getConnection();
		return c1;
		}
		
		/*c2 = dbConnectionPool.getConnection();
		c3 = dbConnectionPool.getConnection();
		c4 = dbConnectionPool.getConnection();*/
		
		//dbConnectionPool.freeConnection(c5);

		//c6 = dbConnectionPool.getConnection();
	public void freePoolConnection(Connection con){
		
		dbConnectionPool.freeConnection(con);
		dbConnectionPool.destroy();
	}
	
}

			