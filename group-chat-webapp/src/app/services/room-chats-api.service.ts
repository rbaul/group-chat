import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../models/page';
import { RoomChatDto, RoomChatMessageDto } from '../models/room-chat.model';
import { PageableApiService } from '../utils/pageable-api-service';

const API_URL = '/api/v1/group-chats';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json'
  })
};

@Injectable({
  providedIn: 'root'
})
export class RoomChatsApiService extends PageableApiService<RoomChatDto> {

  constructor(
    http: HttpClient
  ) {
    super(http, API_URL);
  }

  public getPageable(): Observable<Page<RoomChatDto>> {
    return this.http.get<Page<RoomChatDto>>(API_URL);
  }

  public create(roomChat: RoomChatDto): Observable<RoomChatDto> {
    return this.http.post<RoomChatDto>(API_URL, roomChat, httpOptions);
  }

  public update(id: number, request: RoomChatDto): Observable<RoomChatDto> {
    return this.http.put<RoomChatDto>(`${API_URL}/${id}`, request, httpOptions);
  }

  public delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API_URL}/${id}`, httpOptions);
  }

  public get(id: number): Observable<RoomChatDto> {
    return this.http.get<RoomChatDto>(`${API_URL}/${id}`);
  }

  public getMessages(id: number): Observable<RoomChatMessageDto[]> {
    return this.http.get<RoomChatMessageDto[]>(`${API_URL}/${id}/history`);
  }
}
