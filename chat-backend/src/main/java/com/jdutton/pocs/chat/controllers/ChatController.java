package com.jdutton.pocs.chat.controllers;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.jdutton.pocs.chat.models.documents.ChatMessage;

@Controller
@EnableScheduling
public class ChatController {
	
	@Autowired
	private SimpMessagingTemplate messageTemplate;
	
	private static final Logger LOG = LoggerFactory.getLogger(ChatController.class);
	
	private final List<String> colors = Arrays.asList(
		"#FF00FF",
		"#FFAFAF",
		"#00FF00",
		"#000000",
		"#FFFF00",
		"#00FFFF",
		"#404040",
		"#FF0000",
		"#FF0001",
		"#FF0011",
		"#FFC800",
		"#808080",
		"#0000FF",
		"#404040",
		"#C0C0C0",
		"#FF00FF"
	);
	
	@MessageMapping("/message")
	@SendTo("/topic/message")
	public ChatMessage receiveMessage(final ChatMessage chatMessage) {
		chatMessage.setDate(new Date().getTime());
		
		if (chatMessage.getType().equals("NEW_USER")) {
			chatMessage.setColor(this.colors.get(new SecureRandom().nextInt(colors.size())));
			chatMessage.setText(String.format("New user connected."));
		}
		
		return chatMessage;
	}
	
	@MessageMapping("/writing")
	//@SendTo("/topic/writing")
	public void writingMessage(final String username) {
		this.messageTemplate.convertAndSend("/topic/writing", username.concat(" is writting..."));
		//return username.concat(" is writting...");
	}
	
	@MessageMapping("/private")
	@SendToUser("/queue/user")
	public ChatMessage reply(@Payload final ChatMessage chatMessage, Principal user) {
		LOG.info("Received message... {}", user.getName());
		chatMessage.setDate(new Date().getTime());
		return chatMessage;
	}
	
}
