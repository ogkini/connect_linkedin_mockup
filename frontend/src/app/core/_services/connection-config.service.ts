import { Injectable } from '@angular/core';

@Injectable()
export class ConnectionConfigService {

  serverUrl = '//localhost:9090/api/';

  signinEndpoint  = 'signin';
  usersEndpoint  = 'users';
  filesEndpoint = 'uploads';
  userFilesEndpoint = this.filesEndpoint + '/' + this.usersEndpoint;
}

