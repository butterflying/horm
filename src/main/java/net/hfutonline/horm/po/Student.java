package net.hfutonline.horm.po;


public class Student {

	private String name;
	private Integer id;


	public void setName(String name){
		this.name = name;
	}
	public void setId(Integer id){
		this.id = id;
	}
	public String getName(){
		return this.name;
	}
	public Integer getId(){
		return this.id;
	}
}
