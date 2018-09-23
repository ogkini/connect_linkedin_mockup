import { Component, OnInit } from '@angular/core';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { AuthenticationService } from '../../../_services/index';

@Component({
  selector: 'app-user-nav-bar',
  templateUrl: './user-nav-bar.component.html',
  styleUrls: ['./user-nav-bar.component.scss']
})

@NgModule({
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})


export class UserNavBarComponent implements OnInit {

  constructor(
    private authenticationService: AuthenticationService
  ) { }

  ngOnInit() { }

  logout() {
    this.authenticationService.logout();
  }

}
