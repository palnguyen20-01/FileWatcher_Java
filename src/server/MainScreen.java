/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Anh Dat
 */
public class MainScreen extends JFrame implements ActionListener{
    
    JLabel portLabel;
    JTextField portText;
	JLabel serverNameLabel;
	JTextField serverNameText;
                
                JTable clientTable;
                JButton openCloseButton;
                boolean isConnectionOpen=false;
                
                public MainScreen(){
                    JPanel mainContainer = new JPanel( new BorderLayout());
                    
                    JLabel ipLabel = new JLabel("IP: "+ Connection.getThisIP());
                    
                    portLabel= new JLabel("Port: ");
                    portText= new JTextField();
                    portText.setPreferredSize(new Dimension(150, 20));
                    
//                    serverNameLabel = new JLabel("Tên server: ");
//                    serverNameText = new JTextField();
//                                        serverNameText.setPreferredSize(new Dimension(150, 20));

                    openCloseButton= new JButton("Mở server");
                    openCloseButton.addActionListener(this);
                    
                    clientTable=new JTable(new Object[][]{}, new String[]{"IP","Port","Trạng thái"});
                    clientTable.setRowHeight(25);
                    clientTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
                                if (column == 2) {
					c.setForeground(value.toString().equals("Đang theo dõi") ? Color.green : Color.red);
					c.setFont(new Font("Dialog", Font.BOLD, 13));
				} else
					c.setForeground(Color.black);

			
				return c;
			}
		});
		JScrollPane clientScrollPane = new JScrollPane(clientTable);
		clientScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách client đang kết nối"));
                
                clientTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
			String serverState = clientTable.getValueAt(clientTable.getSelectedRow(), 2).toString();
			if (serverState.equals("Đang theo dõi")) {
				JOptionPane.showMessageDialog(Main.mainScreen, "Đã theo dõi client này", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
			}else{
				String selectedIP = clientTable.getValueAt(clientTable.getSelectedRow(), 0).toString();
				int selectedPort = Integer
								.parseInt(clientTable.getValueAt(clientTable.getSelectedRow(), 1).toString());
                                Client selectedClient = Main.connection.connectedClient.stream()
								.filter(x -> x.IP.equals(selectedIP) && x.port == selectedPort).findAny().orElse(null);
selectedClient.isWatching=true;
updateClientTable();
ClientWatchThread.requestChoosingFromClient(selectedClient);
//						ClientWatchThread.requestAPathFromClient(selectedClient);                                                
				}}
			}
		});

                
                JPanel first=new JPanel(new FlowLayout(FlowLayout.CENTER, 30,20));
                JPanel leftFirst= new JPanel(new FlowLayout());
                                JPanel rightFirst= new JPanel(new FlowLayout());

                                JPanel content=new JPanel(new FlowLayout());
                JPanel third=new JPanel(new FlowLayout());
                
leftFirst.add(ipLabel);
                rightFirst.add(portLabel);
rightFirst.add(portText);

first.add(leftFirst);
first.add(rightFirst);

//first.add(serverNameLabel);
//first.add(serverNameText);

mainContainer.add(first,BorderLayout.NORTH);
content.add(clientScrollPane);
mainContainer.add(content,BorderLayout.CENTER);

third.add(openCloseButton);
mainContainer.add(third,BorderLayout.SOUTH);



this.setTitle("Server chat");
		this.setContentPane(mainContainer);
		this.getRootPane().setDefaultButton(openCloseButton);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
                Main.connection=new Connection();
                }

    @Override
    public void actionPerformed(ActionEvent e) {
if (!isConnectionOpen) {
			try {
				if (portText.getText().isEmpty())
					JOptionPane.showMessageDialog(this, "Port không được trống", "Lỗi", JOptionPane.WARNING_MESSAGE);
				else {

					Main.connection.serverPort = Integer.parseInt(portText.getText());

					Main.connection.OpenSocket(Main.connection.serverPort);
					isConnectionOpen = true;
					openCloseButton.setText("Đóng server");
				}

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Port phải là 1 số nguyên dương", "Lỗi",
						JOptionPane.WARNING_MESSAGE);
			}
		} else {
			Main.connection.CloseSocket();
			isConnectionOpen = false;
			openCloseButton.setText("Mở server");
		}
    }
    public void updateClientTable() {

		Object[][] tableContent = new Object[Main.connection.connectedClient.size()][3];
		for (int i = 0; i < Main.connection.connectedClient.size(); i++) {
			tableContent[i][0] = Main.connection.connectedClient.get(i).IP;
			tableContent[i][1] = Main.connection.connectedClient.get(i).port;
                        			tableContent[i][2] = Main.connection.connectedClient.get(i).isWatching?"Đang theo dõi":"Chưa theo dõi";

		}

		clientTable.setModel(new DefaultTableModel(tableContent, new String[] { "IP client", "Port client","Trạng thái" }){

			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return String.class;
			}

		});
	}
}
