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
    //console.debug("SignedIn user-id: " + this.signedInUser.id);
    this.profilePhotosEndpoint = this.connConfig.usersEndpoint;
  }

  ngOnInit() {
    this.getAllUsers();
  }

  private getAllUsers() {
    this.userService.getAll().subscribe(users => { this.users = users; });
  }

  public extractXMLData() { // Extract IDs from users:

    let usersIDs: string = "";//new StringBuilder();

    let firstPar = true;
    for ( let user of this.users ) {
      if ( firstPar ) {
        usersIDs += "?usersIDs=" + user.id.toString();
        firstPar = false;
      }
      else
        usersIDs += "&usersIDs=" + user.id.toString();
    }

    return usersIDs;
  }

}
