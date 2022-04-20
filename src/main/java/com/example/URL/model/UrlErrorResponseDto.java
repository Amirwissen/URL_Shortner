package com.example.URL.model;

public class UrlErrorResponseDto {

	private int status;
	private String error;
	
	public UrlErrorResponseDto() {
		
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	@Override
	public String toString() {
		return "UrlErrorResponseDto [status=" + status + ", error=" + error + "]";
	}
	
	
	
}
