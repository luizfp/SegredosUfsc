package br.com.luizfp.segredosufsc.network;

/**
 * Objeto utilizado pelo WS para dar feedback das requisições.
 */
public class Response {
	private String status;
	private String msg;
	private Long number;
	public static final String OK = "OK";
	public static final String ERROR = "ERROR";

	public Response() {
	}

	public static Response Ok(Long number) {
		Response r = new Response();
		r.setStatus("OK");
		r.setNumber(number);
		return r;
	}

	public static Response Error(Long number) {
		Response r = new Response();
		r.setStatus("ERROR");
		r.setNumber(number);
		return r;
	}

	public static Response Ok(String string) {
		Response r = new Response();
		r.setStatus("OK");
		r.setMsg(string);
		return r;
	}

	public static Response Error(String string) {
		Response r = new Response();
		r.setStatus("ERROR");
		r.setMsg(string);
		return r;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}