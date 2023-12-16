package com.example.academichub.responsePackage;

import java.util.List;

public class AttendanceReport {
	
	private String cid;
	private List<AttendanceList> lst;
	public AttendanceReport(String cid, List<AttendanceList> lst) {
		super();
		this.cid = cid;
		this.lst = lst;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public List<AttendanceList> getLst() {
		return lst;
	}
	public void setLst(List<AttendanceList> lst) {
		this.lst = lst;
	}
	@Override
	public String toString() {
		return "AttendanceReport [cid=" + cid + ", lst=" + lst + "]";
	}
	
	

}
