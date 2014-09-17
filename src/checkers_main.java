import java.sql.*;

public class checkers_main {
	public static void main(String[] args) throws SQLException{
		db_config c1=new db_config();
		Connection conn= c1.accessconn();
		checkers_model chmodel = new checkers_model();
		
		//chmodel.insert_game_data(conn, 2,3);
		//chmodel.insert_user_data(conn, "utank@hotmail134.edu","ISA681");
		//chmodel.getUserData(conn, "tankurvi@gmail.com", "urvitank");
	   chmodel.getUserData(conn,"utank@hotmail134.edu", "ISA681");
		c1.freePoolConnection(conn);
	}

}
