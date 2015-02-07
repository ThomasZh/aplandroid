package com.redoct.iclub.item;

import com.redoct.iclub.util.CharacterParser;

public class ContactItem {
	  /*"desc": "",
      "email": "thomas.zh@qq.com",
      "id": "33d1dfdf-c11b-42bf-90a6-2b43e1f16f4e",
      "imageUrl": "http://tripc2c-person-face.b0.upaiyun.com/1/3/5/7af985595d94e9fad1ac953393af891f.jpg",
      "loginName": "",
      "name": "大",
      "state": 81,
      "telephone": ""*/
	private  String sortLetters;
	private String desc;
	private String email;
	private String id;
	private String imageUrl;
	private String loginName;
	private String name;
	private int state ;
    private String telephone;
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		CharacterParser characterParser = CharacterParser.getInstance();
		String pinyin = characterParser.getSelling(name);
		String sortString = pinyin.substring(0, 1).toUpperCase();
		
		// 正则表达式，判断首字母是否是英文字母
		if(sortString.matches("[A-Z]")){
			setSortLetters(sortString.toUpperCase());
		}else{
			setSortLetters("#");
		}
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
    
}
