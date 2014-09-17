import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;

public class checkers_model {
	private Connection con;
	private PreparedStatement preparedStatement;
	private ResultSet rs;
	private final static int ITERATION_NUMBER = 1000;

	public checkers_model() {
		db_config dbconf = new db_config();
	}

	/*
	 * Get user data if user authentication pass.
	 */
	public void getUserData(Connection con, String username, String password) {
		db_config dbconf = null;

		try {
			
			if (this.authenticate(con, username, password) == true) {

				String sel_query = "select * from checkers_user where user_name=?";
				preparedStatement = con.prepareStatement(sel_query);
				preparedStatement.setString(1, username);
				rs = preparedStatement.executeQuery();

				if (rs.next()) {
					
					String userid = rs.getString("user_id");
					String num_of_games = rs.getString("no_of_games");
					Date reg_formated_date = (Date) rs.getDate("register_date");

					System.out.println("Userid: " + userid
							+ "\nnumber of games: " + num_of_games
							+ "\nregestration date: " + reg_formated_date);

				}

				} else {
				System.out
						.println("Sorry. No record found for the given username and password combination");
				return ;
			}
			rs.close();
			preparedStatement.close();
			con.setAutoCommit(true);
		} catch (Exception ex) {
			System.out.println("Exception occur:" + ex);
		}

		//dbconf.freePoolConnection(con);
	}

	/*
	 * Citation: Source: https://www.owasp.org/index.php/Hashing_Java
	 */
	public boolean authenticate(Connection con,String username, String password)
			throws SQLException, NoSuchAlgorithmException {
		boolean authenticated = false;

		ResultSet rs = null;
		try {
			boolean userExist = true;
			// INPUT VALIDATION
			if (username == null || password == null) {
				// TIME RESISTANT ATTACK
				// Computation time is equal to the time needed by a legitimate
				// user
				userExist = false;
				username = "";
				password = "";
			}

			String selquery = "select password, salt from checkers_user WHERE user_name = ?";
			preparedStatement = con.prepareStatement(selquery);
			preparedStatement.setString(1, username);

			rs = preparedStatement.executeQuery();
			String digest, salt;
			if (rs.next()) {
				digest = rs.getString("PASSWORD");
				salt = rs.getString("SALT");

				// DATABASE VALIDATION
				if (digest == null || salt == null) {
					throw new SQLException(
							"Database inconsistant Salt or Digested Password altered");
				}
				if (rs.next()) { // Should not append, because login is the
									// primary key
					throw new SQLException(
							"Database inconsistent two CREDENTIALS with the same LOGIN");
				}
			} else { // TIME RESISTANT ATTACK (Even if the user does not exist
						// the
				// Computation time is equal to the time needed for a legitimate
				// user
				digest = "000000000000000000000000000=";
				salt = "00000000000=";
				userExist = false;
			}

			byte[] bDigest = base64ToByte(digest);
			byte[] bSalt = base64ToByte(salt);

			// Compute the new DIGEST
			byte[] proposedDigest = getHash(ITERATION_NUMBER, password, bSalt);

			return Arrays.equals(proposedDigest, bDigest) && userExist;
		} catch (IOException ex) {
			throw new SQLException(
					"Database inconsistant Salt or Digested Password altered");
		} finally {
			rs.close();
			preparedStatement.close();
			con.setAutoCommit(true);

		}
	}

	/* Insert users data if username is not exists */

	public void insert_user_data(Connection con, String username, String password) throws SQLException {
		try {

			java.sql.Timestamp date = new java.sql.Timestamp(
					new java.util.Date().getTime());

			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			// Salt generation 64 bits long
			byte[] bSalt = new byte[8];
			random.nextBytes(bSalt);
			// Digest computation
			byte[] bDigest = getHash(ITERATION_NUMBER, password, bSalt);
			System.out.println(bDigest);
			String sDigest = byteToBase64(bDigest);
			String sSalt = byteToBase64(bSalt);

			// Check for username; If exists then don't insert.

			String selquery = "select password, salt from checkers_user WHERE user_name = ?";
			preparedStatement = con.prepareStatement(selquery);
			preparedStatement.setString(1, username);
			rs = preparedStatement.executeQuery();
			String digest, salt;
			if (rs.next()) {
				System.out
						.println("Username is already exists. Please enter different username.");
				System.exit(0);
			} else {
				String ins_query = "insert into checkers_user (user_name, salt, password, register_date)"
						+ " values (?, ?, ?, ?)";

				preparedStatement = con.prepareStatement(ins_query);
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, sSalt);
				preparedStatement.setString(3, sDigest);
				preparedStatement.setTimestamp(4, date);

				preparedStatement.execute();
				System.out.println("Data insert successfully");
				
			}
		} catch (Exception ex) {
			System.out.println("Exception occur:" + ex);
		}
		finally {
			rs.close();
			preparedStatement.close();
			con.setAutoCommit(true);

		}
	}

	/**
	 * Citation: Source: https://www.owasp.org/index.php/Hashing_Java
	 * 
	 * From a password, a number of iterations and a salt, returns the
	 * corresponding digest
	 * 
	 * @param iterationNb
	 *            int The number of iterations of the algorithm
	 * @param password
	 *            String The password to encrypt
	 * @param salt
	 *            byte[] The salt
	 * @return byte[] The digested password
	 * @throws NoSuchAlgorithmException
	 *             If the algorithm doesn't exist
	 * @throws UnsupportedEncodingException
	 */
	public byte[] getHash(int iterationNb, String password, byte[] salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		//MessageDigest digest = MessageDigest.getInstance("SHA-1");
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.reset();
		digest.update(salt);
		byte[] input;
		input = digest.digest(password.getBytes("UTF-8"));
		for (int i = 0; i < iterationNb; i++) {
			digest.reset();
			input = digest.digest(input);
		}

		return input;
	}

	public static byte[] base64ToByte(String data) throws IOException {

		byte[] decoded = Base64.decodeBase64(data);
		return decoded;
	}

	public static String byteToBase64(byte[] data) {
		byte[] base64 = Base64.encodeBase64(data);
		return new String(base64);
	}

	public void insert_game_data(Connection con, Integer uid1, Integer uid2) throws SQLException {
		try {

			java.sql.Timestamp date = new java.sql.Timestamp(
					new java.util.Date().getTime());

			String ins_game_query = "insert into game_user_info (first_user_id, sec_user_id, date_created)"
						+ " values (?, ?, ?)";

				preparedStatement = con.prepareStatement(ins_game_query);
				preparedStatement.setInt(1, uid1);
				preparedStatement.setInt(2, uid2);
				//preparedStatement.setInt(3, sDigest);
				preparedStatement.setTimestamp(3, date);

				preparedStatement.execute();
				System.out.println("Game data insert successfully");
				
			
		} catch (Exception ex) {
			System.out.println("Exception occur:" + ex);
		}
		finally {
			
			preparedStatement.close();
			con.setAutoCommit(true);

		}
	}

}
