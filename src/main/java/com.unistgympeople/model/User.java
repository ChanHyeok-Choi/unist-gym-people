package com.unistgympeople.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String userId;
    private String gender;
    private int age;
    private int occupation;
    private String zipcode;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

    public void setAge(int age) {
        this.age = age;
    }

    public int getOccupation() {
        return occupation;
    }

	public void setOccupation(int occupation) {
		this.occupation = occupation;
	}

    public String getZipcode() {
        return zipcode;
    }

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
}
