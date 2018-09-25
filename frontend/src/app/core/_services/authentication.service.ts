import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Router } from '@angular/router';
import 'rxjs/add/operator/map';

import { ConnectionConfigService } from './connection-config.service';
import {first} from "rxjs/operators";
import {User} from "../_models";
import {AlertService} from "./alert.service";

@Injectable()
export class AuthenticationService {

  currentUser: User;

  constructor(
    private http: HttpClient,
    private router: Router,
    private connConfig: ConnectionConfigService,
    private alertService: AlertService
  ) { }

  login(email: string, password: string) {
    return this.http.post<any>(this.connConfig.signInEndpoint, { email: email, password: password })
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

  public authenticateUserAndGoToHome(email: string, password: string ) {

    this.login(email, password)
      .pipe(first()).subscribe(
      user => {
        this.currentUser = user;

        // Set url to redirect to after signIn
        if (email === 'admin@mail.com') {
          if (!this.router.navigate(['/home-admin'])) {
            console.error("Navigation from \"SignIn\" to \"/home-admin\" failed!");
          }
        } else {
          if (!this.router.navigate(['/users', this.currentUser.id])) {
            console.error("Navigation from \"SignIn\" to \"/users/\"" + this.currentUser.id + "\" failed!");
          }
        }
      },
      error => { this.alertService.error(error.message); }
    );
  }

}
