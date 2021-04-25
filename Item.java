import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JLabel;

public class Item {

	private String name;
	private int id;
	private double price;
	private ArrayList<String> tags;
	
	Item() {
		this("UNTITLED", -1, -1, new ArrayList<String>());
	}
	
	Item(String name, int id, double price, ArrayList<String> tags) {
		this.name = name.toUpperCase();
		this.id = id;
		this.price = price;
		this.tags = tags;
		if (this.tags.indexOf(this.name) == -1)
			this.tags.add(this.name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		tags.remove(tags.indexOf(this.name));
		this.name = name.toUpperCase();
		tags.add(this.name);
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
	
	public String getPriceCurrencyFormat() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String price = formatter.format(getPrice());
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public ArrayList<String> getTags() {
		return this.tags;
	}
	
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	// MAYBE REMOVE
	public JLabel itemLabel(int x, int y, int width, int height) {
		JLabel itemLabel = new JLabel();
		itemLabel.setText(getName());
		itemLabel.setBounds(x,y,width,height);
		return itemLabel;
	}
	
	public String toString() {
		
		String str = getName();
		str += " (" + getId() + ")";
		return str;
		
	}
	
	public String allToString() {
		String str = toString() + ", ";
		str += getPrice() + ", ";
		str += getTags().toString();
		return str;
	}
	
}
