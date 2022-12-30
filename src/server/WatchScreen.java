/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    
    ArrayList<String> fileList;
    ArrayList<String> eventList;
    public WatchScreen(String ip, int port,String filePath,Client curClient){
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
                
                this.addWindowListener(new WindowAdapter(){
                   public void windowClosed(WindowEvent windowEvent){      
                       try{
                System.out.println("Stop watching");
                curClient.isWatching=false;
                Main.mainScreen.updateClientTable();
            curClient.sender.write("stop watching");
            curClient.sender.newLine();
            curClient.sender.flush();
                   }catch(Exception ex){
                       ex.printStackTrace();
                   }}
                });
                
                this.setTitle("IP: "+ip+" Path: "+filePath);
		this.setContentPane(mainContainer);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public void updateStatusTable(ArrayList<String> file,ArrayList<String> event) {

		Object[][] tableContent = new Object[file.size()][2];
		for (int i = 0; i < file.size(); i++) {
			tableContent[i][0] = file.get(i);
			tableContent[i][1] = event.get(i);
		}

		statusTable.setModel(new DefaultTableModel(tableContent, new String[] { "File", "Thay đổi" }){

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
