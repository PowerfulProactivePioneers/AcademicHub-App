package com.example.academichub.responsePackage;

public class Post {
	private String title,desc,files,cid,date;
	private boolean assignment;
	public Post(String title, String desc, String files, String cid, String date, boolean assignment) {
		super();
		this.title = title;
		this.desc = desc;
		this.files = files;
		this.cid = cid;
		this.date = date;
		this.assignment = assignment;
	}
	public String getTitle() {
		return title;
	}
	public String getDesc() {
		return desc;
	}
	public String getFiles() {
		return files;
	}
	public String getCid() {
		return cid;
	}
	public String getDate() {
		return date;
	}
	public boolean isAssignment() {
		return assignment;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void setFiles(String files) {
		this.files = files;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setAssignment(boolean assignment) {
		this.assignment = assignment;
	}

	@Override
	public String toString() {
		return "Post [title=" + title + ", desc=" + desc + ", files=" + files + ", cid=" + cid + ", date=" + date
				+ ", assignment=" + assignment + "]";
	}
}
