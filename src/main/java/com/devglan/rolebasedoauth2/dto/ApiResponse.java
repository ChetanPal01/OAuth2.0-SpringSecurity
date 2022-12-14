package com.devglan.rolebasedoauth2.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
public class ApiResponse {
	
	private int status;
	private String message;
	private Object result;

	public ApiResponse(HttpStatus status, String message, Object result){
	    this.status = status.value();
	    this.message = message;
	    this.result = result;
    }

    public ApiResponse(HttpStatus status, String message){
        this.status = status.value();
        this.message = message;
    }



}
