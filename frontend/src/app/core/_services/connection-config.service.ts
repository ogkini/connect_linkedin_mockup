import { Injectable } from '@angular/core';

@Injectable()
export class ConnectionConfigService {
  serverUrl = '//localhost:9090/api/';

  signInEndpoint  = this.serverUrl + 'signin';

  usersEndpoint = this.serverUrl + 'users';

  generalFilesEndpoint = this.serverUrl + 'uploads';
  //userFilesEndpoint = this.serverUrl + this.generalFilesEndpoint + '/' + this.usersEndpoint;

  relationshipEndpoint = this.serverUrl + 'relationships';

  // These are just "endings", as between them and the "serverUrl" will be the userId.
  experienceEndpoint = 'experience';
  educationEndpoint = 'education';
  networkEndpoint = 'network';

}
