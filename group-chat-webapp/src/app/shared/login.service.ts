import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

const USER_NAME_LOCAL_STORAGE = 'user-name';
@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private loggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  private loggedUser: BehaviorSubject<string> = new BehaviorSubject<string>('');

  constructor() {
    const userName: string = this.getUser();

    if (userName) {
      this.loginProcess(userName);
    } else {
      this.logoutProcess();
    }
  }

  get loggedUserIn() {
    return this.loggedUser.asObservable();
  }

  get isLoggedIn() {
    return this.loggedIn.asObservable();
  }

  getUser(): string {
    return localStorage.getItem(USER_NAME_LOCAL_STORAGE) || '';
  }

  isLogged() {
    return !!localStorage.getItem(USER_NAME_LOCAL_STORAGE);
  }

  loginProcess(userName: string): void {
    localStorage.setItem(USER_NAME_LOCAL_STORAGE, userName);
    this.loggedUser.next(userName);
    this.loggedIn.next(true);
  }

  logoutProcess(): void {
    localStorage.removeItem(USER_NAME_LOCAL_STORAGE);
    this.loggedUser.next('');
    this.loggedIn.next(false);
  }

  logout(): void {
    this.logoutProcess();
  }

}
