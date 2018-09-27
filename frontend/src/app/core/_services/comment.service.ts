import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Comment, Post } from '../_models';
import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class CommentService {

  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  // A user comments on a post
  create(comment: Comment, userId: number, postId: number) {
    return this.httpClient.post(this.connConfig.usersEndpoint + '/' + userId + '/'+
      this.connConfig.postsEndpoint + '/' + postId + '/' + this.connConfig.commentsEndpoint, comment);
  }

}
