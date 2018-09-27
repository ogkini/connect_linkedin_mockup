import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

import { User } from '../../../_models';
import { ConnectionConfigService, UserService } from '../../../_services';
import 'rxjs/Rx';

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

  public extractXMLData() {
    // Extract IDs from users:
    let usersIDs: string[] = [];

    for ( let user of this.users ) {
      usersIDs.push(user.id.toString());
    }

    this.userService.getXMLdataForUsersIDs(usersIDs)
      .subscribe(
      data => {
        this.downloadFile(data);
      },
        error => { console.error(error); }
    )
  }

  downloadFile(data: Response) {
    console.debug("Inside \"downloadFile()\"");
    var blob = new Blob([data], { type: 'application/xml' });
    var url= window.URL.createObjectURL(blob);
    console.debug("BLOB-url is: " + url);
    window.open(url);
  }

}
