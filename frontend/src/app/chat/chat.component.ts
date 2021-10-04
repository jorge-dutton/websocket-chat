import { Component, OnInit } from '@angular/core';
import { Client } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { Message } from './models/message';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  
  private stompClient: Client;
  connected: boolean = false;
  message: Message = new Message();
  messages: Message[] = [];
  writing: string;


  constructor() { }

  ngOnInit(): void {
    this.stompClient = new Client();

    this.stompClient.webSocketFactory = ()=> {
      return new SockJS("http://localhost:8080/chat-websocket");
    }

    this.stompClient.onConnect = (frame) => {
      console.log("Connected: " + this.stompClient.connected + ':' + frame);
      this.connected = true;
      
      this.stompClient.subscribe('/topic/message', e => {
        console.log(e);
        let receivedMessage: Message = JSON.parse(e.body) as Message;
        receivedMessage.date = new Date(receivedMessage.date);
        
        if (receivedMessage.type == "NEW_USER" && 
          this.message.username == receivedMessage.username) {
          this.message.color = receivedMessage.color;
        }

        this.messages.push(receivedMessage);
        console.log(receivedMessage);
      });
      
      this.stompClient.subscribe('/topic/writing', e => {
        console.log(e.body);
        this.writing = e.body;
        setTimeout(() => this.writing = '', 3000);
      });

      this.stompClient.subscribe('/queue/user/' + this.message.username, e => {
        console.log(e);
      });

      this.message.type = 'NEW_USER';
      this.stompClient.publish({destination: '/app/message', body: JSON.stringify(this.message)});

     
      this.stompClient.subscribe('/queue/user/' + this.message.username, e => {
        console.log(e);
        let message: Message = JSON.parse(e.body) as Message;
        console.log(message);
        message.date = new Date(message.date);
        this.messages.push(message);
      });
      
    }

    this.stompClient.onDisconnect = (frame) => {
      console.log("Disonnected: " + !this.stompClient.connected + ':' + frame);
      this.connected = false;
    }

  }
  
  connect(): void {
    this.stompClient.activate();
  }

  disconnect(): void {
    this.stompClient.deactivate();
  }

  sendMessage(): void {
    this.message.type = 'MESSAGE';
    this.stompClient.publish({
      destination: '/app/message',
      body: JSON.stringify(this.message)
    });

    this.message.text = '';
  }

  writingEvent(): void {
    this.stompClient.publish({destination: '/app/writing', body: this.message.username});
  }
}
