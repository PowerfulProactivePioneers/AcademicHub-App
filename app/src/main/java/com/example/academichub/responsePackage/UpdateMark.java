package com.example.academichub.responsePackage;

import java.util.List;

public class UpdateMark {
	private String cid,exam;
	private List<Marks> marks;
	public UpdateMark(String cid, String exam, List<Marks> marks) {
		super();
		this.cid = cid;
		this.exam = exam;
		this.marks = marks;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getExam() {
		return exam;
	}
	public void setExam(String exam) {
		this.exam = exam;
	}
	public List<Marks> getMarks() {
		return marks;
	}
	public void setMarks(List<Marks> marks) {
		this.marks = marks;
	}
	@Override
	public String toString() {
		return "UpdateMark [cid=" + cid + ", exam=" + exam + ", marks=" + marks + "]";
	}
	
	
	

}
