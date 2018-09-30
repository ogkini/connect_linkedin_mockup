import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { JobOffer } from '../_models';
import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class JobOfferService {

  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  // Returns all jobOffers of a user
  getAll(userId: number) {
    return this.httpClient.get<JobOffer[]>(this.connConfig.usersEndpoint + '/' +
      userId + '/' + this.connConfig.jobOffersEndpoint);
  }

  // Returns all jobOffers of a user's connections
  getTimeline(userId: number) {
    return this.httpClient.get<JobOffer[]>(this.connConfig.usersEndpoint + '/' +
      userId + '/' + this.connConfig.jobOffersTimelineEndpoint);
  }

  create(jobOffer: JobOffer, userId: number) {
    return this.httpClient.post(this.connConfig.usersEndpoint + '/' +
      userId + '/' + this.connConfig.jobOffersEndpoint, jobOffer);
  }

  // update(user: JobOffer) {
  //   return this.httpClient.put(this.connConfig.usersEndpoint + '/' + user.id, user);
  // }
  //
  // delete(id: number) {
  //   return this.httpClient.delete(this.connConfig.usersEndpoint + '/' + id);
  // }

}
