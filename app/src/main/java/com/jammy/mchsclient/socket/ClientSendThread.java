package com.jammy.mchsclient.socket;

import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientSendThread{
	public static final String TAG = "ClientSendThread";
	private Socket socket = null;
	private ObjectOutputStream oos = null;  
	public ClientSendThread(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void send(Object obj){
		Log.i(TAG, "send"+obj);
		try {
			oos.writeObject(obj);
			oos.flush();
			Log.i(TAG, "send: "+obj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
