import { User, Like, Comment } from '.';

export class Post {
  id: number;
  owner: User;
  text: string;
  createdTime: string;

  likes: Array<Like>;
  comments: Array<Comment>;

  likesPost: boolean;
  likesCount: number;
  commentsCount: number;
  
  isRecommended: boolean;
}
