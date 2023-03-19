import {Component, OnDestroy, OnInit} from '@angular/core';
import {RoomChatsApiService} from "../services/room-chats-api.service";
import {ActivatedRoute} from "@angular/router";
import {RoomChatMessageDto, RoomChatMessageNotificationDto} from "../models/room-chat.model";
import {LoginService} from "../shared/login.service";
import {CompatClient, IFrame, Stomp} from '@stomp/stompjs';
import {StompHeaders} from "@stomp/stompjs/src/stomp-headers";

declare let SockJS: any

@Component({
  selector: 'app-room-chat',
  templateUrl: './room-chat.component.html',
  styleUrls: ['./room-chat.component.css']
})
export class RoomChatComponent implements OnInit, OnDestroy {

  selectedConversation: RoomChatData = {};
  messages: RoomChatMessageDto[] = [];

  text: string = '';
  stompClient: CompatClient | undefined;

  constructor(
    private roomChatsApiService: RoomChatsApiService,
    private activatedRoute: ActivatedRoute,
    private loginService: LoginService
  ) {
  }

  ngOnInit() {
    this.activatedRoute.params.subscribe(value => {
      let roomId: number = value['id'];
      if (roomId) {
        this.roomChatsApiService.get(roomId).subscribe(result => {
          this.selectedConversation = result;
          this.selectedConversation.userName = this.loginService.getUser();
          this.selectedConversation.id = roomId;
          const socket: WebSocket = new SockJS('/group-chat-websocket');
          this.stompClient = Stomp.over(socket);
          let headers: StompHeaders = {
            user: this.selectedConversation.userName,
            roomId: `${this.selectedConversation.id}`
          };

          this.stompClient.connect(headers, (frame: IFrame) => {
            this.stompClient?.subscribe(`/topic/group-chats/${this.selectedConversation.id}/messages-changed`, message => {
              console.log(`Received: ${message}`)
              if (message.body) {
                let notification: RoomChatMessageNotificationDto = JSON.parse(message.body);
                let items = notification.content;
                if (items) {
                  this.messages.push(items);
                }

              }
            });
          });
        });

      }
    });
  }

  ngOnDestroy() {
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  }

  sendText(text: string) {
    console.log(`Message ${text} send`);
    const message: RoomChatMessageDto = {
      message: text,
      userName: this.selectedConversation.userName
    }
    this.stompClient?.publish({ destination: `/group-chats/${this.selectedConversation.id}/message`, body: JSON.stringify(message) });
    this.text = '';
    // this.selectedConversation.sendText(text).then(() => this.text = "")
  }

}

interface RoomChatData {
  id?: number;
  name?: string;
  members?: string[];
  userName?: string;
}
