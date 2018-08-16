package com.jammy.mchsclient.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

import com.jammy.mchsclient.url.API;

public class NetConnect {
	private static final String IPAddress = API.IP;
	private static final int Port = 8081;
	private boolean isConnected = false;
	private Socket clientSocket = null;
	public NetConnect() {
		// TODO Auto-generated constructor stub
	}
	public void startConnect(){
		if(clientSocket == null){
			try {
				clientSocket = new Socket(IPAddress, Port);
				// clientSocket.connect(new
				// InetSocketAddress(IPAddress,Port),3000);
				this.isConnected = clientSocket.isConnected();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public Socket getInstance(){
		Log.i("NetConnect", "getInstance");
		return this.clientSocket;
	}
	public boolean isConnected(){
		return this.isConnected;
	}
}
