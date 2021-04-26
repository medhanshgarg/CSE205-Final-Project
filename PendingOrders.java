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

public class PendingOrders extends JFrame implements ActionListener {
	
	ArrayList<Integer> orderList = new ArrayList<Integer>(), orderBank = new ArrayList<Integer>();
	ItemList itemBank;
	Container c;
	
	JButton addOrderButton, logoutButton, itemModificationButton;
	JLabel editOrdersLabel;
	
	JLabel[] orderNameLabels,orderQtyLabels, orderPriceLabels, totalPriceLabels;
	JButton[] deleteOrderButtons, approveOrderButtons;
	
	PendingOrders() {
		this(new ItemList());
	}
	PendingOrders(ItemList itemBank) {
		
		this.itemBank = itemBank;
		
		PostgresConnection db = new PostgresConnection();
		this.orderBank = db.getAllPrevOrders();
		for (int i = 0; i < 4 && i < orderBank.size(); i++) {
			orderList.add(orderBank.get(i));
		}
		db.close();
		
		setTitle("Pending Orders");
		setSize(1000,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		c=getContentPane();
		c.setLayout(null);
		
		editOrdersLabel = new JLabel("edit orders:");
		editOrdersLabel.setBounds(250,60,150,25);
		editOrdersLabel.setFont(new Font("Times New Roman",Font.PLAIN,25));
		c.add(editOrdersLabel);
		
		logoutButton = new JButton("Log out");
		logoutButton.setBounds(40,20,80,20);
		logoutButton.addActionListener(this);
		c.add(logoutButton);
		
		itemModificationButton = new JButton("Modify Items");
		itemModificationButton.setBounds(500,70,150,30);
		itemModificationButton.addActionListener(this);
		c.add(itemModificationButton);
		
		
		orderNameLabels = new JLabel[5];
		orderQtyLabels = new JLabel[5];
		orderPriceLabels = new JLabel[5];
		
		totalPriceLabels = new JLabel[5];
		
		deleteOrderButtons = new JButton[5];
		approveOrderButtons = new JButton[5];
		
		
		for (int i = 0; i < orderList.size(); i++) {
			displayOrder(i, 120 + 125*i);
		}
		
		
		setVisible(true);
		
	}
	
	public void displayOrder(int orderIndex, int y) {
		
		orderNameLabels[orderIndex] = orderNameLabel(orderList.get(orderIndex), 20, y, 200, 20);
		c.add(orderNameLabels[orderIndex]);
		
		orderQtyLabels[orderIndex] = orderQtyLabel(orderList.get(orderIndex), 20, y + 40, 100, 20);
		c.add(orderQtyLabels[orderIndex]);
		
		orderPriceLabels[orderIndex] = orderPriceLabel(orderList.get(orderIndex), 140, y + 40, 140, 20);
		c.add(orderPriceLabels[orderIndex]);
		
		deleteOrderButtons[orderIndex] = deleteOrderButton(300, y + 40, 90, 20);
		c.add(deleteOrderButtons[orderIndex]);
		
		approveOrderButtons[orderIndex] = approveOrderButton(300, y, 90, 20);
		c.add(approveOrderButtons[orderIndex]);		
		
	}
	
	public void removeOrders() {
		for (int i = 0; i < ordersOnPage(); i++) {
			c.remove(orderNameLabels[i]);
			c.remove(orderQtyLabels[i]);
			c.remove(orderPriceLabels[i]);
			c.remove(deleteOrderButtons[i]);
			c.remove(approveOrderButtons[i]);
		}
	}
	
	public int ordersOnPage() {
		if (orderBank.size() > 4) {
			return 4;
		}
		return orderBank.size();
	}
	
	public JLabel orderNameLabel(int orderId, int x, int y, int width, int height) {
		JLabel orderLabel = new JLabel();
		orderLabel.setText("Order #" + orderId);
		orderLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		orderLabel.setBounds(x,y,width,height);
		return orderLabel;
	}
	
	
	public JLabel orderQtyLabel(int orderId, int x, int y, int width, int height) {
		JLabel orderQty = new JLabel();
		orderQty.setBounds(x, y, width, height);
		orderQty.setFont(new Font("Arial", Font.PLAIN, 15));
		PostgresConnection db = new PostgresConnection();
		orderQty.setText("Quantity:" + (new Order (db.orderToString(orderId)).length() - 1));
		return orderQty;
	}
	
	public JButton approveOrderButton(int x, int y, int width, int height) {
		JButton approveOrderButton = new JButton("approve");
		approveOrderButton.setBounds(x,y,width,height);
		approveOrderButton.addActionListener(this);
		return approveOrderButton;
	}
	
	public JButton deleteOrderButton(int x, int y, int width, int height) {
		JButton deleteOrderButton = new JButton("delete");
		deleteOrderButton.setBounds(x,y,width,height);
		deleteOrderButton.addActionListener(this);
		return deleteOrderButton;
	}
	
	public JLabel orderPriceLabel(int orderId, int x, int y, int width, int height) {
		JLabel orderPrice = new JLabel();
		orderPrice.setBounds(x, y, width, height);
		orderPrice.setFont(new Font("Arial", Font.PLAIN, 16));
		double price = 0;
		PostgresConnection db = new PostgresConnection();		
		Order order = new Order(db.orderToString(orderId));
		db.close();
		
		for (int i = 0; i < order.length(); i++) {
			try {
				price += itemBank.getItem(order.get(i)).getPrice();
			} catch (Exception e) {
				// item is not saved in itemlist
				price += 7;
			}
		}
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		orderPrice.setText(formatter.format(price));
		return orderPrice;
	}
	
	public void repaintContainer() {
		removeOrders();
		for (int i = 0; i < ordersOnPage(); i++) {
			displayOrder(i, 120 + 125*i);
		}
		c.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() == logoutButton) {
			dispose();
			return;
		}
		
		if (e.getSource() == itemModificationButton) {
			dispose();
			Main.openItemModification(User.EMPLOYEE_POSITION, itemBank);
			return;
		}
		
		for (int i = 0; i < ordersOnPage(); i++) {
			
			if (e.getSource() == deleteOrderButtons[i]) {
				removeOrders();
				PostgresConnection db = new PostgresConnection();
				db.deleteOrder(orderBank.get(i));
				db.close();
				orderBank.remove(i);
				if (orderBank.size() > 3) {
					orderList.add(orderBank.get(4));
				}
				repaintContainer();
				return;
			}
			
			if (e.getSource() == approveOrderButtons[i]) {
				removeOrders();
				PostgresConnection db = new PostgresConnection();
				db.deleteOrder(orderBank.get(i));
				db.close();
				orderBank.remove(i);
				if (orderBank.size() > 3) {
					orderList.add(orderBank.get(4));
				}
				repaintContainer();
				return;
			}
			
		}
		
	}
	
}
