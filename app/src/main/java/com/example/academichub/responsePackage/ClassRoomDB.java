package com.example.academichub.responsePackage;

public class ClassRoomDB {
	private String cid,cname,ccode,fac_id,allowed_dept;
	private char allowed_section;
	
	
	public ClassRoomDB() {
		super();
	}

	public ClassRoomDB(String cid, String cname, String ccode, String fac_id, String allowed_dept,
			char allowed_section) {
		super();
		this.cid = cid;
		this.cname = cname;
		this.ccode = ccode;
		this.fac_id = fac_id;
		this.allowed_dept = allowed_dept;
		this.allowed_section = allowed_section;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public String getFac_id() {
		return fac_id;
	}

	public void setFac_id(String fac_id) {
		this.fac_id = fac_id;
	}

	public String getAllowed_dept() {
		return allowed_dept;
	}

	public void setAllowed_dept(String allowed_dept) {
		this.allowed_dept = allowed_dept;
	}

	public char getAllowed_section() {
		return allowed_section;
	}

	public void setAllowed_section(char allowed_section) {
		this.allowed_section = allowed_section;
	}

	@Override
	public String toString() {
		return "ClassRoomDB [cid=" + cid + ", cname=" + cname + ", ccode=" + ccode + ", fac_id=" + fac_id
				+ ", allowed_dept=" + allowed_dept + ", allowed_section=" + allowed_section + "]";
	}
	
	
}
