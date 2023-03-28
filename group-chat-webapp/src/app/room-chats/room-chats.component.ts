import {GroupChatNotificationDto, RoomChatDto} from './../models/room-chat.model';
import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTable} from '@angular/material/table';
import {RoomChatsDataSource} from './room-chats-datasource';
import {RoomChatsApiService} from '../services/room-chats-api.service';
import {Router} from '@angular/router';
import {UnsubscribeOnDestroyAdapter} from "../utils/unsubscribe-on-destroy-adapter";
import {WrappedSocket} from "../ngx-socket-io/socket-io.service";


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

  pageSize = 10;
  pageSizeOptions = [5, 10, 20];

  constructor(
    private roomChatsApi: RoomChatsApiService,
    private router: Router,
    // private socket: SocketIoService,

    private socket: WrappedSocket
  ) {
    super();
    this.dataSource = new RoomChatsDataSource(roomChatsApi);
  }

  ngOnInit() {

    this.subscriptions.push(this.socket.fromEvent<GroupChatNotificationDto>('group-chats-change').subscribe((message) => {
      console.log(`Received: ${message}`)
      let items = message.content;
      if (items) {
        let find = this.dataSource.getData()
          .find(value => value.id === items?.id);
        if (find) {
          find.name = items.name;
          find.description = items.description;
          find.participants = items.participants;
        }
      }
    }));

    // Disconnect
    this.subscriptions.push(this.socket.fromEvent('disconnect').subscribe(() => {
      console.log('The client has disconnected!')
    }));

    // Reconnect attempts
    this.subscriptions.push(this.socket.fromEvent('reconnect_attempt').subscribe(attempts => {
      console.log(`Try to reconnect at ${attempts} attempt(s).`)
    }));
  }

  override ngOnDestroy() {
    super.ngOnDestroy();
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

}
