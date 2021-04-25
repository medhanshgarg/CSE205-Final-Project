public class User {
	
	private int id, pendorder = -1, prevorder = -1;
	private String name, email, password, position, address;
	public static final String MANAGER_POSITION = "MANAGER";
	public static final String EMPLOYEE_POSITION = "EMPLOYEE";
	public static final String CUSTOMER_POSITION = "CUSTOMER";

	User() {
		
		this(null,null,null,null,null);
				
	}
	
	// converts PostgresConnection.userToString(userId) to User
	User(String userString) {
		
		String userReader = userString.substring(userString.indexOf('#') + 1);
		
		this.id = Integer.parseInt(userReader.substring(0,userReader.indexOf(':')));
		
		userReader = userReader.substring(userReader.indexOf(':') + 2);
		
		this.name = userReader.substring(0,userReader.indexOf(", "));
		
		userReader = userReader.substring(userReader.indexOf(", ") + 2);
		
		this.email = userReader.substring(0,userReader.indexOf(", "));
		
		userReader = userReader.substring(userReader.indexOf(", ") + 2);
		
		this.position = userReader.substring(0,userReader.indexOf(", "));
		
		userReader = userReader.substring(userReader.indexOf(", ") + 2);
		
		this.password = userReader.substring(0,userReader.indexOf(", "));
		
		userReader = userReader.substring(userReader.indexOf(", ") + 2);
		
		this.address = userReader.substring(0,userReader.indexOf(", "));
		
		userReader = userReader.substring(userReader.indexOf(", ") + 2);
		
		this.prevorder = Integer.parseInt(userReader.substring(0,userReader.indexOf(", ")));
		
		userReader = userReader.substring(userReader.indexOf(", ") + 2);
		
		this.pendorder = Integer.parseInt(userReader);
		
	}
	
	User(int id) {
		
		PostgresConnection db = new PostgresConnection();
		
		String userReader = db.userToString(id);
		
		db.close();
		
		userReader = userReader.substring(userReader.indexOf('#') + 1);
		
		this.id = Integer.parseInt(userReader.substring(0,userReader.indexOf(':')));
		
		userReader = userReader.substring(userReader.indexOf(':') + 2);
		
		this.name = userReader.substring(0,userReader.indexOf(", "));
		
		userReader = userReader.substring(userReader.indexOf(", ") + 2);
		
		this.email = userReader.substring(0,userReader.indexOf(", "));
		
		userReader = userReader.substring(userReader.indexOf(", ") + 2);
		
		this.position = userReader.substring(0,userReader.indexOf(", "));
		
		userReader = userReader.substring(userReader.indexOf(", ") + 2);
		
		this.password = userReader.substring(0,userReader.indexOf(", "));
		
		userReader = userReader.substring(userReader.indexOf(", ") + 2);
		
		this.address = userReader.substring(0,userReader.indexOf(", "));
		
		userReader = userReader.substring(userReader.indexOf(", ") + 2);
		
		this.prevorder = Integer.parseInt(userReader.substring(0,userReader.indexOf(", ")));
		
		userReader = userReader.substring(userReader.indexOf(", ") + 2);
		
		this.pendorder = Integer.parseInt(userReader);
		
	}
	
	User(String name, String email, String password, String position, String address) {
		
		this.name = name;
		this.email = email;
		this.password = password;
		this.position = position;
		this.address = address;
		this.id = newID();
		this.pendorder = -1;
		this.prevorder = -1;
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPendorder() {
		return pendorder;
	}
	public void setPendorder(int pendorder) {
		this.pendorder = pendorder;
	}
	public int getPrevorder() {
		return prevorder;
	}
	public void setPrevorder(int prevorder) {
		this.prevorder = prevorder;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	// returns new 7 digit id
	public int newID() {
		PostgresConnection db = new PostgresConnection();
		
		int newId = db.newID();
		db.close();
		
		return newId;
	}
	
	// returns true if pendorder includes item
	public boolean pendorderIncludes(Item item) {
		
		PostgresConnection db = new PostgresConnection();
		Order order = new Order(db.orderToString(getPendorder()));
		db.close();
		return order.containsItem(item);
		
	}
	
	// returns true if user has an id, name, valid email, password, position, and address
	public boolean dataComplete() {
		if (getId() > 0 && getName() != null && emailIsValid(getEmail()) && getPassword() != null && getPosition() != null && getAddress() != null) {
			return true;
		}
		return false;
	}
	
	// adds user to database; returns true if successful
	public boolean addToDatabase() {
		if (dataComplete()) {
			PostgresConnection db = new PostgresConnection();
			
			boolean userAdded = db.addUser(this);
			
			db.close();
			
			return userAdded;
			
		}
		
		return false;
		
	}
	
	// returns true if email is valid
	public static boolean emailIsValid(String email) {
		
		PostgresConnection db = new PostgresConnection();
		if (db.findId(email) != -1) {
			return false;
		}
		db.close();
		
		String emailReader;
		
		// does email containt ' ' (space)
		if (email.indexOf(' ') != -1) {
			return false;
		}
		
		// does email contain '@'
		if (email.indexOf('@') == -1) {
			return false;
		}
		
		// does email contain at least one character before '@'
		emailReader = email.substring(0,email.indexOf('@'));
		if (emailReader.length() < 1) {
			return false;
		}
		// does email contain '.' after '@'
		if (email.indexOf('.',email.indexOf('@')) == -1) {
			return false;
		}
		
		// does email contain at least one character between '@' and '.'
		emailReader = email.substring(email.indexOf('@'),email.indexOf('.') + 1);
		if (emailReader.length() < 1) {
			return false;
		}
		
		// does email contain at least one character after '.' after '@'
		emailReader = email.substring(email.indexOf('.',email.indexOf('@')) + 1);
		if (emailReader.length() < 1) {
			return false;
		}
		
		// email is valid
		return true;
		
	}
	
	public void updateInDatabase() {
		PostgresConnection db = new PostgresConnection();
		db.updateUser(this);
		db.close();
	}
	
	public String toString() {
		
		String str = "User #";
		str += getId() + ": ";
		str += getName() + ", ";
		str += getEmail() + ", ";
		str += getPosition() + ", ";
		str += getPassword() + ", ";
		str += getAddress() + ", ";
		str += getPrevorder() + ", ";
		str += getPendorder();
		return str;
	}
	
	
}
