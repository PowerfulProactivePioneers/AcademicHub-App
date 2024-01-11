package com.example.academichub.responsePackage;

public class MarkSchema {
	private String rno;
	private int cat1,cat2,cat3,assignment1,assignment2,assignment3;

	

	public MarkSchema() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MarkSchema(String rno, int cat1, int cat2, int cat3, int assignment1, int assignment2,int assignment3) {
		super();
		this.rno = rno;
		this.cat1 = cat1;
		this.cat2 = cat2;
		this.cat3 = cat3;
		this.assignment1 = assignment1;
		this.assignment2 = assignment2;
		this.assignment3 = assignment3;
	}
	public String getRno() {
		return rno;
	}

	public void setRno(String rno) {
		this.rno = rno;
	}

	public int getCat1() {
		return cat1;
	}

	public void setCat1(int cat1) {
		this.cat1 = cat1;
	}

	public int getCat2() {
		return cat2;
	}

	public void setCat2(int cat2) {
		this.cat2 = cat2;
	}

	public int getCat3() {
		return cat3;
	}

	public void setCat3(int cat3) {
		this.cat3 = cat3;
	}
	
	

	public int getAssignment1() {
		return assignment1;
	}

	public void setAssignment1(int assignment1) {
		this.assignment1 = assignment1;
	}

	public int getAssignment2() {
		return assignment2;
	}

	public void setAssignment2(int assignment2) {
		this.assignment2 = assignment2;
	}

	public int getAssignment3() {
		return assignment3;
	}

	public void setAssignment3(int assignment3) {
		this.assignment3 = assignment3;
	}

	@Override
	public String toString() {
		return "Marks [rno=" + rno + ", cat1=" + cat1 + ", cat2=" + cat2 + ", cat3=" + cat3 + ", assignment1="
				+ assignment1 + ", assignment2=" + assignment2 + ", assignment3=" + assignment3 + "]";
	}

	
	
}
