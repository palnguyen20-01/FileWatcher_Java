/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Anh Dat
 */
public class MainScreen extends JFrame implements ActionListener {
    	public Server connectedServer;
	JTable serverTable;
	List<Server> serverList;
        
        public MainScreen(){
            JPanel mainContent=new JPanel(new BorderLayout());
            JButton refreshButton = new JButton("Làm mới");
		refreshButton.setActionCommand("refresh");
		refreshButton.addActionListener(this);

		serverTable = new JTable();
		serverTable.setRowHeight(25);
		serverTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(column==2){
                                    c.setForeground((value.toString().equals("Hoạt động")||value.toString().equals("Đang theo dõi")) ? Color.green : Color.red);
					c.setFont(new Font("Dialog", Font.BOLD, 13));
                                }else if(column==3){
                                     c.setForeground((value.toString().equals("Đang theo dõi")||value.toString().equals("Đang theo dõi")) ? Color.green : Color.red);
					c.setFont(new Font("Dialog", Font.BOLD, 13));
                                }else
					c.setForeground(Color.black);

                                
                                
				return c;
			}
		});
                
		updateServerTable();

		JScrollPane serverScrollPane = new JScrollPane(serverTable);
		serverScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách server để kết nối"));

		JButton joinButton = new JButton("Kết nối server");
		joinButton.addActionListener(this);
		joinButton.setActionCommand("join");
		serverTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					joinButton.doClick();
				}
			}
		});

		JButton addButton = new JButton("Thêm");
		addButton.addActionListener(this);
		addButton.setActionCommand("add");

//		JButton deleteButton = new JButton("Xoá");
//		deleteButton.addActionListener(this);
//		deleteButton.setActionCommand("delete");
//
//		JButton editButton = new JButton("Sửa");
//		editButton.addActionListener(this);
//		editButton.setActionCommand("edit");

		mainContent.add(serverScrollPane,BorderLayout.CENTER);
		JPanel joinRefreshPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		joinRefreshPanel.add(joinButton);
		joinRefreshPanel.add(refreshButton);

                JPanel edit=new JPanel(new FlowLayout(FlowLayout.CENTER,20,25));
		edit.add(addButton );
//		edit.add(deleteButton);
//		edit.add(editButton);
                
                JPanel footer=new JPanel(new BorderLayout());
                footer.add(joinRefreshPanel,BorderLayout.NORTH);
                footer.add(edit,BorderLayout.CENTER);
                
                mainContent.add(footer,BorderLayout.SOUTH);

		this.setTitle("Ứng dụng chat");
		this.setContentPane(mainContent);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
        }

        JTextField nameText;
        
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
		case "join": {
			if (serverTable.getSelectedRow() == -1)
				break;
			String serverState = serverTable.getValueAt(serverTable.getSelectedRow(), 2).toString();
			if (serverState.equals("Không hoạt động")) {
				JOptionPane.showMessageDialog(this, "Server không hoạt động", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
				break;
			}else{
			 
						String selectedIP = serverTable.getValueAt(serverTable.getSelectedRow(), 0).toString();
						int selectedPort = Integer
								.parseInt(serverTable.getValueAt(serverTable.getSelectedRow(), 1).toString());
						Server selectedServer = serverList.stream()
								.filter(x -> x.ip.equals(selectedIP) && x.port == selectedPort).findAny().orElse(null);

						Main.connection = new Connection( selectedServer);
						Main.connection.Connect();
                }
			

			break;
		}
		case "add": {
			JDialog addServerDialog = new JDialog();


			JLabel ipLabel = new JLabel("IP");
			JLabel portLabel = new JLabel("Port");
			JTextField ipText = new JTextField();
			JTextField portText = new JTextField();
			JButton addServerButton = new JButton("Thêm");
			addServerButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						int port = Integer.parseInt(portText.getText());
						String ip = ipText.getText();

						if (serverList == null)
							serverList = new ArrayList<Server>();
						serverList.add(new Server( ip, port, false));

						updateServerTable();

						addServerDialog.setVisible(false);
						addServerDialog.dispose();
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(addServerDialog, "Port phải là 1 số nguyên dương", "Thông báo",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});

			JPanel addServerContent = new JPanel(new GridLayout(3,2));
			 addServerContent.setPreferredSize(new Dimension(200, 50));
			addServerContent.add(ipLabel);
			addServerContent.add(ipText);
			addServerContent.add(portLabel);
			addServerContent.add(portText);
			addServerContent.add(addServerButton);

			addServerDialog.setContentPane(addServerContent);
			addServerDialog.setTitle("Nhập port của server");
			addServerDialog.getRootPane().setDefaultButton(addServerButton);
			addServerDialog.setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
			addServerDialog.pack();
			addServerDialog.setLocationRelativeTo(null);
			addServerDialog.setVisible(true);

			break;
		}

		case "refresh": {
			updateServerTable();
			break;
		}
		}
    }
    public void loginResultAction(String result) {
		if (result.equals("success")) {

			JOptionPane.showMessageDialog(this, "Kết nối thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

		} else if (result.equals("existed"))
			JOptionPane.showMessageDialog(this, "Username đã tồn tại", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		else if (result.equals("closed"))
			JOptionPane.showMessageDialog(this, "Server đã đóng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

	}
public Object[][] getServerObjectMatrix(List<Server> serverList) {
		if (serverList == null)
			return new Object[][] {};
		Object[][] serverObjMatrix = new Object[serverList.size()][4];
		for (int i = 0; i < serverList.size(); i++) {
			serverObjMatrix[i][0] = serverList.get(i).ip;
			serverObjMatrix[i][1] = serverList.get(i).port;
			serverObjMatrix[i][2] = serverList.get(i).isOpen ? "Hoạt động" : "Không hoạt động";
                        serverObjMatrix[i][3] = serverList.get(i).isWatching ? "Đang theo dõi" : "Chưa theo dõi";
		}
		return serverObjMatrix;
	}
	public void updateServerTable() {
		if (serverList == null)
			return;
		for (Server serverData : serverList) {
			serverData.isOpen = Connection.serverOnline(serverData.ip, serverData.port);
			if (serverData.isOpen) {
			}
		}
                
         

		serverTable.setModel(new DefaultTableModel(getServerObjectMatrix(serverList), new String[] {
				"IP server", "Port server", "Trạng thái hoạt động","Theo dõi"}) {

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
