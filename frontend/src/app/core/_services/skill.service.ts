import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Skill} from '../_models/index';
import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class SkillService {

  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  create(skill: Skill, userId: number) {
    return this.httpClient.post(this.connConfig.usersEndpoint + '/' +
      userId + '/' + this.connConfig.skillEndpoint, skill);
  }

  // update(user: User) {
  //   return this.httpClient.put('/api/users/' + user.id, user);
  // }

  delete(skillId: number, userId: number) {
    return this.httpClient.delete(this.connConfig.usersEndpoint + '/' +
      userId + '/' + this.connConfig.skillEndpoint + '/' + skillId);
  }

}
