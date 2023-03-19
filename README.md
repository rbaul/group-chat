# Chat Spring Boot - Angular
> WebSocket
> 
> SocketIO
> 
> RSocket

## Build on
* Java 17, Framework Spring Boot 3.0.4
* Angular 15.2.0, Framework Angular Material
* Gradle 7.6.1

## Demo
![](demo/Chat_demo.gif)

## Run dev
Run `GroupChatApplication.java`

> Execute gradle Task `:frontend:runDevProxy`
### Open
> http://<remote_ip>:4200

## Run production
> Execute gradle Task `Task :backend:bootJar`

> Run command `java -jar group-chat-0.0.1-SNAPSHOT.jar`
### Open
> http://<remote_ip>:8080