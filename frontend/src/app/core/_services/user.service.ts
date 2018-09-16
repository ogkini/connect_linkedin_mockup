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
    return this.httpClient.get<User[]>(this.connConfig.serverUrl + this.connConfig.usersEndpoint);
  }

  getById(id: number) {
    return this.httpClient.get(this.connConfig.serverUrl + this.connConfig.usersEndpoint + '/' + id);
  }

  create(user: User) {
    return this.httpClient.post(this.connConfig.serverUrl + this.connConfig.usersEndpoint, user);
  }

  update(user: User) {
    return this.httpClient.put(this.connConfig.serverUrl + this.connConfig.usersEndpoint + '/' + user.id, user);
  }

  delete(id: number) {
    return this.httpClient.delete(this.connConfig.serverUrl + this.connConfig.usersEndpoint + '/' + id);
  }

}
