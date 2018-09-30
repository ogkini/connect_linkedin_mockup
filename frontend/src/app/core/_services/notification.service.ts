import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Notification } from '../_models/index';
import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class NotificationService {

  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  getAll(userId: number) {
    return this.httpClient.get<Notification[]>(this.connConfig.usersEndpoint + '/' +
      userId + '/' + this.connConfig.notificationsEndpoint);
  }

}
