export interface RoomChatDto {
  id: number;
  name: string;
  description: string;
  participants: string[];
}

export interface RoomChatMessageDto {
  id?: number;
  userName?: string;
  message?: string;
  dateTime?: string;
  type?: string;
}

export enum NotificationType {
  CREATE = 'CREATE',
  DELETE = 'DELETE',
  UPDATE = 'UPDATE'
}

export interface RoomChatMessageNotificationDto {
  notificationType?: NotificationType;
  content?: RoomChatMessageDto;
}

export interface GroupChatNotificationDto {
  notificationType?: NotificationType;
  content?: RoomChatDto;
}
