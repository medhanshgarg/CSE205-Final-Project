public class User {
	
	private int id, pendorder = -1, prevorder = -1;
	private String name, email, password, position, address;

	User() {
		
		this(null,null,null,null,null);
				
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
	
	public int newID() {
		PostgresConnection db = new PostgresConnection();
		
		int newId = db.newID();
		db.close();
		
		return newId;
	}
	
	public boolean dataComplete() {
		if (getId() > 0 && getName() != null && getEmail() != null && getPassword() != null && getPosition() != null && getAddress() != null) {
			return true;
		}
		return false;
	}
	
	public boolean addToDatabase() {
		
		if (dataComplete()) {
			
			PostgresConnection db = new PostgresConnection();
			
			boolean userAdded = db.addUser(this);
			
			db.close();
			
			return userAdded;
			
		}
		
		return false;
		
	}
	
}