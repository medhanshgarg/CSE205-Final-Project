import java.util.ArrayList;

public class Order {

	private int orderId, buyerId;
	private boolean approved, purchased;
	private ArrayList<Integer> items;
	
	Order(int buyerId) {
		this.orderId = newID();
		this.buyerId = buyerId;
		this.approved = false;
		this.purchased = false;
		this.items = new ArrayList<Integer>();
	}
	
	// converts PostgresConnection.orderToString(orderId) to Order
	Order(String orderString) {
		
		String orderReader = orderString;
		
		orderReader = orderReader.substring(orderReader.indexOf('#') + 1);
		
		this.orderId = Integer.parseInt(orderReader.substring(0,orderReader.indexOf(':')));
		
		orderReader = orderReader.substring(orderReader.indexOf(':') + 2);
		
		this.buyerId = Integer.parseInt(orderReader.substring(0, orderReader.indexOf(',')));
		
		orderReader = orderReader.substring(orderReader.indexOf(',') + 2);
		
		this.items = getItems(orderReader.substring(0,orderReader.indexOf(']') + 1));
		
		orderReader = orderReader.substring(orderReader.indexOf(']') + 3);
		
		this.approved = orderReader.substring(0,orderReader.indexOf(',')).compareTo("Approved") == 0;
		
		orderReader = orderReader.substring(orderReader.indexOf(',') + 2);
		
		this.purchased = orderReader.compareTo("Purchased") == 0;
		
	}
	
	public void approve() {
		approved = true;
	}
	
	public void purchase() {
		purchased = true;
	}
	
	public void addItem(int itemId) {
		
		items.add(itemId);
		
	}
	
	public int newID() {
		PostgresConnection db = new PostgresConnection();
		
		int newId = db.newID();
		db.close();
		
		return newId;
	}
	
	public int getId() {
		return orderId;
	}
	
	public int getBuyer() {
		return buyerId;
	}
	
	// returns items in format compatible with postgres
	public String getItemsPostgres() {
		
		return "ARRAY" + getItems();
		
	}
	
	public String getItems() {
		
		return items.toString();
		
	}
	
	public boolean isApproved() {
		
		return approved;
		
	}
	
	public boolean isPurchased() {
		
		return purchased;
		
	}
	
	/**
	 * getItems
	 * returns items <ArrayList<Integer>> of input order's id if it exists
	 * @param orderId <int> : id of order
	 * @return items <ArrayList<Integer>> if valid id entered; else returns null
	 */
	public ArrayList<Integer> getItems(String itemReader) {
		
		try {
			
			// removing curly braces; converting to #,#,# format
			itemReader = itemReader.substring(1,itemReader.length()-1);
			
			ArrayList<Integer> items = new ArrayList<Integer>();
			
			// while itemList still has commas
			while (itemReader.indexOf(',') != -1) {
				
				// add next item to list
				items.add(Integer.parseInt(itemReader.substring(0,itemReader.indexOf(','))));
				
				// remove next comma & space from string
				itemReader = itemReader.substring(itemReader.indexOf(',') + 2);
			}
			
			// add last item to list
			items.add(Integer.parseInt(itemReader));
			
			// return item list
			return items;
			
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public String toString() {
		
		String orderStr = "Order #" + getId() + ": ";
		orderStr += getBuyer() + ", ";
		orderStr += getItems() + ", ";
		orderStr += isApproved() ? "Approved" : "Not Approved";
		orderStr += ", ";
		orderStr += isPurchased() ? "Purchased" : "Not Purchased";
		
		return orderStr;
		
	}
	
}
