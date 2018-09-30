import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import {Occupation} from '../_models/index';
import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class OccupationService {

  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  create(occupation: Occupation, userId: number) {
    return this.httpClient.post(this.connConfig.usersEndpoint + '/' +
      userId + '/' + this.connConfig.occupationEndpoint, occupation);
  }

  // update(user: User) {
  //   return this.httpClient.put('/api/users/' + user.id, user);
  // }

  delete(occupationId: number, userId: number) {
    return this.httpClient.delete(this.connConfig.usersEndpoint + '/' +
      userId + '/' + this.connConfig.occupationEndpoint + '/' + occupationId);
  }

}
