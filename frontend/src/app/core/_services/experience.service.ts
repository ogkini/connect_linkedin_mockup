import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Experience } from '../_models/index';
import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class ExperienceService {

  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  create(experience: Experience, userId: number) {
    return this.httpClient.post(this.connConfig.serverUrl + this.connConfig.usersEndpoint + '/' +
        userId + '/' + this.connConfig.experienceEndpoint, experience);
  }

  // update(user: User) {
  //   return this.httpClient.put('/api/users/' + user.id, user);
  // }

  delete(id: number, userId: number) {
    return this.httpClient.delete(this.connConfig.serverUrl + this.connConfig.usersEndpoint + '/' +
        userId + '/' + this.connConfig.experienceEndpoint + '/' + id);
  }
}
