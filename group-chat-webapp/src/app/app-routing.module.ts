import { inject, NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateChildFn, CanActivateFn, Router, RouterModule, RouterStateSnapshot, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RoomChatComponent } from './room-chat/room-chat.component';
import { RoomChatsComponent } from './room-chats/room-chats.component';
import { LoginService } from './shared/login.service';

export const canActivate: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  const authService = inject(LoginService);
  const router = inject(Router);

  if (authService.isLogged()) {
    return true;
  }
  router.navigate(['login'],
    { queryParams: { returnUrl: state.url } });
  return false;
};

export const canActivateChild: CanActivateChildFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => canActivate(route, state);

const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'room-chats', component: RoomChatsComponent, canActivate: [canActivate] },
  { path: 'room-chat/:id', component: RoomChatComponent, canActivate: [canActivate] },
  // { path: '**', redirectTo: 'login'},
  { path: '**', redirectTo: 'room-chats', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
