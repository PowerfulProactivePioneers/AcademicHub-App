package com.example.academichub.responsePackage;

public class StudentClassRoomDB {
	private String id,cid;

	public StudentClassRoomDB() {
		super();
	}

	public StudentClassRoomDB(String id, String cid) {
		super();
		this.id = id;
		this.cid = cid;
	}

	@Override
	public String toString() {
		return "StudentClassRoomDB [id=" + id + ", cid=" + cid + "]";
	}

	public String getId() {
		return id;
	}

	public String getCid() {
		return cid;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
	
	
}
