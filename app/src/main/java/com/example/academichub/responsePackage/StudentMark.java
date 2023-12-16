package com.example.academichub.responsePackage;

public class StudentMark {
	
	private String cid,id;

	public StudentMark(String cid, String id) {
		super();
		this.cid = cid;
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "StudentMark [cid=" + cid + ", id=" + id + "]";
	}
	
	

}
