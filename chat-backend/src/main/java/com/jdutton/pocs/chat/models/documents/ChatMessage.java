package com.jdutton.pocs.chat.models.documents;

import java.io.Serializable;

import lombok.Data;

@Data
public class ChatMessage implements Serializable {
	
	private static final long serialVersionUID = 5141287198248034229L;
	
	private String text;
	private Long date;
	private String username;
	private String type;
	private String color;

}
