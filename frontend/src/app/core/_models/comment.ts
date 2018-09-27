import { User, Post } from '.';

export class Comment {
  id: number;
  user: User;
  post: Post;
  text: string;
}
