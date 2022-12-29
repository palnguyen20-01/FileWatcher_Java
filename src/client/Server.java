/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

/**
 *
 * @author Anh Dat
 */
public class Server {
	public String ip;
	public int port;
	public boolean isOpen;

	public Server(String ip, int port) {
		this.ip = ip;
		this.port = port;
		this.isOpen = false;
	}

	public Server(String ip, int port, boolean isOpen) {
		this.ip = ip;
		this.port = port;
		this.isOpen = isOpen;
	}
}
