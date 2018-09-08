import { Injectable } from '@angular/core';

@Injectable()
export class ConnectionConfigService {
    signinEndpoint  = 'signin';
    signupEndpoint  = 'users';

    serverUrl = '//localhost:9090/api/';
}
