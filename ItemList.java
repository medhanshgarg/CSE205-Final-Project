import java.util.ArrayList;

public class ItemList {

	private ArrayList<Item> itemList;
	private ArrayList<String> itemTags;
	
	ItemList() {
		itemList = new ArrayList<Item>();
		itemTags = new ArrayList<String>();
		addItemStock();
	}
	
	ItemList(ArrayList<Item> itemList, ArrayList<String> itemTags) {
		this.itemList = itemList;
		this.itemTags = itemTags;
	}
	
	ItemList(ArrayList<String> tags) {
		this.itemList = new ArrayList<Item>();
		this.itemTags = new ArrayList<String>();
		ItemList bank = new ItemList();
		for (int i = 0; i < tags.size(); i++) {
			ArrayList<Integer> itemIds = searchTag(tags.get(i));
			for (int j = 0; j < itemIds.size(); j++) {
				try {
					getItem(itemIds.get(j)).equals(null);
				} catch (Exception e) {
					add(bank.getItem(itemIds.get(j)));
					addTags(bank.getItem(itemIds.get(j)).getTags());
					getItem(itemIds.get(j)).setTags(bank.getItem(itemIds.get(j)).getTags());
				}
				/*
				if (getItem(itemIds.get(j)).equals(null)) {
					add(bank.getItem(itemIds.get(j)));
					addTags(bank.getItem(itemIds.get(j)).getTags());
					getItem(itemIds.get(j)).setTags(bank.getItem(itemIds.get(j)).getTags());
				}
				*/
			}
		}
	}
	
	public ArrayList<Item> getItemList() {
		return itemList;
	}
	
	public ArrayList<String> getTags() {
		return itemTags;
	}
	
	public void setItemList(ArrayList<Item> itemList) {
		this.itemList = itemList;
	}
	
	public void add(Item item) {
		itemList.add(item);
	}
	
	public void remove(int itemIndex) {
		if (itemIndex < length() && itemIndex >= 0) {
			itemList.remove(itemIndex);
		}
	}
	
	public void addTag(String tag) {
		itemTags.add(tag.toUpperCase());
	}
	
	public void addTags(ArrayList<String> tags) {
		for (int i = 0; i < tags.size(); i++) {
			itemTags.add(tags.get(i));
		}
	}
	
	public void removeTag(int tagIndex) {
		if (tagIndex < tagLength() && tagIndex >= 0) {
			itemTags.remove(tagIndex);
		}
	}
	
	public Item getItem(int itemId) {
		for (int i = 0; i < itemList.size(); i++) {
			if (itemList.get(i).getId() == itemId) {
				return itemList.get(i);
			}
		}
		return null;
	}
	
	public Item get(int index) {
		return itemList.get(index);
	}
	
	public int indexOf(Item item) {
		for (int i = 0; i < itemList.size(); i++) {
			if (itemList.get(i).toString().equals(item.toString())) {
				return i;
			}
		}
		return -1;
	}
	
	public int length() {
		return itemList.size();
	}
	
	public int tagLength() {
		return itemTags.size();
	}
	
	// return item id with tag
	public ArrayList<Integer> searchTag(String tag) {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		ItemList bank = new ItemList();
		for (int i = 0; i < bank.length(); i++) {
			int index = bank.get(i).getTags().indexOf(tag.toUpperCase());
			if (index != -1) {
				indexes.add(bank.get(i).getId());
			}
			index = -1;
		}
		return indexes;
	}
	
	public void addItemStock() {
		addStockTags();
		itemList.add(new Item("CAKE",1,19.5,new ArrayList<String>(itemTags.subList(0, 3))));
		itemList.add(new Item("CHAIR", 2, 26.5, new ArrayList<String>(itemTags.subList(2, 5))));
		itemList.add(new Item("BOOK", 3, 8.5, new ArrayList<String>(itemTags.subList(5,7))));
	}
	
	public void addStockTags() {
		itemTags.add("FOOD");
		itemTags.add("DESSERT");
		itemTags.add("KITCHEN");
		itemTags.add("FANCY");
		itemTags.add("SEAT");
		itemTags.add("TEXT");
		itemTags.add("LITERATURE");
	}
	
	public String toString() {
		return itemList.toString();
	}
	
	public String allToString() {
		String str = "[";
		for (int i = 0; i < itemList.size() - 1; i++) {
			str += itemList.get(i).allToString() + ", ";
		}
		str += itemList.get(itemList.size()-1).allToString() + "]";
		return str;
	}
	
}
