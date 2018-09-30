import { Component, Input, OnInit } from '@angular/core';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { AuthenticationService, DataService } from '../../../_services';

@Component({
  selector: 'app-user-nav-bar',
  templateUrl: './user-nav-bar.component.html',
  styleUrls: ['./user-nav-bar.component.scss']
})

@NgModule({
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})

export class UserNavBarComponent implements OnInit {

  @Input() public signedInUserId: number;

  message: string [];

  constructor(
    private authenticationService: AuthenticationService,
    private dataService: DataService
  ) { }

  ngOnInit() {
    this.dataService.currentMessage.subscribe(message => this.message = message.split("-", 3));
  }

  logout() {
    this.authenticationService.logout();
  }

}
