/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Anh Dat
 */
public class FileChooserScreen extends JFrame implements ActionListener{
 JLabel ipLabel;
        JLabel ipClient;
    JLabel portLabel;
    JLabel portClient;

    JButton goIntoBtn;
    JButton okBtn;
        JButton backBtn;

    JTable fileTable;
    
    ArrayList<String> curFile;
    ArrayList<String> parent;
    Client curClient;
    public FileChooserScreen(Client curClient){
        parent=new ArrayList<String>();
        String ip=curClient.IP;
        int port=curClient.port;
                
        JPanel mainContainer=new JPanel(new BorderLayout());
        this.curClient=curClient;
        ipLabel=new JLabel("IP: ");
        ipClient= new JLabel(ip);
        
        portLabel=new JLabel("Port: ");
        portClient=new JLabel(Integer.toString(port));

        
JPanel header=new JPanel(new GridLayout(2,2));
header.add(ipLabel);
header.add(ipClient);
header.add(portLabel);
header.add(portClient);

mainContainer.add(header,BorderLayout.NORTH);


fileTable=new JTable(new Object[][]{}, new String[]{"Đường dẫn"});
  fileTable.setRowHeight(25);
  fileTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					goIntoBtn.doClick();
				}
			}
		});
        JScrollPane listScrollPane = new JScrollPane(fileTable);
		listScrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách các file"));
    
                mainContainer.add(listScrollPane,BorderLayout.CENTER);
                
                JPanel footer=new JPanel(new FlowLayout());
                goIntoBtn =new JButton("Go into");
                goIntoBtn.addActionListener(this);
		goIntoBtn.setActionCommand("go into");
                             
                
                okBtn=new JButton("Ok");
                okBtn.addActionListener(this);
		okBtn.setActionCommand("ok");
                
                backBtn=new JButton("Back");
                backBtn.addActionListener(this);
		backBtn.setActionCommand("back");
                                            backBtn.setEnabled(false);

                footer.add(goIntoBtn);
                                footer.add(okBtn);
                footer.add(backBtn);

                            backBtn.setEnabled(false);
                                mainContainer.add(footer,BorderLayout.SOUTH);

                
                
                this.setTitle("IP: "+ip+" Port: "+port);
		this.setContentPane(mainContainer);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
       switch (e.getActionCommand()) {
		case "go into": {
			if (fileTable.getSelectedRow() == -1)
				break;
			String filePath = fileTable.getValueAt(fileTable.getSelectedRow(), 0).toString();
			
                        if(!this.parent.contains(filePath))
                        this.parent.add(filePath);
                        
                        ClientWatchThread.goInto(curClient,filePath);
			
                        if(parent.isEmpty()){
                            backBtn.setEnabled(false);
                        }else
                            backBtn.setEnabled(true);
                        break;
                        
		}
                case "ok": {
			if (fileTable.getSelectedRow() == -1)
				break;
			String filePath = fileTable.getValueAt(fileTable.getSelectedRow(), 0).toString();
			curClient.pathWatching=filePath;
                       ClientWatchThread.requestAPathFromClient(curClient);
			break;
		}
                case "back": {
			if(!backBtn.isEnabled() || parent.size()==1){
                        ClientWatchThread.requestChoosingFromClient(curClient);
                        backBtn.setEnabled(false);
                        }
                        else {
                            
                            String filePath = this.parent.get(this.parent.size()-2);
                        this.parent.remove(this.parent.size()-1);
                        
                        ClientWatchThread.goInto(curClient,filePath);
                        }
			
		}
                
		}
    }
    public void updateFileTable(ArrayList<String> file) {

		Object[][] tableContent = new Object[file.size()][1];
		for (int i = 0; i < file.size(); i++) {
			tableContent[i][0] = file.get(i);
		}

		fileTable.setModel(new DefaultTableModel(tableContent, new String[] { "File" }){

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
