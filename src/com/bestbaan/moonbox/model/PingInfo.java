package com.bestbaan.moonbox.model;

public class PingInfo {
	public String addr;
	public String prompt;
	public String Success_info;
	public String Fail_info;
    public PingInfo(String addr,String prompt,String Success_info,String Fail_info){
    	this.addr=addr;
    	this.prompt=prompt;
    	this.Success_info=Success_info;
    	this.Fail_info=Fail_info;
    }
	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getSuccess_info() {
		return Success_info;
	}

	public void setSuccess_info(String success_info) {
		Success_info = success_info;
	}

	public String getFail_info() {
		return Fail_info;
	}

	public void setFail_info(String fail_info) {
		Fail_info = fail_info;
	}
}
