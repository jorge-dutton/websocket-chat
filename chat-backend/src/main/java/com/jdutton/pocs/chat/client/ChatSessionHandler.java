package com.jdutton.pocs.chat.client;

import java.lang.reflect.Type;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.jdutton.pocs.chat.models.documents.ChatMessage;


public class ChatSessionHandler extends StompSessionHandlerAdapter{
	
	private static final Logger LOG = LoggerFactory.getLogger(ChatSessionHandler.class);
	
	private String username;
	
	public ChatSessionHandler (final String username) {
		super();
		this.username = username;
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return ChatMessage.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		var msg = (ChatMessage)payload;
		LOG.info("{} Reveived: {}", msg.getDate(), msg.getText());
	}

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		LOG.info("New session stablished");
//		session.subscribe("/queue/user/jorge_dutton", this);
//		ChatMessage testMessage = new ChatMessage();
//		testMessage.setUsername("jorge_dutton");
//		testMessage.setDate(new Date().getTime());
//		testMessage.setText("Connection test");
//		testMessage.setType("MESSAGE");
//		session.send("/queue/user/"+testMessage.getUsername(), testMessage);
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
			Throwable exception) {
		exception.printStackTrace();
	}

	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		exception.printStackTrace();
	}

}
