import {Page} from './../models/page';
import {RoomChatsApiService} from './../services/room-chats-api.service';
import {RoomChatDto} from './../models/room-chat.model';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {Observable} from 'rxjs';
import {PageableDataSource} from "../utils/pageable.datasource";


/**
 * Data source for the Decisions view. This class should
 * encapsulate all logic for fetching and manipulating the displayed data
 * (including sorting, pagination, and filtering).
 */
export class RoomChatsDataSource extends PageableDataSource<RoomChatDto> {
  paginator: MatPaginator | undefined;
  sort: MatSort | undefined;

  constructor(
    private roomChatsApi: RoomChatsApiService
  ) {
    super();
  }

  getPageableContent(pageSize: number, pageIndex: number, sort: string[], filter: string): Observable<Page<RoomChatDto>> {
    return this.roomChatsApi.search(pageSize, pageIndex, [], '');
  }

}
