import { Injectable } from '@angular/core';

@Injectable()
export class ConnectionConfigService {
  serverUrl = '//localhost:9090/api/';

  signInEndpoint  = this.serverUrl + 'signin';

  usersEndpoint = this.serverUrl + 'users';
  experienceEndpoint = this.serverUrl + 'experience';
  educationEndpoint = this.serverUrl + 'education';
  relationshipEndpoint = this.serverUrl + 'relationships';
  networkEndpoint = this.serverUrl + 'network';

  generalFilesEndpoint = this.serverUrl + 'uploads';
  //userFilesEndpoint = this.serverUrl + this.generalFilesEndpoint + '/' + this.usersEndpoint;
}
