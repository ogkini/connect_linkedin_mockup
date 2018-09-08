import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { ConnectionConfigService } from './connection-config.service';
import 'rxjs/add/operator/map';

@Injectable()
export class AuthenticationService {

  constructor(
    private http: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  login(email: string, password: string) {
    return this.http.post<any>(this.connConfig.serverUrl + this.connConfig.signinEndpoint, { email: email, password: password })
      .map(user => {
        // Login successful if there's a jwt token in the response
        if (user && user.token) {
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
  }
}
