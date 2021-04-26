import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ItemModification extends JFrame implements ActionListener {
	
	ItemList pageItems, itemBank;
	String position;
	Container c;
	
	JTextField searchBar;
	JButton searchButton, addItemButton, logoutButton, pendorderButton;
	JLabel editItemsLabel;
	
	JLabel[] itemNameLabels,itemQtyLabels, itemPriceLabels, itemNumberLabels, totalPriceLabels;
	JTextField[] itemQtys, itemPriceTexts;
	JButton[] itemQtyButtons, updatePriceButtons, deleteItemButtons;
	
	ItemModification(String position) {
		this(position, new ItemList());
	}
	
	ItemModification(String position, ItemList itemBank) {
		
		this.position = position;
		this.itemBank = itemBank;
		this.pageItems = new ItemList();
		while (pageItems.length() > 0) {
			pageItems.remove(0);
		}
		for (int i = 0; i < itemBank.length(); i++) {
			pageItems.add(itemBank.get(i));
		}
		
		setTitle("Item Modification Page");
		setSize(1000,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		c=getContentPane();
		c.setLayout(null);
		
		searchBar = new JTextField();
		searchBar.setBounds(180,20,370,20);
		c.add(searchBar);
		
		searchButton = new JButton("search");
		searchButton.setBounds(550,20,100,20);
		searchButton.addActionListener(this);
		c.add(searchButton);
		
		editItemsLabel = new JLabel("edit items:");
		editItemsLabel.setBounds(250,60,150,25);
		editItemsLabel.setFont(new Font("Times New Roman",Font.PLAIN,25));
		c.add(editItemsLabel);
		
		logoutButton = new JButton("Log out");
		logoutButton.setBounds(40,20,80,20);
		logoutButton.addActionListener(this);
		c.add(logoutButton);
		
		addItemButton = new JButton("Add New Item");
		addItemButton.setBounds(140,570,150,40);
		addItemButton.addActionListener(this);
		c.add(addItemButton);
		if (position.equals(User.EMPLOYEE_POSITION)) {
			pendorderButton = new JButton("Pending Orders");
			pendorderButton.setBounds(500,70,150,30);
			pendorderButton.addActionListener(this);
			c.add(pendorderButton);
		}
		
		itemNameLabels = new JLabel[4];
		itemQtys = new JTextField[4];
		itemQtyLabels = new JLabel[4];
		itemQtyButtons = new JButton[4];
		itemPriceLabels = new JLabel[4];
		itemNumberLabels = new JLabel[4];
		
		itemPriceTexts = new JTextField[4];
		totalPriceLabels = new JLabel[4];
		updatePriceButtons = new JButton[4];
		
		deleteItemButtons = new JButton[4];
		
		for (int i = 0; i < pageItems.length(); i++) {
			displayItem(i, 120 + 125*i);
		}
		
		setVisible(true);
		
	}
	
	public void displayItem(int index, int y) {
		
		itemNameLabels[index] = itemNameLabel(pageItems.get(index), 20, y, 100, 20);
		c.add(itemNameLabels[index]);
		
		itemQtys[index] = itemQty(pageItems.get(index), 130, y + 40, 60, 20);
		c.add(itemQtys[index]);
		
		itemQtyLabels[index] = itemQtyLabel(20, y + 40, 100, 20);
		c.add(itemQtyLabels[index]);
		
		itemQtyButtons[index] = updateButton(190, y + 40, 80, 20);
		c.add(itemQtyButtons[index]);
		
		itemPriceLabels[index] = itemPriceLabel(pageItems.get(index), 118, y, 10, 20);
		c.add(itemPriceLabels[index]);
		
		itemPriceTexts[index] = itemPriceText(pageItems.get(index), 130, y, 60, 20);
		c.add(itemPriceTexts[index]);
		
		updatePriceButtons[index] = updateButton(190, y, 80, 20);
		c.add(updatePriceButtons[index]);
		
		itemNumberLabels[index] = itemNumberLabel(pageItems.get(index), 300, y, 100, 20);
		c.add(itemNumberLabels[index]);
		
		deleteItemButtons[index] = deleteItemButton(300, y + 40, 80, 20);
		c.add(deleteItemButtons[index]);
		
	}
	
	public void removeItems() {
		for (int i = 0; i < pageItems.length(); i++) {
			c.remove(itemNameLabels[i]);
			c.remove(itemQtys[i]);
			c.remove(itemQtyLabels[i]);
			c.remove(itemQtyButtons[i]);
			c.remove(itemPriceLabels[i]);
			c.remove(itemPriceTexts[i]);
			c.remove(updatePriceButtons[i]);
			c.remove(itemNumberLabels[i]);
			c.remove(deleteItemButtons[i]);
		}
	}
	
	public void search(String search) {
		
		ArrayList<String> searchItems = getSearchItems(search);
		
		removeItems();
		while (pageItems.length() > 0) {
			pageItems.remove(0);
		}
		if (search.equals("")) {
			for (int i = 0; i < itemBank.length(); i++) {
				pageItems.add(itemBank.get(i));
			}
		} else {
			for (int i = 0; i < itemBank.length(); i++) {
				
				Boolean exit = false;
				for (int j = 0; j < searchItems.size() && !exit; j++) {
					
					if (itemBank.get(i).getTags().indexOf(searchItems.get(j)) != -1) {
						pageItems.add(itemBank.get(i));
						exit = true;
					}
				}
			}
		}
		
		for (int i = 0; i < pageItems.length(); i++) {
			displayItem(i, 120 + 125*i);
		}
		c.repaint();
		
	}
	
	public ArrayList<String> getSearchItems(String search) {
		ArrayList<String> searchItems = new ArrayList<String>();
		int index = 0;
		while (index < search.length()-1 && search.lastIndexOf(" ") > index) {
			if (search.substring(index, index + 1).compareTo(" ") != 0) {
				searchItems.add(search.substring(index, search.substring(index).indexOf(" ") + index).toUpperCase());
				index += search.substring(index).indexOf(" ") + 1;
			} else {
				index ++;
			}
		}
		if (!search.substring(index).equals(" ") && index < search.length()-1) {
			searchItems.add(search.substring(index).toUpperCase());
		}
		return searchItems;
	}
	
	public JLabel itemNameLabel(Item item, int x, int y, int width, int height) {
		JLabel itemLabel = new JLabel();
		itemLabel.setText(item.getName());
		itemLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		itemLabel.setBounds(x,y,width,height);
		return itemLabel;
	}
	
	public JTextField itemQty(Item item, int x, int y, int width, int height) {
		
		JTextField itemQty = new JTextField();
		itemQty.setText(Integer.toString(item.getQuantity()));
		itemQty.setBounds(x, y, width, height);
		return itemQty;
		
	}
	
	public JLabel itemQtyLabel(int x, int y, int width, int height) {
		JLabel itemQty = new JLabel();
		itemQty.setBounds(x, y, width, height);
		itemQty.setFont(new Font("Arial", Font.PLAIN, 15));
		itemQty.setText("Quantity:");
		return itemQty;
	}
	
	public JButton updateButton(int x, int y, int width, int height) {
		JButton updateButton = new JButton("update");
		updateButton.setBounds(x,y,width,height);
		updateButton.addActionListener(this);
		return updateButton;
	}
	
	public JButton deleteItemButton(int x, int y, int width, int height) {
		JButton deleteItemButton = new JButton("delete");
		deleteItemButton.setBounds(x,y,width,height);
		deleteItemButton.addActionListener(this);
		return deleteItemButton;
	}
	
	public JLabel itemPriceLabel(Item item, int x, int y, int width, int height) {
		JLabel itemPrice = new JLabel();
		itemPrice.setBounds(x, y, width, height);
		itemPrice.setFont(new Font("Arial", Font.PLAIN, 16));
		itemPrice.setText("$");
		return itemPrice;
	}
	
	public JTextField itemPriceText(Item item, int x, int y, int width, int height) {
		JTextField itemPrice = new JTextField();
		itemPrice.setBounds(x, y, width, height);
		itemPrice.setText(item.getPriceCurrencyFormat().substring(1));
		return itemPrice;
	}
	
	public JLabel itemNumberLabel(Item item, int x, int y, int width, int height) {
		JLabel itemNumber = new JLabel();
		itemNumber.setBounds(x, y, width, height);
		itemNumber.setFont(new Font("Arial", Font.PLAIN, 12));
		itemNumber.setForeground(new Color(120,120,120));
		itemNumber.setText("Item Number " + Integer.toString(item.getId()));
		return itemNumber;
	}
	
	public JLabel totalPriceLabel(int index, int qty, int x, int y, int width, int height) {
		
		JLabel label = new JLabel();
		label.setBounds(x, y, width, height);
		label.setFont(new Font("Arial", Font.PLAIN, 16));
		label.setForeground(new Color(120,120,180));
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		label.setText("Total Price: " + formatter.format(((double) qty * pageItems.get(index).getPrice())));
		
		return label;
		
	}
	
	public JLabel addedToCartLabel(int index, int qty, Rectangle bounds) {
		
		JLabel label = new JLabel();
		label.setBounds((int) bounds.getX() + 160, (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		label.setForeground(new Color(120,120,180));
		label.setText("added x" + qty + " to cart!");
		return label;
		
	}
	
	public void repaintContainer() {
		removeItems();
		for (int i = 0; i < pageItems.length(); i++) {
			displayItem(i, 120 + 125*i);
		}
		c.repaint();
	}
	
	public int getBankLength() {
		return itemBank.length();
	}
	
	public void addItemToBank(Item item) {
		itemBank.add(item);
		if (itemBank.length() >= 4) {
			c.remove(addItemButton);
		}
	}
	
	public void setSearchText(String text) {
		searchBar.setText(text);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getSource() == searchButton) {
			search(searchBar.getText());
			return;
		}

		if (e.getSource() == addItemButton) {
			AddItemPop ai = new AddItemPop(this);
			c.repaint();
			return;
		}
		
		if (e.getSource() == logoutButton) {
			dispose();
			return;
		}
		
		if (e.getSource() == pendorderButton) {
			dispose();
			Main.openPendingOrders(itemBank);
			return;
		}
		
		for (int i = 0; i < pageItems.length(); i++) {
			
			if (e.getSource() == itemQtyButtons[i]) {
				
				pageItems.get(i).setQuantity(Integer.parseInt(itemQtys[i].getText()));
				itemBank.get(i).setQuantity(Integer.parseInt(itemQtys[i].getText()));
				return;
			}
			
			if (e.getSource() == updatePriceButtons[i]) {
				
				pageItems.get(i).setPrice(Double.parseDouble(itemPriceTexts[i].getText()));
				itemBank.get(i).setPrice(Double.parseDouble(itemPriceTexts[i].getText()));
				itemPriceTexts[i].setText(pageItems.get(i).getPriceCurrencyFormat().substring(1));
				return;
			}
			
			if (e.getSource() == deleteItemButtons[i]) {
				removeItems();
				pageItems.remove(i);
				itemBank.remove(i);
				for (int j = 0; j < pageItems.length(); j++) {
					displayItem(j, 120 + 125*j);
				}
				if (itemBank.length() < 4) {
					c.add(addItemButton);
				}
				c.repaint();
				return;
			}
			
		}
		
	}
	
}       
