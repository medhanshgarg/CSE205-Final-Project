import java.util.ArrayList;

public class Order {

	private int orderId, buyerId;
	private boolean approved, purchased;
	private ArrayList<Integer> items;
	
	Order(int buyerId) {
		newOrder(buyerId);
	}
	
	Order(User user) {
		this(user, "pendorder");
	}
	
	Order(User user, String orderType) {
		PostgresConnection db = new PostgresConnection();
		if (orderType.equals("prevorder")) {
			updateOrder(db.orderToString(user.getPrevorder()));
		} else {
			if (user.getPendorder() != -1) {
				updateOrder(db.orderToString(user.getPendorder()));
				
			} else {
				newOrder(user.getId());
				user.setPendorder(getId());
				db.updateUser(user);
				db.addOrder(this);
			}
		}
		db.close();
	}
	
	// converts PostgresConnection.orderToString(orderId) to Order
	Order(String orderString) {
		
		updateOrder(orderString);
		
	}
	
	public void newOrder(int buyerId) {
		this.orderId = newID();
		this.buyerId = buyerId;
		this.approved = false;
		this.purchased = false;
		this.items = new ArrayList<Integer>();
		this.items.add(-1);
	}
	
	
	public void updateOrder(String orderString) {
		
		try {
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
		} catch (Exception e) {
			
		}
		
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
	
	public void removeItem(int itemId) {
		
		items.remove(items.indexOf(itemId));
		
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
	
	public int length() {
		
		return items.size();
		
	}
	
	public boolean containsItem(Item item) {
		
		return contains(item.getId());
		
	}
	
	public boolean contains(int itemId) {
		if (items.indexOf(itemId) != -1) {
			return true;
		}
		return false;
	}
	
	public int get(int index) {
		return items.get(index);
	}
	
	/**
	 * getItems
	 * returns items <ArrayList<Integer>> of input order's id if it exists
	 * @param itemReader <String>: items
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
	
	public int getItemQty(int itemId) {
	
		int qty = 0;
		
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).equals(itemId)) {
				qty ++;
			}
		}
		
		return qty;
		
	}
	
	public void addToCart(Item item, int qty) {
		
		addItem(item.getId());
		if (qty > 1) {
			addToCart(item, qty - 1);
		}
	}
	
	public void changeItemQty(int itemId, int qty) {
		while(items.indexOf(itemId) != -1) {
			items.remove(items.indexOf(itemId));
		}
		for (int i = 0; i < qty; i++) {
			items.add(itemId);
		}
	}
	
	public boolean isEmpty() {
		
		if (length() == 0) {
			
			return true;
		}
		
		return false;
	}
	
	public void checkOut() {
		
		purchase();
		User user = new User(getBuyer());
		
		if (user.getPrevorder() != -1) {
			Order existingOrder = new Order(user, "prevorder");
			if (contains(-1)) {
				removeItem(-1);
			}
			existingOrder.mergeOrder(this);
			deleteFromDatabase();
		} else {
			user.setPrevorder(getId());
		}
		user.setPendorder(new Order(user.getId()).getId());
		user.setPendorder(-1);
		updateInDatabase();
		user.updateInDatabase();
		
	}
	
	/**
	 * mergeOrder
	 * merges additionalOrder with existing order
	 * @param additionalOrder <Order> user's new order
	 */
	public void mergeOrder(Order additionalOrder) {
		
		for (int i = 0; i < additionalOrder.length(); i++) {
			addItem(additionalOrder.get(i));
		}
		updateInDatabase();
		
	}
	
	public void updateInDatabase() {
		PostgresConnection db = new PostgresConnection();
		db.updateOrder(this);
		db.close();
	}
	
	public void deleteFromDatabase() {
		PostgresConnection db = new PostgresConnection();
		db.deleteOrder(getId());
		db.close();
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
