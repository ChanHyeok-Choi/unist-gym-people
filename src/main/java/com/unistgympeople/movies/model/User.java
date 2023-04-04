package com.unistgympeople.movies.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

	@Id
	private String userId;
    private String gender;
    private Long age;
    private Long occupation;
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

	public Long getAge() {
		return age;
	}

    public void setAge(Long age) {
        this.age = age;
    }

    public Long getOccupation() {
        return occupation;
    }

	public void setOccupation(Long occupation) {
		this.occupation = occupation;
	}

    public String getZipcode() {
        return zipcode;
    }

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
}
