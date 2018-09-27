import { User, Like } from '.';

export class Post {
  id: number;
  owner: User;
  text: string;
  createdTime: string;

  likes: Array<Like>;
  
  likesPost: boolean;
  likesCount: number;
  commentsCount: number;
}
