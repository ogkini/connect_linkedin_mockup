import { User, Message } from '.';

export class Conversation {
  id: number;
  userOne: User;
  userTwo: User;
  messages: Array<Message>;

  // Used to indicate the active conversation button
  // on the left hand side of the messages page
  // active: boolean;
}
