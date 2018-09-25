import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Education } from '../_models/index';
import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class EducationService {

  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  create(education: Education, userId: number) {
    return this.httpClient.post(this.connConfig.usersEndpoint + '/' +
        userId + '/' + this.connConfig.educationEndpoint, education);
  }

  // update(user: User) {
  //   return this.httpClient.put('/api/users/' + user.id, user);
  // }

  delete(id: number, userId: number) {
    return this.httpClient.delete(this.connConfig.usersEndpoint + '/' +
        userId + '/' + this.connConfig.educationEndpoint + '/' + id);
  }
}
