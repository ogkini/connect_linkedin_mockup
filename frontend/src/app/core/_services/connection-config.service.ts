import { Injectable } from '@angular/core';

@Injectable()
export class ConnectionConfigService {
  serverUrl = '//localhost:9090/api/'; // HTTP
  //serverUrl = 'https//localhost:8443/api/'; // HTTPS
  //serverUrl = '//localhost:8443/api/'; // HTTPS <-- try this with base URL to be n HTTPS

  signInEndpoint  = this.serverUrl + 'signin';

  usersEndpoint = this.serverUrl + 'users';

  generalFilesEndpoint = this.serverUrl + 'uploads';
  //userFilesEndpoint = this.serverUrl + this.generalFilesEndpoint + '/' + this.usersEndpoint;

  relationshipEndpoint = this.serverUrl + 'relationships';

  usersSearchEndpoint = this.usersEndpoint + '/searchUser';

  // These are just "endings", as between them and the "serverUrl" will be the userId.
  experienceEndpoint = 'experience';
  educationEndpoint = 'education';
  networkEndpoint = 'network';

}
