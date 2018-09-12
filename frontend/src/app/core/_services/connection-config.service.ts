import { Injectable } from '@angular/core';

@Injectable()
export class ConnectionConfigService {
    signinEndpoint  = 'signin';
    signupEndpoint  = 'users';
    uploadEndpoint = 'uploads';

    serverUrl = '//localhost:9090/api/';
}
