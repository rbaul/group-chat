import {Component, OnDestroy, OnInit} from '@angular/core';
import {RoomChatsApiService} from "../services/room-chats-api.service";
import {ActivatedRoute} from "@angular/router";
import {RoomChatMessageDto, RoomChatMessageNotificationDto} from "../models/room-chat.model";
import {LoginService} from "../shared/login.service";
import {SocketIoChatService} from "../services/socket-io-chat.service";


@Component({
  selector: 'app-room-chat',
  templateUrl: './room-chat.component.html',
  styleUrls: ['./room-chat.component.css']
})
export class RoomChatComponent implements OnInit, OnDestroy {

  selectedConversation: RoomChatData = {};
  messages: RoomChatMessageDto[] = [];

  text: string = '';

  constructor(
    private roomChatsApiService: RoomChatsApiService,
    private activatedRoute: ActivatedRoute,
    private loginService: LoginService,
    private socket: SocketIoChatService
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

          this.socket.initSocket(`${roomId}`, this.selectedConversation.userName);
          this.socket.onEvent('connect').subscribe(value1 => {
            console.log('Connected');
          });

          this.socket.onEvent<RoomChatMessageNotificationDto>('chat-message-change').subscribe((message) => {
            console.log(`Received: ${message}`)
            let items = message.content;
            if (items) {
              this.messages.push(items);
            }
          });

        });

      }
    });
  }

  ngOnDestroy() {
    if (this.socket) {
      this.socket.disconnect();
    }
  }

  sendText(text: string) {
    console.log(`Message ${text} send`);
    const message: RoomChatMessageDto = {
      message: text,
      userName: this.selectedConversation.userName
    }
    this.socket?.emit('newMessage', message);
    this.text = '';
  }

}

interface RoomChatData {
  id?: number;
  name?: string;
  members?: string[];
  userName?: string;
}
