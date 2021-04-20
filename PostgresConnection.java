import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class PostgresConnection {

		private Connection c = null;
		private final String DATABASE = "USERSPROTO";
		private final String ORDER_DATABASE = "ORDERSDB";
		
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
		 * createOrdersTable
		 * creates items table in sql database if not already created
		 */
		public void createOrdersTable() {
			
			try {
				Statement stmt = c.createStatement();
				String sql = "CREATE TABLE " + ORDER_DATABASE + " (ORDER_ID INT PRIMARY KEY NOT NULL, "
					+ "BUYER_ID INT NOT NULL, APPROVED BOOLEAN NOT NULL, ITEMS INTEGER[]);";
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
			
			boolean userDeleted = false;
			
			try {
				
				if (idExists(id)) {
					c.setAutoCommit(false);
					Statement stmt = c.createStatement();
					String sql = "DELETE from " + DATABASE + " where ID = " + Integer.toString(id) + ";";
					stmt.executeUpdate(sql);
					c.commit();
					stmt.close();
					
					userDeleted = true;
				}
				
			} catch (Exception e) {
				return false;
			}
			
			return userDeleted;
			
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
			
			return update("NAME", "'" + newName + "'", Integer.toString(id));
			
		}
		
		/**
		 * updateEmail
		 * changes email if id exists
		 * @param id <int> User's id #
		 * @param newEmail <String> user's new email
		 * @return true if id is in database
		 */
		public boolean updateEmail(int id, String newEmail) {
			
			return update("EMAIL",  "'" + newEmail + "'", Integer.toString(id));
			
		}
		
		/**
		 * updatePassword
		 * changes password if id exists
		 * @param id <int> User's id #
		 * @param newPassword <String> user's new password
		 * @return true if id is in database
		 */
		public boolean updatePassword(int id, String newPassword) {
			
			return update("PASSWORD",  "'" + newPassword + "'", Integer.toString(id));
			
		}
		
		/**
		 * updatePosition
		 * changes position if id exists
		 * @param id <int> User's id #
		 * @param newPosition <String> user's new position
		 * @return true if id is in database
		 */
		public boolean updatePosition(int id, String newPosition) {
			
			return update("POSITION",  "'" + newPosition + "'", Integer.toString(id));
			
		}
		
		/**
		 * updatePendOrder
		 * changes pendorder if id exists
		 * @param id <int> User's id #
		 * @param newPendOrder <String> user's new pendorder (pending order id #)
		 * @return true if id is in database
		 */
		public boolean updatePendOrder(int id, int newPendOrder) {
			
			return update("PENDORDER", "'" + newPendOrder + "'", Integer.toString(id));
			
		}
		
		/**
		 * updatePrevOrder
		 * changes prevorder if id exists
		 * @param id <int> User's id #
		 * @param newPrevOrder <String> user's new prevorder (previous order id #)
		 * @return true if id is in database
		 */
		public boolean updatePrevOrder(int id, int newPrevOrder) {
			
			return update("PREVORDER", "'" + newPrevOrder + "'", Integer.toString(id));
			
		}

		/**
		 * updateAddress
		 * changes address if id exists
		 * @param id <int> User's id #
		 * @param newAddress <String> user's new address
		 * @return true if id is in database
		 */
		public boolean updateAddress(int id, String newAddress) {
			
			return update("ADDRESS",  "'" + newAddress + "'", Integer.toString(id));
			
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
		 * orderToString
		 * returns <String> of order data if id is in database; else returns "ORDER NOT FOUND"
		 * @param orderId <int> order id #
		 * @return order information <String>
		 */
		public String orderToString(int orderId) {
			
			if (orderIdExists(orderId)) {
				
				String userStr = "Order #" + orderId + ": ";
				userStr += getBuyer(orderId) + ", ";
				userStr += getItems(orderId) + ", ";
				userStr += isApproved(orderId) ? "Approved" : "Not Approved"; 
				userStr += ", ";
				userStr += isPurchased(orderId) ? "Purchased" : "Not Purchased";
				
				return userStr;
				
			}
			
			return "ORDER NOT FOUND";
			
		}
		
		/**
		 * orderIdExists
		 * checks if orderId is in order database
		 * @param orderId <int>
		 * @return true if orderId is in database
		 */
		public boolean orderIdExists(int orderId) {
			
			try {
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM " + ORDER_DATABASE + " WHERE ORDER_ID = " + Integer.toString(orderId) + ";");
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
		 * findOrder
		 * returns order id <int> of input buyer's id if it exists
		 * @param buyer <int> : id of buyer
		 * @return order id if buyer's id is valid; else returns -1
		 */
		public int findOrder(int buyerId) {
			
			try {
				return Integer.parseInt(getString("ORDER", "BUYER_ID", Integer.toString(buyerId), ORDER_DATABASE));
			} catch (Exception e) {
				return -1;
			}
			
		}
		
		/**
		 * getBuyer
		 * returns buyer id <int> of input order's id if it exists
		 * @param orderId <int> : id of order
		 * @return buyer's id if order id is valid; else returns -1
		 */
		public int getBuyer(int orderId) {
			
			try {
				return Integer.parseInt(getString("BUYER_ID", "ORDER_ID", Integer.toString(orderId), ORDER_DATABASE));
			} catch (Exception e) {
				return -1;
			}
			
		}
		
		/**
		 * isApproved
		 * returns true if order is approved
		 * @param orderId <int> : id of order
		 * @return true if order is approved; else returns false
		 */
		public boolean isApproved(int orderId) {
			
			try {
				return (getString("APPROVED", "ORDER_ID", Integer.toString(orderId), ORDER_DATABASE).compareTo("t") == 0);
			} catch (Exception e) {
				return false;
			}
		}
		
		/**
		 * isPurchased
		 * returns true if order is purchased
		 * @param orderId <int> : id of order
		 * @return true if order is purchased; else returns false
		 */
		public boolean isPurchased(int orderId) {
			
			try {
				return (getString("APPROVED", "ORDER_ID", Integer.toString(orderId), ORDER_DATABASE).compareTo("t") == 0);
			} catch (Exception e) {
				return false;
			}
		}
		
		/**
		 * getItems
		 * returns items <ArrayList<Integer>> of input order's id if it exists
		 * @param orderId <int> : id of order
		 * @return items <ArrayList<Integer>> if valid id entered; else returns null
		 */
		public ArrayList<Integer> getItems(int orderId) {
			
			try {
				
				// String of item list
				String itemReader =  getString("ITEMS", "ORDER_ID", Integer.toString(orderId), ORDER_DATABASE);
				
				// removing curly braces; converting to #,#,# format
				itemReader = itemReader.substring(1,itemReader.length()-1);
				
				ArrayList<Integer> items = new ArrayList<Integer>();
				
				// while itemList still has commas
				while (itemReader.indexOf(',') != -1) {
					
					// add next item to list
					items.add(Integer.parseInt(itemReader.substring(0,itemReader.indexOf(','))));
					
					// remove next comma from string
					itemReader = itemReader.substring(itemReader.indexOf(',') + 1);
				}
				
				// add last item to list
				items.add(Integer.parseInt(itemReader));
				
				// return item list
				return items;
				
			} catch (Exception e) {
				return null;
			}
		}
		
		/**
		 * addOrder
		 * adds order to orders table
		 * @param order <int> : id of order
		 * @return true if order is added; else returns false
		 */
		public boolean addOrder(Order order) {
			
			try {
				insert("INSERT INTO " + ORDER_DATABASE + "(ORDER_ID,BUYER_ID,APPROVED,ITEMS,PURCHASED) VALUES(" 
			+ order.getId() + "," + order.getBuyer() + ",'" + order.isApproved() + "'," + order.getItemsPostgres()+ "," + order.isPurchased() +");");
				return true;
			} catch(Exception e) {
				return false;
			}
			
		}
		
		/**
		 * updateBuyer
		 * changes buyer id if order id exists
		 * @param orderId <int> order's id #
		 * @param newBuyerId <id> new buyer id
		 * @return true if orderId is in database
		 */
		public boolean updateBuyer(int orderId, int newBuyerId) {
			
			return updateOrder("BUYER_ID", Integer.toString(newBuyerId), Integer.toString(orderId));
			
		}
		
		/**
		 * updateOrderId
		 * changes order id if original order id exists
		 * @param originalOrderId <int> order's original id #
		 * @param newOrderId <id> new buyer id
		 * @return true if originalOrderId is in database
		 */
		public boolean updateOrderId(int originalOrderId, int newOrderId) {
			
			return updateOrder("ORDER_ID", Integer.toString(newOrderId), Integer.toString(originalOrderId));
			
		}
		
		/**
		 * addItem
		 * adds item to order if orderId exists
		 * @param orderId <int> order's id #
		 * @param newItem <id> new item
		 * @return true if orderId is in database
		 */
		public boolean addItem(int orderId, int newItem) {
			
			ArrayList<Integer> items = getItems(orderId);
			items.add(newItem);			
			
			return updateOrder("ITEMS", "ARRAY" + items.toString(), Integer.toString(orderId));
			
		}
		
		/**
		 * deleteItem
		 * deletes item from order if orderId exists
		 * @param orderId <int> order's id #
		 * @param deletedItem <id> new item
		 * @return true if orderId is in database
		 */
		public boolean deleteItem(int orderId, int deletedItem) {
			
			ArrayList<Integer> items = getItems(orderId);
			
			if (items.indexOf(deletedItem) != -1) {
				items.remove(items.indexOf(deletedItem));			
				return updateOrder("ITEMS", "ARRAY" + items.toString(), Integer.toString(orderId));
			}
			
			return false;
			
		}
		
		/**
		 * approveOrder
		 * approves order if orderId exists
		 * @param orderId <int> order's id #
		 * @return true if orderId is in database
		 */
		public boolean approveOrder(int orderId) {
			
			return updateOrder("APPROVED", "TRUE", Integer.toString(orderId));
			
		}
		
		/**
		 * deleteOrder
		 * deletes order if orderId exists
		 * @param orderId <int> order's id #
		 * @return true if orderId is in database
		 */
		public boolean deleteOrder(int orderId) {
			
			boolean orderDeleted = false;
			
			try {
				
				if (orderIdExists(orderId)) {
					c.setAutoCommit(false);
					Statement stmt = c.createStatement();
					String sql = "DELETE from " + ORDER_DATABASE + " where ORDER_ID = " + Integer.toString(orderId) + ";";
					stmt.executeUpdate(sql);
					c.commit();
					stmt.close();
					
					orderDeleted = true;
				}
				
			} catch (Exception e) {
				return false;
			}
			
			return orderDeleted;
			
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
			
			boolean updated = false;
			
			try {
				
				if (idExists(Integer.parseInt(ID))) {
					c.setAutoCommit(false);
					Statement stmt = c.createStatement();
					String sql = "UPDATE " + DATABASE + " set " + what + " = " + value + " where ID = " + ID +";";
					stmt.executeUpdate(sql);
					c.commit();
					stmt.close();
					updated = true;
				}
				
			} catch (Exception e) {
				return false;
			}
			
			return updated;
			
		}
		
		/**
		 * update
		 * updates item in database if parameters correct
		 * @param what <String> : column being updated
		 * @param value <String> : value being inserted into column
		 * @param ID <String> : id of row being updated
		 * @param database <String> : database to update
		 * @return true if id exists and row is updated
		 */
		public boolean updateOrder(String what, String value, String orderID) {
			
			boolean updated = false;
			
			try {
				
				if (orderIdExists(Integer.parseInt(orderID))) {
					c.setAutoCommit(false);
					Statement stmt = c.createStatement();
					String sql = "UPDATE " + ORDER_DATABASE + " set " + what + " = " + value + " where ORDER_ID = " + orderID +";";
					stmt.executeUpdate(sql);
					c.commit();
					stmt.close();
					updated = true;
				}
				
			} catch (Exception e) {
				return false;
			}
			
			return updated;
			
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
		
		/**
		 * getString
		 * gets variable from database; returns false if parameters off
		 * @param var <String> : variable to get from database
		 * @param where <String> : column of given value to identify user
		 * @param what <String> : given value to identify user
		 * @return variable <String> if input correct parameters; else returns "invalid"
		 */
		public String getString(String var, String where, String what) {
			
			return getString(var,where,what,DATABASE);
			
			
		}
		
		/**
		 * getString
		 * gets variable from database; returns false if parameters off
		 * @param var <String> : variable to get from database
		 * @param where <String> : column of given value to identify row
		 * @param what <String> : given value to identify row
		 * @param db <String> : database to pull string from
		 * @return variable <String> if input correct parameters; else returns "invalid"
		 */
		public String getString(String var, String where, String what, String db) {
			
			try {
				
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery("select " + var + " from " + db + " where " + where + " = " + what + ";");
				rs.next();
				String strOut = rs.getString(var);
				rs.close();
				stmt.close();
				return strOut;
				
			} catch (Exception e) {
				return "invalid";
			}
			
			
		}
		
}
