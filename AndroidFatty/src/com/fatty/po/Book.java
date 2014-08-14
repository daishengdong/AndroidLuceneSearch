package com.fatty.po;

public class Book {
	private int id;
	private String name;
	private String firstTitle;
	private String secondTitle;
	private String content;
	private String url;

    public Book() {  
        super();  
    }  
    public Book(int id, String name, String firstTitle, String secondTitle, String content, String url) {  
        super();  
        this.id = id;
        this.name = name;
        this.firstTitle = firstTitle;
        this.secondTitle = secondTitle;
        this.content = content;
        this.url = url;
    }  

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstTitle() {
		return firstTitle;
	}

	public void setFirstTitle(String firstTitle) {
		this.firstTitle = firstTitle;
	}

	public String getSecondTitle() {
		return secondTitle;
	}

	public void setSecondTitle(String secondTitle) {
		this.secondTitle = secondTitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {

		return "���ɷ������ƣ�" + this.name + "\n" + "һ�����⣺" + this.firstTitle + "\n"
				+ "�������⣺" + this.secondTitle + "\n" + "����" + this.content
				+ "\n" + "url��ַ" + this.getUrl() + "\n";
	}
}
