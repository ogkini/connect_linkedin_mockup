import { User, Conversation } from '.';

export class Message {
  id: number;
  conversation: Conversation;
  sender: User;
  receiver: User;
  text: String;
  createdTime: string;
  seen: boolean;
  read: boolean;
}
