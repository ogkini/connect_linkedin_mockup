import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class JobApplyService {

  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  // A user applies to a job-offer
  create(userId: number, jobOfferId: number) {
    return this.httpClient.post(this.connConfig.usersEndpoint + '/' + userId + '/'+
      this.connConfig.jobOffersEndpoint + '/' + jobOfferId + '/' + this.connConfig.jobAppliesEndpoint, null);
  }

  // A user un-applies from job-offer.
  delete(ownerId: number, jobOfferId: number, jobApplyId: number) {
    return this.httpClient.delete(this.connConfig.usersEndpoint + '/' + ownerId + '/' +
      this.connConfig.jobOffersEndpoint + '/' + jobOfferId + '/' + this.connConfig.jobAppliesEndpoint + '/' + jobApplyId);
  }

}
