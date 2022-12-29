/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Anh Dat
 */
public class WatchScreen extends JFrame implements ActionListener  {

    JLabel ipLabel;
        JLabel ipClient;
    JLabel portLabel;
    JLabel portClient;

    JLabel path;
        JLabel pathName;

    
    JTable statusTable;
    
    
    public WatchScreen(String ip, int port,String filePath){
        JPanel mainContainer=new JPanel(new BorderLayout());
        
        ipLabel=new JLabel("IP: ");
        ipClient= new JLabel(ip);
        
        portLabel=new JLabel("Port: ");
        portClient=new JLabel(Integer.toString(port));
         path=new JLabel("Path: ");
        pathName=new JLabel(filePath);

JPanel header=new JPanel(new GridLayout(3,2));
header.add(ipLabel);header.add(ipClient);
header.add(portLabel);
header.add(portClient);
header.add(path);
header.add(pathName);
mainContainer.add(header,BorderLayout.NORTH);


statusTable=new JTable(new Object[][]{}, new String[]{"File","Thông báo"});
  statusTable.setRowHeight(25);
        JScrollPane listScrollPane = new JScrollPane(statusTable);
		listScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách các thay đổi trên file"));
    
                mainContainer.add(listScrollPane,BorderLayout.CENTER);
                this.setTitle("Server chat");
		this.setContentPane(mainContainer);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
