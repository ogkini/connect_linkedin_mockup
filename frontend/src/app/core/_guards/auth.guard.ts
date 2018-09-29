import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (localStorage.getItem('currentUser')) {
      console.debug("Logged in.");
      // Logged in so return true
      return true;
    }
    else
      console.debug("NOT logged in.");

    // Not logged in so redirect to login page
    this.router.navigate(['/sign-in'], { queryParams: { returnUrl: state.url }});
    return false;
  }
}
