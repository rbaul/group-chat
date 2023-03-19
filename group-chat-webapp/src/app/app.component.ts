import { LoginService } from './shared/login.service';
import { Component } from '@angular/core';
import { NavItem } from './models/nav-item';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Room Chat Application';

  isLoggedIn$: boolean = false;

  navList: NavItem[] = [
    { linkTitle: 'Room Chats', link: '/room-chats', icon: 'ballot' }
  ];

  constructor(
    private securityService: LoginService
  ) { }

  ngOnInit() {
    this.securityService.isLoggedIn.subscribe(logged => {
      this.isLoggedIn$ = logged;
    });
  }
}
