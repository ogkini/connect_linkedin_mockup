import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Network, FriendRequest } from '../_models/index';
import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class RelationshipService {

  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  getAll(userId: number) {
    return this.httpClient.get<Network>(this.connConfig.serverUrl + this.connConfig.usersEndpoint +
        '/' + userId + '/' + this.connConfig.networkEndpoint);
  }

  create(friendRequest: FriendRequest) {
    return this.httpClient.post(this.connConfig.serverUrl + this.connConfig.relationshipEndpoint, friendRequest);
  }

  decline(id: number) {
    return this.httpClient.delete(this.connConfig.serverUrl + this.connConfig.relationshipEndpoint + '/' + id);
  }

  accept(id: number) {
    return this.httpClient.put(this.connConfig.serverUrl + this.connConfig.relationshipEndpoint + '/' + id, null)
  }

}
