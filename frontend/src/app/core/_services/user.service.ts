import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { User } from '../_models/index';
import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class UserService {
  constructor(
    private http: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  getAll() {
    return this.http.get<User[]>(this.connConfig.serverUrl + this.connConfig.signupEndpoint);
  }

  getById(id: number) {
    return this.http.get('/api/users/' + id);
  }

  create(user: User) {
    return this.http.post(this.connConfig.serverUrl + this.connConfig.signupEndpoint, user);
  }

  update(user: User) {
    return this.http.put('/api/users/' + user.id, user);
  }

  delete(id: number) {
    return this.http.delete('/api/users/' + id);
  }
}