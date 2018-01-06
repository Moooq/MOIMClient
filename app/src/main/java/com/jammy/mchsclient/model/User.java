package com.jammy.mchsclient.model;

import java.io.Serializable;

public class User implements Serializable{
	private String username;
	private String password;
	public User(String name,String pwd){
		this.username = name;
		this.password = pwd;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}