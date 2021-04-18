import java.sql.*;
import java.util.Random;

public class PostgresConnection {

		private Connection c = null;
		private final String DATABASE = "USERSPROTO";
		
		public PostgresConnection() {
			try {
				Class.forName("org.postgresql.Driver");
				c = DriverManager.getConnection(
						"jdbc:postgresql://localhost:5432/usersdb",
						"postgres", "PaaFPOWE_(#");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+e.getMessage());
				System.exit(0);
			}
		}
		
		/**
		 * createDatabaseTable
		 * creates table in sql database if not already created
		 */
		public void createDatabaseTable() {
			
			try {
				Statement stmt = c.createStatement();
				String sql = "CREATE TABLE " + DATABASE + " (ID INT PRIMARY KEY NOT NULL, "
					+ "NAME TEXT NOT NULL, EMAIL TEXT NOT NULL, PASSWORD TEXT NOT NULL, " 
					+ "PENDORDER INT NOT NULL, POSITION TEXT NOT NULL, PREVORDER INT NOT NULL, "
					+ "ADDRESS TEXT NOT NULL);";
				stmt.executeUpdate(sql);
				stmt.close();
				c.close();
				System.out.println("The table has been created");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+e.getMessage());
				System.exit(0);
			}
			
		}
		
				
		/**
		 * getString
		 * gets variable from database; returns false if parameters off
		 * @param var <String> : variable to get from database
		 * @param where <String> : column of given value to identify user
		 * @param what <String> : given value to identify user
		 * @return variable <String> if input correct parameters; else returns "invalid"
		 */
		public String getString(String var, String where, String what) {
			
			try {
				
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery("select " + var + " from " + DATABASE + " where " + where + " = " + what + ";");
				rs.next();
				String strOut = rs.getString(var);
				rs.close();
				stmt.close();
				return strOut;
				
			} catch (Exception e) {
				return "invalid";
			}
			
			
		}
				
		/**
		 * close
		 * closes connection to database
		 */
		public void close() {
			try {
				c.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+e.getMessage());
				System.exit(0);
			}
		}
		
		/**
		 * deleteUser
		 * deletes user from database if id exists
		 * @param id <int> : id of deleted user
		 * @return true if id exists and user is deleted
		 */
		public boolean deleteUser(int id) {
			
			try {
				
				if (idExists(id)) {
					c.setAutoCommit(false);
					Statement stmt = c.createStatement();
					String sql = "DELETE from " + DATABASE + " where ID = " + Integer.toString(id) + ";";
					stmt.executeUpdate(sql);
					c.commit();
					stmt.close();
					
					return true;
				}
				
			} catch (Exception e) {
				return false;
			}
			
		}
		
		/**
		 * newID
		 * finds new 7-digit id #
		 * @return new id <int>
		 */
		public int newID() {
			
			Random rand = new Random();
			int newId = rand.nextInt(8999999) + 1000000;
			
			if (!idExists(newId)) {
				return newId;
			}
			return newID();
			
		}
		
		/**
		 * idExists
		 * checks if id is in database
		 * @param id <int>
		 * @return true if id is in database
		 */
		public boolean idExists(int id) {
			
			try {
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM " + DATABASE + " WHERE ID = " + Integer.toString(id) + ";");
				if (rs.next()) {
					stmt.close();
					rs.close();
					return true;
				}
					stmt.close();
					rs.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+e.getMessage());
				System.exit(0);
			}
			return false;
			
		}
		
		/**
		 * addUser
		 * Adds user to database if they have nondefault data for ID, NAME, EMAIL, PASSWORD, POSITION, ADDRESS
		 * @param user <User>
		 * @return true if user has complete data
		 */
		public boolean addUser(User user) {
			
			if (user.dataComplete()) {
				insert("INSERT INTO " + DATABASE + "(ID,NAME,EMAIL,PASSWORD,PENDORDER,POSITION,PREVORDER,ADDRESS) VALUES(" 
			+ user.getId() + ",'" + user.getName() + "','" + user.getEmail() + "','" + user.getPassword() + "'," + user.getPendorder() 
			+ ",'" + user.getPosition() + "',"+ user.getPrevorder() + ",'" + user.getAddress() + "');");
				return true;
			}
			return false;
		}
		
		/**
		 * findId
		 * finds id given email parameter
		 * @param email <String> User's email
		 * @return id <int> if email is in database; else returns -1
		 */
		public int findId(String email) {
			
			try {
				return Integer.parseInt(getString("ID", "EMAIL", "'" + email + "'"));
			} catch (Exception e) {
				return -1;
			}
						
		}
		
		/**
		 * getName
		 * returns name given id parameter
		 * @param id <int> User's id #
		 * @return name <String> if id is in database; else returns "invalid"
		 */
		public String getName(int id) {
			
			return getString("NAME", "ID", Integer.toString(id));
			
		}
		
		/**
		 * getEmail
		 * returns email given id parameter
		 * @param id <int> User's id #
		 * @return email <String> if id is in database; else returns "invalid"
		 */
		public String getEmail(int id) {
			
			return getString("EMAIL", "ID", Integer.toString(id));
			
		}
		
		/**
		 * getPassword
		 * returns password given id parameter
		 * @param id <int> User's id #
		 * @return password <String> if id is in database; else returns "invalid"
		 */
		public String getPassword(int id) {
			
			return getString("PASSWORD", "ID", Integer.toString(id));
			
		}
		
		/**
		 * getPosition
		 * returns position given id parameter
		 * @param id <int> User's id #
		 * @return position <String> if id is in database; else returns "invalid"
		 */
		public String getPosition(int id) {
			
			return getString("POSITION", "ID", Integer.toString(id));
			
		}
		
		/**
		 * getAddress
		 * returns address given id parameter
		 * @param id <int> User's id #
		 * @return address <String> if id is in database; else returns "invalid"
		 */
		public String getAddress(int id) {
			
			return getString("ADDRESS", "ID", Integer.toString(id));
			
		}
		
		/**
		 * getPendOrder
		 * returns pendorder given id parameter
		 * @param id <int> User's id #
		 * @return pendorder <int> if id is in database; else returns -1
		 */
		public int getPendOrder(int id) {
			
			try {
				return Integer.parseInt(getString("PENDORDER","ID",Integer.toString(id)));
			} catch (Exception e) {
				return -1;
			}
			
		}
		
		/**
		 * getPrevOrder
		 * returns prevorder given id parameter
		 * @param id <int> User's id #
		 * @return prevorder <int> if id is in database; else returns -1
		 */
		public int getPrevOrder(int id) {
			
			try {
				return Integer.parseInt(getString("PREVORDER","ID",Integer.toString(id)));
			} catch (Exception e) {
				return -1;
			}
			
		}
		
		/**
		 * updateName
		 * changes name if id exists
		 * @param id <int> User's id #
		 * @param newName <String> user's new name
		 * @return true if id is in database
		 */
		public boolean updateName(int id, String newName) {
			
			update("NAME", "'" + newName + "'", Integer.toString(id));
			
			return idExists(id);
			
		}
		
		/**
		 * updateEmail
		 * changes email if id exists
		 * @param id <int> User's id #
		 * @param newEmail <String> user's new email
		 * @return true if id is in database
		 */
		public boolean updateEmail(int id, String newEmail) {
			
			update("EMAIL",  "'" + newEmail + "'", Integer.toString(id));
			
			return idExists(id);
			
		}
		
		/**
		 * updatePassword
		 * changes password if id exists
		 * @param id <int> User's id #
		 * @param newPassword <String> user's new password
		 * @return true if id is in database
		 */
		public boolean updatePassword(int id, String newPassword) {
			
			update("PASSWORD",  "'" + newPassword + "'", Integer.toString(id));
			
			return idExists(id);
			
		}
		
		/**
		 * updatePosition
		 * changes position if id exists
		 * @param id <int> User's id #
		 * @param newPosition <String> user's new position
		 * @return true if id is in database
		 */
		public boolean updatePosition(int id, String newPosition) {
			
			update("POSITION",  "'" + newPosition + "'", Integer.toString(id));
			
			return idExists(id);
			
		}
		
		/**
		 * updatePendOrder
		 * changes pendorder if id exists
		 * @param id <int> User's id #
		 * @param newPendOrder <String> user's new pendorder (pending order id #)
		 * @return true if id is in database
		 */
		public boolean updatePendOrder(int id, int newPendOrder) {
			
			update("PENDORDER", "'" + newPendOrder + "'", Integer.toString(id));
			
			return idExists(id);
			
		}
		
		/**
		 * updatePrevOrder
		 * changes prevorder if id exists
		 * @param id <int> User's id #
		 * @param newPrevOrder <String> user's new prevorder (previous order id #)
		 * @return true if id is in database
		 */
		public boolean updatePrevOrder(int id, int newPrevOrder) {
			
			update("PREVORDER", "'" + newPrevOrder + "'", Integer.toString(id));
			
			return idExists(id);
			
		}

		/**
		 * updateAddress
		 * changes address if id exists
		 * @param id <int> User's id #
		 * @param newAddress <String> user's new address
		 * @return true if id is in database
		 */
		public boolean updateAddress(int id, String newAddress) {
			
			update("ADDRESS",  "'" + newAddress + "'", Integer.toString(id));
			
			return idExists(id);
			
		}
		
		/**
		 * userToString
		 * returns <String> of user data if id is in database; else returns "USER NOT FOUND"
		 * @param id <int> User's id #
		 * @return user's information <String>
		 */
		public String userToString(int id) {
			
			if (idExists(id)) {
				String userStr = "User #" + id + ": ";
				userStr += getName(id) + ", ";
				userStr += getEmail(id) + ", ";
				userStr += getPosition(id) + ", ";
				userStr += getPassword(id) + ", ";
				userStr += getAddress(id) + ", ";
				userStr += getPrevOrder(id) + ", ";
				userStr += getPendOrder(id);
				
				return userStr;
			}
			return "USER NOT FOUND";
		}
		
		/**
		 * update
		 * updates user in database if id exists
		 * @param what <String> : column being updated
		 * @param value <String> : value being inserted into column
		 * @param ID <String> : id of user being updated
		 * @return true if id exists and user is updated
		 */
		public boolean update(String what, String value, String ID) {
			
			boolean isUpdated = false;
			
			try {
				
				if (idExists(Integer.parseInt(ID))) {
					c.setAutoCommit(false);
					Statement stmt = c.createStatement();
					String sql = "UPDATE " + DATABASE + " set " + what + " = " + value + " where ID = " + ID +";";
					stmt.executeUpdate(sql);
					c.commit();
					stmt.close();
					isUpdated = true;
				}
				
			} catch (Exception e) {
				return false;
			}
			
			return isUpdated;
			
		}
		
		
		/**
		 * insert
		 * inserts command qry into database
		 * @param qry <String> : command entered into database
		 */
		public void insert(String qry) {
			
			try {
				c.setAutoCommit(false);
				Statement stmt = c.createStatement();
				stmt.executeUpdate(qry);
				stmt.close();
				c.commit();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e.getClass().getName()+": "+e.getMessage());
				System.exit(0);
			}
			
		}
		
}