/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
/**
 *
 * @author Anh Dat
 */
public class ClientWatchThread extends Thread{
    Client curClient;
    public ClientWatchThread(Socket clientSocket){
        try {
			curClient = new Client();
			curClient.socket = clientSocket;
			OutputStream os = clientSocket.getOutputStream();
			curClient.sender = new BufferedWriter(new OutputStreamWriter(os));
			InputStream is = clientSocket.getInputStream();
			curClient.receiver = new BufferedReader(new InputStreamReader(is));
			curClient.port = clientSocket.getPort();
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
                                        
                                        JFileChooser test;
                                    try {
                                          InputStream is=curClient.socket.getInputStream();
                                        ObjectInputStream ois=new ObjectInputStream(is);
                                        test= (JFileChooser) ois.readObject();
                                                                                ois.close();
                                                                                Main.watchScreen=new WatchScreen();
                                        Main.watchScreen.add(test);

                                    } catch (Exception ex) {
ex.printStackTrace();
                                    }
                                    				String temp = curClient.receiver.readLine();
                                    System.out.println(temp);
                                    
				break;
				}
                                
//
//				case "get name": {
//					curClient.sender.write(Main.connection.serverName);
//					curClient.sender.newLine();
//					curClient.sender.flush();
//					break;
//				}
//
//				case "get connected count": {
//					curClient.sender.write("" + Main.connection.connectedClient.size());
//					curClient.sender.newLine();
//					curClient.sender.flush();
//					break;
//				}
//
//				case "request create room": {
//					String roomName = curClient.receiver.readLine();
//					String roomType = curClient.receiver.readLine();
//					int userCount = Integer.parseInt(curClient.receiver.readLine());
//					List<String> users = new ArrayList<String>();
//					for (int i = 0; i < userCount; i++)
//						users.add(curClient.receiver.readLine());
//
//					Room newRoom = new Room(roomName, users);
//					Main.connection.allRooms.add(newRoom);
//
//					for (int i = 0; i < userCount; i++) {
//						BufferedWriter currentClientSender = Client.findClient(Main.connection.connectedClient,
//								users.get(i)).sender;
//						currentClientSender.write("new room");
//						currentClientSender.newLine();
//						currentClientSender.write("" + newRoom.id);
//						currentClientSender.newLine();
//						currentClientSender.write(curClient.userName);
//						currentClientSender.newLine();
//						if (roomType.equals("private")) {
//							// private chat thì tên room của mỗi người sẽ là tên của người kia
//							currentClientSender.write(users.get(1 - i)); // user 0 thì gửi 1, user 1 thì gửi 0
//							currentClientSender.newLine();
//						} else {
//							currentClientSender.write(roomName);
//							currentClientSender.newLine();
//						}
//						currentClientSender.write(roomType);
//						currentClientSender.newLine();
//						currentClientSender.write("" + users.size());
//						currentClientSender.newLine();
//						for (String userr : users) {
//							currentClientSender.write(userr);
//							currentClientSender.newLine();
//						}
//						currentClientSender.flush();
//					}
//					break;
//				}
//
//				case "text to room": {
//					int roomID = Integer.parseInt(curClient.receiver.readLine());
//					String content = "";
//					char c;
//					do {
//						c = (char) curClient.receiver.read();
//						if (c != '\0')
//							content += c;
//					} while (c != '\0');
//
//					Room room = Room.findRoom(Main.connection.allRooms, roomID);
//					for (String user : room.users) {
//						System.out.println("Send text from " + curClient.userName + " to " + user);
//						Client currentClient = Client.findClient(Main.connection.connectedClient, user);
//						if (currentClient != null) {
//							currentClient.sender.write("text from user to room");
//							currentClient.sender.newLine();
//							currentClient.sender.write(curClient.userName);
//							currentClient.sender.newLine();
//							currentClient.sender.write("" + roomID);
//							currentClient.sender.newLine();
//							currentClient.sender.write(content);
//							currentClient.sender.write('\0');
//							currentClient.sender.flush();
//						}
//					}
//					break;
//				}
//
//				case "file to room": {
//					int roomID = Integer.parseInt(curClient.receiver.readLine());
//					int roomMessagesCount = Integer.parseInt(curClient.receiver.readLine());
//					String fileName = curClient.receiver.readLine();
//					int fileSize = Integer.parseInt(curClient.receiver.readLine());
//
//					File filesFolder = new File("files");
//					if (!filesFolder.exists())
//						filesFolder.mkdir();
//
//					int dotIndex = fileName.lastIndexOf('.');
//					String saveFileName = "files/" + fileName.substring(0, dotIndex)
//							+ String.format("%02d%03d", roomID, roomMessagesCount) + fileName.substring(dotIndex);
//
//					File file = new File(saveFileName);
//					byte[] buffer = new byte[1024];
//					InputStream in = curClient.socket.getInputStream();
//					OutputStream out = new FileOutputStream(file);
//
//					int receivedSize = 0;
//					int count;
//					while ((count = in.read(buffer)) > 0) {
//						out.write(buffer, 0, count);
//						receivedSize += count;
//						if (receivedSize >= fileSize)
//							break;
//					}
//
//					out.close();
//
//					Room room = Room.findRoom(Main.connection.allRooms, roomID);
//					for (String user : room.users) {
//						Client currentClient = Client.findClient(Main.connection.connectedClient, user);
//						if (currentClient != null) {
//							currentClient.sender.write("file from user to room");
//							currentClient.sender.newLine();
//							currentClient.sender.write(curClient.userName);
//							currentClient.sender.newLine();
//							currentClient.sender.write("" + roomID);
//							currentClient.sender.newLine();
//							currentClient.sender.write(fileName);
//							currentClient.sender.newLine();
//							currentClient.sender.flush();
//						}
//					}
//					break;
//				}
//
//				case "audio to room": {
//					int roomID = Integer.parseInt(curClient.receiver.readLine());
//					int roomMessagesCount = Integer.parseInt(curClient.receiver.readLine());
//					int audioDuration = Integer.parseInt(curClient.receiver.readLine());
//					int audioByteSize = Integer.parseInt(curClient.receiver.readLine());
//
//					File filesFolder = new File("files");
//					if (!filesFolder.exists())
//						filesFolder.mkdir();
//
//					String audioFileName = "files/audio" + String.format("%02d%03d", roomID, roomMessagesCount);
//
//					File file = new File(audioFileName);
//					byte[] buffer = new byte[1024];
//					InputStream in = curClient.socket.getInputStream();
//					OutputStream out = new FileOutputStream(file);
//
//					int receivedSize = 0;
//					int count;
//					while ((count = in.read(buffer)) > 0) {
//						out.write(buffer, 0, count);
//						receivedSize += count;
//						if (receivedSize >= audioByteSize)
//							break;
//					}
//
//					out.close();
//
//					Room room = Room.findRoom(Main.connection.allRooms, roomID);
//					for (String user : room.users) {
//						Client currentClient = Client.findClient(Main.connection.connectedClient, user);
//						if (currentClient != null) {
//							currentClient.sender.write("audio from user to room");
//							currentClient.sender.newLine();
//							currentClient.sender.write(curClient.userName);
//							currentClient.sender.newLine();
//							currentClient.sender.write("" + roomID);
//							currentClient.sender.newLine();
//							currentClient.sender.write("" + audioDuration);
//							currentClient.sender.newLine();
//							currentClient.sender.flush();
//						}
//					}
//					break;
//				}
//
//				case "request download file": {
//					try {
//						int roomID = Integer.parseInt(curClient.receiver.readLine());
//						int messageIndex = Integer.parseInt(curClient.receiver.readLine());
//						String fileName = curClient.receiver.readLine();
//
//						int dotIndex = fileName.lastIndexOf('.');
//						fileName = "files/" + fileName.substring(0, dotIndex)
//								+ String.format("%02d%03d", roomID, messageIndex) + fileName.substring(dotIndex);
//						File file = new File(fileName);
//
//						curClient.sender.write("response download file");
//						curClient.sender.newLine();
//						curClient.sender.write("" + file.length());
//						curClient.sender.newLine();
//						curClient.sender.flush();
//
//						byte[] buffer = new byte[1024];
//						InputStream in = new FileInputStream(file);
//						OutputStream out = curClient.socket.getOutputStream();
//
//						int count;
//						while ((count = in.read(buffer)) > 0) {
//							out.write(buffer, 0, count);
//						}
//
//						in.close();
//						out.flush();
//					} catch (IOException ex) {
//						ex.printStackTrace();
//					}
//					break;
//				}
//
//				case "request audio bytes": {
//					try {
//						int roomID = Integer.parseInt(curClient.receiver.readLine());
//						int messageIndex = Integer.parseInt(curClient.receiver.readLine());
//
//						String audioFileName = "files/audio" + String.format("%02d%03d", roomID, messageIndex);
//						File file = new File(audioFileName);
//
//						curClient.sender.write("response audio bytes");
//						curClient.sender.newLine();
//						curClient.sender.write("" + file.length());
//						curClient.sender.newLine();
//						curClient.sender.flush();
//
//						byte[] buffer = new byte[1024];
//						InputStream in = new FileInputStream(file);
//						OutputStream out = curClient.socket.getOutputStream();
//
//						int count;
//						while ((count = in.read(buffer)) > 0) {
//							out.write(buffer, 0, count);
//						}
//
//						in.close();
//						out.flush();
//					} catch (IOException ex) {
//						ex.printStackTrace();
//					}
//					break;
//				}

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
}
