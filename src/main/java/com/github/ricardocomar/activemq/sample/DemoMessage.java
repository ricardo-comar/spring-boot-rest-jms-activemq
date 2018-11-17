package com.github.ricardocomar.activemq.sample;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DemoMessage implements Serializable{

	private static final long serialVersionUID = 2388636809753103341L;

	@JsonProperty
	private String message;
	
	@JsonProperty
	private String ack;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DemoMessage [message=");
		builder.append(message);
		builder.append(", ack=");
		builder.append(ack);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ack == null) ? 0 : ack.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DemoMessage other = (DemoMessage) obj;
		if (ack == null) {
			if (other.ack != null)
				return false;
		} else if (!ack.equals(other.ack))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAck() {
		return ack;
	}

	public void setAck(String ack) {
		this.ack = ack;
	}
}
