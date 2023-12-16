package com.example.academichub.responsePackage;

public class UserIDType {
	private String id,type;

	public UserIDType(String id, String type) {
		super();
		this.id = id;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "UserIDType [id=" + id + ", type=" + type + "]";
	}
	
	
}
