package me.ele.talaris.hermes.model;

public class SendVerifyCodeResult {
	public String hashValue; // required
	public String code; // required
	public String getHashValue() {
		return hashValue;
	}
	public SendVerifyCodeResult setHashValue(String hashValue) {
		this.hashValue = hashValue;
		return this;
	}
	public String getCode() {
		return code;
	}
	public SendVerifyCodeResult setCode(String code) {
		this.code = code;
		return this;
	}
	
}
