import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { User } from '../_models/index';
import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class UserService {
  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  getAll() {
    return this.httpClient.get<User[]>(this.connConfig.serverUrl + this.connConfig.signupEndpoint);
  }

  getById(id: number) {
    return this.httpClient.get('/api/users/' + id);
  }

  create(user: User) {
    return this.httpClient.post(this.connConfig.serverUrl + this.connConfig.signupEndpoint, user);
  }

  update(user: User) {
    return this.httpClient.put('/api/users/' + user.id, user);
  }

  delete(id: number) {
    return this.httpClient.delete('/api/users/' + id);
  }
}
