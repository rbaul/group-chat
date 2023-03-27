import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {WrappedSocket} from "../ngx-socket-io/socket-io.service";

@Injectable({
  providedIn: 'root'
})
export class SocketIoChatService {

  private socket: WrappedSocket | undefined;

  constructor() {
  }

  public initSocket(room: string | undefined = undefined, userName: string = ''): void {
    if (room) {
      this.socket = new WrappedSocket({
        url: `${environment.ws_url}?room=${room}&user=${userName}`, options: {
          transports: ['websocket'],
          // Work only in polling, websocket // WARN: this will be ignored in a browser
          // extraHeaders: {
          //   username: userName
          // }
        }
      });
    }
    throw new Error('Not specified room');
  }

  public onEvent<T>(event: string): Observable<T> {
    if (this.socket) {
      return this.socket.fromEvent<T>(event);
    }
    throw new Error('Socket not initialized');
  }

  public emit<T>(event: string, message: T): void {
    if (this.socket) {
      this.socket?.emit(event, message);
    }
    throw new Error('Socket not initialized')
  }

  public disconnect() {
    if (this.socket) {
      this.socket.removeAllListeners();
      this.socket.disconnect();
      this.socket = undefined;
    }
  }
}
