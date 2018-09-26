import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

import { User } from '../../../_models';
import { ConnectionConfigService, UserService } from '../../../_services';


@Component({
  selector: 'app-home-admin',
  templateUrl: './home-admin.component.html',
  styleUrls: ['./home-admin.component.scss']
})
export class HomeAdminComponent implements OnInit {

  title = 'Home';
  signedInUser: User;
  users: User[] = [];
  public profilePhotosEndpoint: string;

  public constructor(
      private titleService: Title,
      private userService: UserService,
      private connConfig: ConnectionConfigService
  ) {
    this.titleService.setTitle( this.title );
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));
    this.profilePhotosEndpoint = this.connConfig.usersEndpoint;
  }

  ngOnInit() {
    this.getAllUsers();
  }

  private getAllUsers() {
    this.userService.getAll().subscribe(users => { this.users = users; });
  }

}
