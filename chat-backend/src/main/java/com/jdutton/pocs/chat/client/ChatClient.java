package com.jdutton.pocs.chat.client;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.jdutton.pocs.chat.models.documents.ChatMessage;

@Component
public class ChatClient {
	
	@Autowired
	private SimpMessagingTemplate messageTemplate;
	
	private static final Logger LOG = LoggerFactory.getLogger(ChatClient.class);
	
	private static final String CHAT_URL = "ws://127.0.0.1:8080/chat-websocket";
	
	public ChatClient() {
		super();
		
	}
	
	public void sendMessage(final ChatMessage chatMessage) {
		WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		StompSessionHandler sessionHandler = new ChatSessionHandler("jorge_dutton");
		try {
			StompSession stompSession = stompClient.connect(CHAT_URL, sessionHandler).get();
			stompSession.send("/queue/user/jorge_dutton", chatMessage);
			new Scanner(System.in).nextLine();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
