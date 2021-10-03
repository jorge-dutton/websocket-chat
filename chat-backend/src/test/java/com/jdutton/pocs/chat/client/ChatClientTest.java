package com.jdutton.pocs.chat.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.jdutton.pocs.chat.controllers.ChatController;
import com.jdutton.pocs.chat.models.documents.ChatMessage;

//@SpringBootTest
public class ChatClientTest {
	
	private static Logger LOG = LoggerFactory.getLogger(ChatClientTest.class);
	
	
	@Test
	void sendClientTest() {
		StompSession mockSession = Mockito.mock(StompSession.class);
		StompHeaders mockHeaders = Mockito.mock(StompHeaders.class);
		ChatSessionHandler sessionHandler = new ChatSessionHandler("Jorge_dutton");
		sessionHandler.afterConnected(mockSession, mockHeaders);
		
		Mockito.verify(mockSession).subscribe("/chat/message", sessionHandler);
		Mockito.verify(mockSession).send(Mockito.anyString(), Mockito.anyObject());
	}
	
//	@Test
//	void sendMessage() {
//		
//		var message = new ChatMessage();
//		message.setDate(new Date().getTime());
//		message.setText("Text broadcasted");
//		try {
//		this.messageTemp.convertAndSendToUser("jorge_dutton","/queue/user", message);
//		this.messageTemp.convertAndSend("/app/message", message);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		LOG.info("Message sent");
//	}
	
	@Test
	void sendMessage2() throws Exception {
		List<Transport> transports = new ArrayList<>(1);
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));      

		SockJsClient sockjsClient = new SockJsClient(transports);   

		WebSocketStompClient stompClient = new WebSocketStompClient(sockjsClient);

		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		
		var url = "ws://127.0.0.1:8080/chat-websocket";
//		var username = "jorge_dutton";
		var username = "ernesto.matensalsa";
		
		StompSessionHandler sessionHandler = new ChatSessionHandler(username);
		StompSession session = stompClient.connect(url, sessionHandler).get();
		
		var chatMessage = new ChatMessage();
		chatMessage.setColor("#FF0011");
		chatMessage.setDate(new Date().getTime());
		chatMessage.setUsername("backend");
		chatMessage.setText("It works!");
		chatMessage.setType("MESSAGE");
		session.send("/queue/user/" + username, chatMessage);
		//LOG.info("Other message sent...");
		
	}
}
