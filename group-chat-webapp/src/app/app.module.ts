import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LoginComponent } from './login/login.component';
import { RoomChatsComponent } from './room-chats/room-chats.component';
import { RoomChatComponent } from './room-chat/room-chat.component';
import { MaterialModule } from './shared/material.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NavigationComponent } from './navigation/navigation.component';
import { LayoutModule } from '@angular/cdk/layout';
import { HttpClientModule } from '@angular/common/http';
import {FlexLayoutModule} from "@angular/flex-layout";
import {NgParticlesModule} from "ng-particles";
import {SocketIoConfig} from "./ngx-socket-io/socket-io.config";
import {SocketIoModule} from "./ngx-socket-io/socket-io.module";
import {environment} from "../environments/environment";

const config: SocketIoConfig = { url: `${environment.ws_url}/chats`, options: {transports: ['websocket']} };

@NgModule({
  declarations: [
    AppComponent,
      LoginComponent,
      RoomChatsComponent,
      RoomChatComponent,
      NavigationComponent
   ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    LayoutModule,
    FlexLayoutModule,
    NgParticlesModule,
    SocketIoModule.forRoot(config)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
