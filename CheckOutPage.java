import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class CheckOutPage extends JFrame implements ActionListener {
	
	ItemList pageItems, itemBank;
	Container c;
	User user;
	Order order;
	
	JTextField searchBar;
	JButton searchButton, checkOutButton, logoutButton;
	JLabel yourCartLabel;
	
	JLabel[] itemNameLabels,itemQtyLabels, itemPriceLabels, itemNumberLabels, totalPriceLabels;
	JComboBox[] itemQtys;
	JButton[] removeFromCartButtons;
	
	CheckOutPage(User user) {
		
		this.user = user;
		this.order = new Order(this.user);
		this.pageItems = new ItemList();
		for (int i = 0; i < pageItems.length(); i++) {
			if (!order.containsItem(pageItems.get(i))) {
				pageItems.remove(i);
				i -= 1;
			}
		}
		
		setTitle("Check Out Page");
		setSize(1000,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		c=getContentPane();
		c.setLayout(null);
		
		searchBar = new JTextField();
		searchBar.setBounds(180,20,370,20);
		c.add(searchBar);
		
		searchButton = new JButton("shop more");
		searchButton.setBounds(550,20,100,20);
		searchButton.addActionListener(this);
		c.add(searchButton);
		
		yourCartLabel = new JLabel("your cart:");
		yourCartLabel.setBounds(250,60,100,25);
		yourCartLabel.setFont(new Font("Times New Roman",Font.PLAIN,25));
		c.add(yourCartLabel);
		
		checkOutButton = new JButton("Check Out");
		checkOutButton.setBounds(200,500,150,40);
		checkOutButton.addActionListener(this);
		c.add(checkOutButton);
		
		logoutButton = new JButton("Log out");
		logoutButton.setBounds(40,20,80,20);
		logoutButton.addActionListener(this);
		c.add(logoutButton);
		
		itemNameLabels = new JLabel[pageItems.length()];
		itemQtys = new JComboBox[pageItems.length()];
		itemQtyLabels = new JLabel[pageItems.length()];
		itemPriceLabels = new JLabel[pageItems.length()];
		itemNumberLabels = new JLabel[pageItems.length()];
		totalPriceLabels = new JLabel[pageItems.length()];
		removeFromCartButtons = new JButton[pageItems.length()];
		
		for (int i = 0; i < pageItems.length(); i++) {
			displayItem(i, 120 + 125*i);
		}
		
		setVisible(true);
		
	}
	
	public void displayItem(int index, int y) {
		
		itemNameLabels[index] = itemNameLabel(pageItems.get(index), 20, y, 100, 20);
		c.add(itemNameLabels[index]);
		
		itemQtys[index] = itemQty(pageItems.get(index), 100, y + 40, 100, 20);
		itemQtys[index].addActionListener(this);
		c.add(itemQtys[index]);
		
		itemQtyLabels[index] = itemQtyLabel(20, y + 40, 100, 20);
		c.add(itemQtyLabels[index]);
		
		itemPriceLabels[index] = itemPriceLabel(pageItems.get(index), 140, y, 100, 20);
		c.add(itemPriceLabels[index]);
		
		itemNumberLabels[index] = itemNumberLabel(pageItems.get(index), 300, y, 100, 20);
		c.add(itemNumberLabels[index]);
		
		totalPriceLabels[index] = totalPriceLabel(index, Integer.parseInt(itemQtys[index].getSelectedItem().toString()), 300, y + 42, 250, 20);
		c.add(totalPriceLabels[index]);
		
		removeFromCartButtons[index] = removeFromCartButton(20, y + 70, 180, 20);
		removeFromCartButtons[index].addActionListener(this);
		c.add(removeFromCartButtons[index]);
		
	}
	
	public void removeItems() {
		for (int i = 0; i < pageItems.length(); i++) {
			c.remove(itemNameLabels[i]);
			c.remove(itemQtys[i]);
			c.remove(itemQtyLabels[i]);
			c.remove(itemPriceLabels[i]);
			c.remove(itemNumberLabels[i]);
			c.remove(totalPriceLabels[i]);
			c.remove(removeFromCartButtons[i]);
		}
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
		if (order.containsItem(item)) {
			itemQty.setSelectedItem(Integer.toString(order.getItemQty(item.getId())));
		}
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
	
	public JButton removeFromCartButton(int x, int y, int width, int height) {
		
		JButton remove = new JButton("Remove From Cart");
		remove.setBounds(x, y, width, height);
		return remove;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getSource() == searchButton) {
			main2.openShoppingPage(user, searchBar.getText());
			dispose();
			return;
		}

		if (e.getSource() == checkOutButton) {
			
			if (!order.isEmpty()) {
				
				double totalPrice = 0;
				int qty = order.length() - 1;
				for (int i = 0; i < pageItems.length(); i++) {
					totalPrice += Double.parseDouble(itemQtys[i].getSelectedItem().toString()) *  pageItems.get(i).getPrice();
				}
				NumberFormat formatter = NumberFormat.getCurrencyInstance();
				String checkoutText = "Your order for " + qty + " items (" + formatter.format(totalPrice) + ") was completed successfully";				
				removeItems();
				order.checkOut();
				order = new Order(user);
				user = new User(user.getId());
				while (pageItems.length() > 0) {
					pageItems.remove(0);
				}
				
				JLabel checkOutLabel = new JLabel(checkoutText);
				checkOutLabel.setBounds(175,210,700,40);
				checkOutLabel.setFont(new Font("times new roman", Font.PLAIN, 25));
				c.add(checkOutLabel);
				
			}
			
			c.repaint();
			return;
		}
		
		if (e.getSource() == logoutButton) {
			dispose();
			return;
		}
		
		for (int i = 0; i < pageItems.length(); i++) {
			if (e.getSource() == itemQtys[i]) {

				order.changeItemQty(pageItems.get(i).getId(),Integer.parseInt(itemQtys[i].getSelectedItem().toString()));
				order.updateInDatabase();
				NumberFormat formatter = NumberFormat.getCurrencyInstance();
				totalPriceLabels[i].setText("Total Price: " + formatter.format( Double.parseDouble(itemQtys[i].getSelectedItem().toString()) * pageItems.get(i).getPrice()));
				c.repaint();
				return;
				
			}
			
			if (e.getSource() == removeFromCartButtons[i]) {
				order.changeItemQty(pageItems.get(i).getId(),0);
				order.updateInDatabase();
				removeItems();
				pageItems.remove(i);
				for (int j = 0; j < pageItems.length(); j++) {
					displayItem(j, 120 + 125*j);
				}
				c.repaint();
				return;
			}
			
		}
		
	}
	
}
