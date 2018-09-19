import { Component, OnInit } from '@angular/core';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AuthenticationService } from '../../../_services/index';

@Component({
  selector: 'app-admin-nav-bar',
  templateUrl: './admin-nav-bar.component.html',
  styleUrls: ['./admin-nav-bar.component.scss']
})

@NgModule({
  exports: [  ],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})

export class AdminNavBarComponent implements OnInit {

  constructor(private authenticationService: AuthenticationService) { }

  ngOnInit() { }

  logout() {
    this.authenticationService.logout();
  }

}
