import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Post } from '../_models';
import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class PostService {

  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  // Returns all posts of a user
  getAll(userId: number) {
    return this.httpClient.get<Post[]>(this.connConfig.usersEndpoint + '/' +
      userId + '/' + this.connConfig.postsEndpoint);
  }

  // Returns all posts of a user's connections
  getHome(userId: number) {
    return this.httpClient.get<Post[]>(this.connConfig.usersEndpoint + '/' +
      userId + '/' + this.connConfig.homeEndpoint);
  }

  create(post: Post, userId: number) {
    return this.httpClient.post(this.connConfig.usersEndpoint + '/' +
      userId + '/' + this.connConfig.postsEndpoint, post);
  }

  // update(user: Post) {
  //   return this.httpClient.put(this.connConfig.usersEndpoint + '/' + user.id, user);
  // }
  //
  // delete(id: number) {
  //   return this.httpClient.delete(this.connConfig.usersEndpoint + '/' + id);
  // }

}
