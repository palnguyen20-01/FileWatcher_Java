/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
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
                    
                    clientTable=new JTable(new Object[][]{}, new String[]{"IP","Port"});
                    clientTable.setRowHeight(25);
		JScrollPane clientScrollPane = new JScrollPane(clientTable);
		clientScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách client đang kết nối"));
                
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
//mainContainer.add(third,BorderLayout.SOUTH);


JFileChooser fc=new JFileChooser();
fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
mainContainer.add(fc);
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

		Object[][] tableContent = new Object[Main.connection.connectedClient.size()][2];
		for (int i = 0; i < Main.connection.connectedClient.size(); i++) {
			tableContent[i][0] = Main.connection.connectedClient.get(i).IP;
			tableContent[i][1] = Main.connection.connectedClient.get(i).port;
		}

		clientTable.setModel(new DefaultTableModel(tableContent, new String[] { "IP client", "Port client" }));
	}
}