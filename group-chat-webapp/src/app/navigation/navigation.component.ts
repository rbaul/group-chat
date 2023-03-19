import { LoginService } from './../shared/login.service';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { map, Observable, shareReplay } from 'rxjs';
import { NavItem } from '../models/nav-item';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {

  activeLink = '';
  @Input() title: string = '';
  @Input() navList: NavItem[] = [];

  loggedUser: string = '';

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(
    private breakpointObserver: BreakpointObserver,
    private securityService: LoginService,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.securityService.loggedUserIn.subscribe(username => {
      this.loggedUser = username;
    });
  }

  handleClick(selectedItem: NavItem) {
    this.activeLink = selectedItem.linkTitle;
  }

  logout() {
    this.securityService.logout();
    this.router.navigate(['/login']);
  }

}
