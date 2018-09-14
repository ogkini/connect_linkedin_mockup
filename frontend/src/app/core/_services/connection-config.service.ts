import { Injectable } from '@angular/core';

@Injectable()
export class ConnectionConfigService {
    signinEndpoint  = 'signin';
    signupEndpoint  = 'users';
    uploadEndpoint = 'uploads';
    uploadProfilePhotoEndpoint = this.uploadEndpoint +'/profilePhotos';

    serverUrl = '//localhost:9090/api/';
}
