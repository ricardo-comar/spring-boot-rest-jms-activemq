package com.github.ricardocomar.activemq.sample;

import java.time.LocalDateTime;


public class DemoMessage {

	private String message;
	
	private Boolean ack;

	private LocalDateTime received;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getAck() {
		return ack;
	}

	public void setAck(Boolean ack) {
		this.ack = ack;
	}

	public LocalDateTime getReceived() {
		return received;
	}

	public void setReceived(LocalDateTime received) {
		this.received = received;
	}

	public LocalDateTime getRead() {
		return read;
	}

	public void setRead(LocalDateTime read) {
		this.read = read;
	}

	private LocalDateTime read;
	
}
