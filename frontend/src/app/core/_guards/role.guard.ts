import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { AuthGuard } from './auth.guard';
import * as jwt_decode from 'jwt-decode';

@Injectable()
export class RoleGuard implements CanActivate {

  constructor(public auth: AuthGuard, public router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    // This will be passed from the route config on the data property
    const expectedRole = route.data.expectedRole;
    const token = localStorage.getItem('currentUser');

    // Decode the token to get its payload
    const tokenPayload = jwt_decode(token);
    if (tokenPayload.role !== expectedRole) {
      this.router.navigate(['/sign-in']);
      return false;
    }

    return true;
  }
}
