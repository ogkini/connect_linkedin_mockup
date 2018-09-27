import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Post } from '../_models';
import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class LikeService {

  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  // A user likes a post
  create(userId: number, postId: number) {
    return this.httpClient.post(this.connConfig.usersEndpoint + '/' + userId + '/'+
      this.connConfig.postsEndpoint + '/' + postId + '/' + this.connConfig.likesEndpoint, null);
  }

  // A user deletes a like
  delete(ownerId: number, postId: number, likeId: number) {
    return this.httpClient.delete(this.connConfig.usersEndpoint + '/' + ownerId + '/' +
    this.connConfig.postsEndpoint + '/' + postId + '/' + this.connConfig.likesEndpoint + '/' + likeId);
  }

}
