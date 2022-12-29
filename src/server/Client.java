/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author Anh Dat
 */
public class Client {
    public String IP;
	public int port;
	public Socket socket;
	public BufferedReader receiver;
	public BufferedWriter sender;

	public Client(String IP, int port, Socket socket, BufferedReader receiver, BufferedWriter sender) {
		this.IP = IP;
		this.port = port;
		this.socket = socket;
		this.receiver = receiver;
		this.sender = sender;
	}

	public Client() {
	}

	public static Client findClient(List<Client> clientList, String IP) {
		for (Client client : clientList) {
			if (client.IP.equals(IP))
				return client;
		}
		return null;
	}
}
