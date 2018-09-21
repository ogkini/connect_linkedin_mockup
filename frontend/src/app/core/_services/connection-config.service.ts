import { Injectable } from '@angular/core';

@Injectable()
export class ConnectionConfigService {
  serverUrl = '//localhost:9090/api/';

  signinEndpoint  = 'signin';

  usersEndpoint = 'users';
  experienceEndpoint = 'experience';
  educationEndpoint = 'education';

  filesEndpoint = 'uploads';
  userFilesEndpoint = this.filesEndpoint + '/' + this.usersEndpoint;
}
