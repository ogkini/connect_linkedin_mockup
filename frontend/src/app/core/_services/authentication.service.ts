import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';
import 'rxjs/add/operator/map';

import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class AuthenticationService {

  constructor(
    private http: HttpClient,
    private router: Router,
    private connConfig: ConnectionConfigService
  ) { }

  login(email: string, password: string) {
    return this.http.post<any>(this.connConfig.serverUrl + this.connConfig.signInEndpoint, { email: email, password: password })
      .map(user => {
        // Login successful if there's a jwt token in the response
        if (user && user.accessToken) {
          // Store user details and jwt token in local storage
          // to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify(user));
        }

        return user;
      });
  }

  logout() {
    // Remove user from local storage to log user out
    localStorage.removeItem('currentUser');
    if ( !this.router.navigate(['/sign-in']) ) {
      console.error("Navigation to \"/sign-in\" has failed!");
    }
  }
}
