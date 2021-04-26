import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class ShoppingPage extends JFrame implements ActionListener {
	
	ItemList pageItems, itemBank;
	Container c;
	User user;
	Order order;
	
	JTextField searchBar;
	JButton searchButton, checkOutButton, logoutButton;
	
	JLabel[] itemNameLabels,itemQtyLabels, itemPriceLabels, itemNumberLabels, addedToCartLabels;
	JComboBox[] itemQtys;
	JButton[] addToCartButtons;

	ShoppingPage(User user) {
		
		this(user, "");
	}
	
	ShoppingPage(User user, String search) {
		
		if (search.equals("")) {
			this.pageItems = new ItemList();
		} else {
			
			this.pageItems = new ItemList(getSearchItems(search));
			
		}
		
		this.itemBank = new ItemList();
		
		this.user = user;
		this.order = new Order(this.user);
		
		setTitle("Shopping Page");
		setSize(1000,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		c=getContentPane();
		c.setLayout(null);
		
		searchBar = new JTextField();
		searchBar.setBounds(180,20,370,20);
		c.add(searchBar);
		
		searchButton = new JButton("Search");
		searchButton.setBounds(550,20,80,20);
		searchButton.addActionListener(this);
		c.add(searchButton);
		
		checkOutButton = new JButton("Go To Cart");
		checkOutButton.setBounds(680,20,110,20);
		checkOutButton.addActionListener(this);
		c.add(checkOutButton);
		
		logoutButton = new JButton("Log out");
		logoutButton.setBounds(40,20,80,20);
		logoutButton.addActionListener(this);
		c.add(logoutButton);
		
		itemNameLabels = new JLabel[itemBank.length()];
		itemQtys = new JComboBox[itemBank.length()];
		itemQtyLabels = new JLabel[itemBank.length()];
		itemPriceLabels = new JLabel[itemBank.length()];
		itemNumberLabels = new JLabel[itemBank.length()];
		addToCartButtons = new JButton[itemBank.length()];
		addedToCartLabels = new JLabel[itemBank.length()];
		
		for (int i = 0; i < pageItems.length(); i++) {
			displayItem(i, 70 + 95*i);
		}
		
		setVisible(true);
	}
	
	public void displayItem(int index, int y) {
		
		itemNameLabels[index] = itemNameLabel(pageItems.get(index), 20, y, 100, 20);
		c.add(itemNameLabels[index]);
		
		itemQtys[index] = itemQty(pageItems.get(index), 100, y + 40, 100, 20);
		c.add(itemQtys[index]);
		
		itemQtyLabels[index] = itemQtyLabel(20, y + 40, 100, 20);
		c.add(itemQtyLabels[index]);
		
		itemPriceLabels[index] = itemPriceLabel(pageItems.get(index), 140, y, 100, 20);
		c.add(itemPriceLabels[index]);
		
		itemNumberLabels[index] = itemNumberLabel(pageItems.get(index), 300, y, 100, 20);
		c.add(itemNumberLabels[index]);
		
		addToCartButtons[index] = addToCartButton(300, y + 40, 150, 20);
		addToCartButtons[index].addActionListener(this);
		c.add(addToCartButtons[index]);
		
	}
	
	public void removeItems() {
		for (int i = 0; i < itemBank.length(); i++) {
			try {
				c.remove(itemNameLabels[i]);
				itemQtys[i].removeAllItems();
				c.remove(itemQtys[i]);
				c.remove(itemQtyLabels[i]);
				c.remove(itemPriceLabels[i]);
				c.remove(itemNumberLabels[i]);
				c.remove(addToCartButtons[i]);
			} catch (Exception e) {
				return;
			}
			try {
				addedToCartLabels[i].setText("");
				c.remove(addedToCartLabels[i]);
			} catch (Exception e) {
				
			}
		}
	}
	
	public void search(String search) {
		
		ArrayList<String> searchItems = getSearchItems(search);
		
		removeItems();
		
		pageItems = new ItemList(searchItems);
		for (int i = 0; i < pageItems.length(); i++) {
			displayItem(i, 70 + 95*i);
		}
		c.repaint();
		
	}
	
	public ArrayList<String> getSearchItems(String search) {
		ArrayList<String> searchItems = new ArrayList<String>();
		int index = 0;
		while (index < search.length()-1 && search.lastIndexOf(" ") > index) {
			if (search.substring(index, index + 1).compareTo(" ") != 0) {
				searchItems.add(search.substring(index, search.substring(index).indexOf(" ") + index));
				index += search.substring(index).indexOf(" ") + 1;
			} else {
				index ++;
			}
		}
		if (!search.substring(index).equals(" ") && index < search.length()-1) {
			searchItems.add(search.substring(index));
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

	public JComboBox itemQty(Item item, int x, int y, int width, int height) {
		String[] qtys = new String[99];
		for (int i = 0; i < qtys.length; i++) {
			qtys[i] = Integer.toString(i+1);
		}
		JComboBox itemQty = new JComboBox(qtys);
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
	
	public JLabel itemPriceLabel(Item item, int x, int y, int width, int height) {
		JLabel itemPrice = new JLabel();
		itemPrice.setBounds(x, y, width, height);
		itemPrice.setFont(new Font("Arial", Font.PLAIN, 17));
		itemPrice.setText(item.getPriceCurrencyFormat());
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
	
	public JButton addToCartButton(int x, int y, int width, int height) {
		JButton button = new JButton("Add To Cart");
		button.setBounds(x,y,width,height);
		return button;
	}
	
	public JLabel addedToCartLabel(int index, int qty, Rectangle bounds) {
		
		JLabel label = new JLabel();
		label.setBounds((int) bounds.getX() + 160, (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		label.setForeground(new Color(120,120,180));
		label.setText("added x" + qty + " to cart!");
		return label;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == searchButton) {
			
			if (searchBar.getText().equals("")) {
				String search = "";
				for (int i = 0; i < itemBank.length(); i++) {
					search += itemBank.get(i).getName() + " ";		
				}
				search(search);
				return;
			}
			
			search(searchBar.getText());
			return;
		}
		
		if (e.getSource() == logoutButton) {
			dispose();
		}
		
		if (e.getSource() == checkOutButton) {
			Main.openCheckOut(user);
			dispose();
			return;
		}
		
		for (int i = 0; i < pageItems.length(); i++) {
			if (e.getSource() == addToCartButtons[i]) {
				order.addToCart(pageItems.get(i),Integer.parseInt(itemQtys[i].getSelectedItem().toString()));
				if (order.getItemQty(pageItems.get(i).getId()) > 99) {
					order.changeItemQty(pageItems.get(i).getId(), 99);
				}
				order.updateInDatabase();
				try {
					c.remove(addedToCartLabels[i]);
					String str = addedToCartLabels[i].getText();
					int itemsAdded = ((Integer.parseInt(str.substring(str.indexOf("added x") + 7, str.indexOf(" to cart!")))
							+ Integer.parseInt(itemQtys[i].getSelectedItem().toString())));
					if (itemsAdded > 99) {
						itemsAdded = 99;
					}
					
					addedToCartLabels[i].setText("added x" + itemsAdded + " to cart!"); 
				} catch (Exception err) {
					addedToCartLabels[i] = addedToCartLabel(i, Integer.parseInt(itemQtys[i].getSelectedItem().toString()), addToCartButtons[i].getBounds());;
				}
				
				c.add(addedToCartLabels[i]);
				c.repaint();
			}
		}
		
	}
	
}
