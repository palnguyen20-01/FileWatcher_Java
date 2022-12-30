/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.net.Socket;
import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTree;

/**
 *
 * @author Anh Dat
 */
public class ClientWatchThread extends Thread{
    Client curClient;
     public static FileChooserScreen fcs;
    public ClientWatchThread(Socket clientSocket){
        try {
			curClient = new Client();
			curClient.socket = clientSocket;
			OutputStream os = clientSocket.getOutputStream();
			curClient.sender = new BufferedWriter(new OutputStreamWriter(os));
			InputStream is = clientSocket.getInputStream();
			curClient.receiver = new BufferedReader(new InputStreamReader(is));
			curClient.port = clientSocket.getPort();
                        InetSocketAddress sockaddr = (InetSocketAddress)clientSocket.getRemoteSocketAddress();
                        InetAddress inaddr = sockaddr.getAddress();
                        Inet4Address in4addr = (Inet4Address)inaddr;
byte[] ip4bytes = in4addr.getAddress(); // returns byte[4]
curClient.IP = in4addr.toString();
curClient.isWatching=false;

		} catch (IOException e) {

		}
    }
    
    @Override
	public void run() {
		try {
			while (true) {
				String header = curClient.receiver.readLine();
				if (header == null)
					throw new IOException();

				System.out.println("Header: " + header);
                                    
            
				switch (header) {

				case "connect": {
					
                                    Main.connection.connectedClient.add(curClient);
                                    Main.mainScreen.updateClientTable();

                                    curClient.sender.write("connect success");
                                    curClient.sender.newLine();
                                    curClient.sender.flush();
                                
				break;
				}
                                
                                case "root files":{
                                    int length=Integer.parseInt(curClient.receiver.readLine());
                                    System.out.println(length);
                                   ArrayList<String> rootList=new ArrayList<String>();
                                    for(int i=0;i<length;i++){
                                        rootList.add(curClient.receiver.readLine());
                                        System.out.println(rootList.get(i));
                                    }       
                                    fcs.updateFileTable(rootList);
                                    break;
                                }
                                
                                       case "file choosen":{
                                    int length=Integer.parseInt(curClient.receiver.readLine());
                                   ArrayList<String> childList=new ArrayList<String>();
                                    for(int i=0;i<length;i++){
                                        childList.add(curClient.receiver.readLine());
                                        System.out.println(childList.get(i));
                                    }       
                                    fcs.updateFileTable(childList);
                                    break;
                                }
                                
                                case "respone a path":{    
                    curClient.sender.write(curClient.pathWatching);
                  curClient.sender.newLine();
                                    curClient.sender.flush();
                                    
                                    if(curClient.pathWatching!=null){
                curClient.sender.write("success send a path");
                  curClient.sender.newLine();
                                    curClient.sender.flush();
            }else{
                                curClient.sender.write("fail send a path");
                                  curClient.sender.newLine();
                                    curClient.sender.flush();
            }
            				break;
               }
                                case "begin watching":{
                                    fcs.dispose();
                                    curClient.watchScreen =new WatchScreen(curClient.IP,curClient.port,curClient.pathWatching,curClient);
            curClient.watchScreen.fileList=new ArrayList<String>();
            curClient.watchScreen.eventList=new ArrayList<String>();
            				break;

                                }
                                case "event found":{
                                    
                String file=curClient.receiver.readLine();
                String event=curClient.receiver.readLine();
                curClient.watchScreen.fileList.add(file);
                curClient.watchScreen.eventList.add(event);
                curClient.watchScreen.updateStatusTable( curClient.watchScreen.fileList, curClient.watchScreen.eventList);
                System.out.println(file+" : "+event);
                				break;

                                }
                                    


				}
			}

		} catch (IOException e) {
			if (!Main.connection.s.isClosed()) {

				try {
//					for (Client client : Main.connection.connectedClient) {
//						if (!client.userName.equals(curClient.userName)) {
//							client.sender.write("user quit");
//							client.sender.newLine();
//							client.sender.write(curClient.userName);
//							client.sender.newLine();
//							client.sender.flush();
//						}
//					}

					curClient.socket.close();

				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Main.connection.connectedClient.remove(curClient);
				Main.mainScreen.updateClientTable();
			}
		}
	}
        
        public static void requestAPathFromClient(Client thisClient){
            try{
            thisClient.sender.write("request a path");
            thisClient.sender.newLine();
            thisClient.sender.flush();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        public static void requestChoosingFromClient(Client thisClient){
            try{
            thisClient.sender.write("request choosing");
            thisClient.sender.newLine();
            thisClient.sender.flush();
            
            
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        public static void goInto(Client thisClient,String path){
            try{
            thisClient.sender.write("go into");
            thisClient.sender.newLine();
            thisClient.sender.flush();
            
            thisClient.sender.write(path);
            thisClient.sender.newLine();
            thisClient.sender.flush();
            
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
}
