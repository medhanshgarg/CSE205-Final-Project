
public class Item {

	private String name;
	private int id;
	private double price;
	
	Item() {
		this("UNTITLED", -1, -1);
	}
	
	Item(String name, int id, double price) {
		this.name = name;
		this.id = id;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public String toString() {
		
		String str = "Item #";
		str += getId() + ": ";
		str += getName() + ", $";
		str += getPrice();
		return str;
		
	}
	
}
