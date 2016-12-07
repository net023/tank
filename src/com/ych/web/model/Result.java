package com.ych.web.model;

public class Result {
	private int code; //0失败,1成功
	private Object data;
	public Result() {
	}
	public Result(int code) {
		super();
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Result [code=" + code + ", data=" + data + "]";
	}
}
