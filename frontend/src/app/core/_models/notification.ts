import { User, Post } from '.';

export class Notification {
  id: number;
  user: User;
  from: User;
  action: string;
  post: Post;
  createdTime: string;
  seen: boolean;
}
