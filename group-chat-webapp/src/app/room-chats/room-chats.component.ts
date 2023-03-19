import {GroupChatNotificationDto, RoomChatDto, RoomChatMessageNotificationDto} from './../models/room-chat.model';
import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTable} from '@angular/material/table';
import {RoomChatsDataSource} from './room-chats-datasource';
import {RoomChatsApiService} from '../services/room-chats-api.service';
import {Router} from '@angular/router';
import {CompatClient, IFrame, Stomp} from '@stomp/stompjs';
import {StompHeaders} from "@stomp/stompjs/src/stomp-headers";
import {UnsubscribeOnDestroyAdapter} from "../utils/unsubscribe-on-destroy-adapter";

declare let SockJS: any

@Component({
  selector: 'app-room-chats',
  templateUrl: './room-chats.component.html',
  styleUrls: ['./room-chats.component.scss']
})
export class RoomChatsComponent extends UnsubscribeOnDestroyAdapter implements OnInit, OnDestroy, AfterViewInit {

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<RoomChatDto>;
  dataSource: RoomChatsDataSource;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['name', 'description', 'participants', 'actions'];

  stompClient: CompatClient | undefined;

  pageSize = 10;
  pageSizeOptions = [5, 10, 20];

  constructor(
    private roomChatsApi: RoomChatsApiService,
    private router: Router
  ) {
    super();
    this.dataSource = new RoomChatsDataSource(roomChatsApi);
  }

  ngOnInit() {
    const socket: WebSocket = new SockJS('/group-chat-websocket');
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, (frame: IFrame) => {
      this.stompClient?.subscribe(`/topic/group-chats/changed`, message => {
        console.log(`Received: ${message}`)
        if (message.body) {
          let notification: GroupChatNotificationDto = JSON.parse(message.body);
          let items = notification.content;
          if (items) {
            let find = this.dataSource.getData()
              .find(value => value.id === items?.id);
            if (find) {
              find.name = items.name;
              find.description = items.description;
              find.participants = items.participants;
            }
          }
        }
      });
    });
  }

  override ngOnDestroy() {
    super.ngOnDestroy();
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
    this.dataSource.loadContent(this.paginator.pageSize, this.paginator.pageIndex);
    this.subscriptions.push(this.paginator.page.subscribe(event => {
      this.dataSource.loadContent(this.paginator.pageSize, this.paginator.pageIndex);
    }));
  }

  openChat(room: RoomChatDto) {
    this.router.navigate([`/room-chat/${room.id}`]);
  }

  // onPaginateChange(event: PageEvent) {
  //   this.dataSource.loadContent(this.paginator.pageSize, this.paginator.pageIndex)
  // }
}
