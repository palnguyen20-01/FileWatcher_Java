/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Anh Dat
 */
public class Connection {
    String userName;
	Server connectedServer;
	Socket s;
	BufferedReader receiver;
	BufferedWriter sender;

	Thread receiveAndProcessThread;

//	public List<String> onlineUsers;
//	public List<Room> allRooms;

	public Connection(Server connectedServer) {
		try {
			this.connectedServer = connectedServer;
			s = new Socket(connectedServer.ip, connectedServer.port);
			InputStream is = s.getInputStream();
			receiver = new BufferedReader(new InputStreamReader(is));
			OutputStream os = s.getOutputStream();
			sender = new BufferedWriter(new OutputStreamWriter(os));

		} catch (IOException e1) {
			Main.mainScreen.loginResultAction("closed");
		}
	}
        
        public void createAndSaveFileTree(){
                    File[] roots = File.listRoots();

                                FileSystemModel file= new FileSystemModel(roots[0]);
 JFileChooser test=new JFileChooser();
 
        try {
           OutputStream os=s.getOutputStream();
                                
                                ObjectOutputStream oos=new ObjectOutputStream(os);
                                oos.writeObject(test);
                                oos.close();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
         
                }
        

	public void Connect() {
		try {
                    sender.write("connect");
			sender.newLine();
			sender.flush();
			String loginResult = receiver.readLine();
			if (loginResult.equals("connect success")) {
				Main.mainScreen.loginResultAction("success");
                                createAndSaveFileTree();
                                sender.write("succes to send file tree");
			sender.newLine();
			sender.flush();

				receiveAndProcessThread = new Thread(() -> {
					try {
						while (true) {
							String header = receiver.readLine();
							System.out.println("Header " + header);
							if (header == null)
								throw new IOException();
                                                        switch(header){
                                                            
                                                        }
						}
					} catch (IOException e) {
						JOptionPane.showMessageDialog(Main.mainScreen, "Server đã đóng, ứng dụng sẽ thoát", "Thông báo",
								JOptionPane.INFORMATION_MESSAGE);
						try {
							Main.connection.s.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.exit(0);
					}
				});
				receiveAndProcessThread.start();
			} else
				Main.mainScreen.loginResultAction("existed");

		} catch (IOException e1) {

		}
	}
        
        	public static boolean serverOnline(String ip, int port) {
		try {
			Socket s = new Socket();
			s.connect(new InetSocketAddress(ip, port), 300);
			s.close();
			return true;
		} catch (IOException ex) {
			return false;
		}
	}
                
                
                
//        public static String serverName(String ip, int port) {
//
//		if (!serverOnline(ip, port))
//			return "";
//
//		try {
//			Socket s = new Socket(ip, port);
//			InputStream is = s.getInputStream();
//			BufferedReader receiver = new BufferedReader(new InputStreamReader(is));
//			OutputStream os = s.getOutputStream();
//			BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(os));
//
//			sender.write("get name");
//			sender.newLine();
//			sender.flush();
//
//			String name = receiver.readLine();
//
//			s.close();
//			return name;
//		} catch (IOException ex) {
//			return "";
//		}
//	}
                
}
