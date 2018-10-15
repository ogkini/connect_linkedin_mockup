import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { User } from '../../../_models';
import {AlertService, UsersCheckBoxService, ConnectionConfigService, UserService} from '../../../_services';


@Component({
  selector: 'app-home-admin',
  templateUrl: './home-admin.component.html',
  styleUrls: ['./home-admin.component.scss']
})
export class HomeAdminComponent implements OnInit {

  title = 'Home';
  signedInUser: User;
  users: User[] = [];
  goingToExtract = false;
  public profilePhotosEndpoint: string;

  public constructor(
      private titleService: Title,
      private userService: UserService,
      private usersCheckBoxService: UsersCheckBoxService,
      private alertService: AlertService,
      private connConfig: ConnectionConfigService
  ) {
    this.titleService.setTitle( this.title );
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));
    //console.debug("SignedIn user-id: " + this.signedInUser.id);
    this.profilePhotosEndpoint = this.connConfig.usersEndpoint;
    this.getAllUsers();
  }

  ngOnInit() {
  }

  private getAllUsers() {
    this.userService.getAll().subscribe(users => { this.users = users; });
  }

  public areAllSelected() {
    return ( this.usersCheckBoxService.selectedAllUsers );
  }

  public checkToExtract() {
    this.goingToExtract = true;
  }

  public areAnySelectedUsers() {
    if ( this.usersCheckBoxService.getUserIDsListLength() == 0 ) {
      //console.debug("No userIDs have been added yet!");
      this.alertService.warning("You haven't selected any users yet!");
      this.goingToExtract = false;
      return false;
    }
    else
      return true;
  }

  public extractXMLData() { // Extract IDs from users:
    document.getElementById("extractXML").click()​​​
    this.goingToExtract = false;  // Reset to avoid to auto-click on any checkbox-change.
  }

  public getUsersIDsQuery() {
    let usersIDsQuery: string = "";
    let firstPar = true;
    for ( let id of this.usersCheckBoxService.getUserIDsList() ) {
      if ( firstPar ) {
        usersIDsQuery += "?usersIDs=" + id;
        firstPar = false;
      }
      else
        usersIDsQuery += "&usersIDs=" + id;
    }

    return usersIDsQuery;
  }

  public toggleAllUsers()
  {
    if ( this.usersCheckBoxService.selectedAllUsers ) { // If we already have selected all users, then on this click we have to deselect them.
      //console.debug("All users were found selected, going to deselect them all.");
      this.usersCheckBoxService.clearUserIDsList();
      this.usersCheckBoxService.selectedAllUsers = false;
    }
    else {
      //console.debug("Going to select all users.");
      if ( this.usersCheckBoxService.getUserIDsListLength() > 0 ) {
        // If there were any users individually-selected before, clear the list and add all the users, we don't want duplicates.
        this.usersCheckBoxService.clearUserIDsList();
      }

      for ( let user of this.users ) {
        this.usersCheckBoxService.getUserIDsList().push(user.id.toString())
      }
      this.usersCheckBoxService.selectedAllUsers = true;
      this.alertService.clear();  // Avoid showing that no users are selected (in case the admin clicked to extract before selection).
    }
  }

  isUserSelected(userId: number) {
    return ( this.usersCheckBoxService.getUserIDsList().find(id => id === userId.toString()) != undefined )
  }

  public toggleSpecificUser(userId: number)
  {
    let idIndex: number;
    if ( this.usersCheckBoxService.selectedAllUsers ) { // If all users are selected.. we know that this ine is too, so go and deselect it directly (faster than checking if this exists in the list).
      //console.debug("All users were found selected when user with ID=" + userId.toString() + " was selected so it will be removed.");
      this.removeUserId(userId);
    }
    else if ( (idIndex = this.usersCheckBoxService.getUserIDsList().findIndex(id => id === userId.toString())) != -1 ) { // If this userId exists in the list
      //console.debug("User with ID=" + userId.toString() + " was found in the list and will be removed.");
      this.removeUserId(userId, idIndex);
    }
    else {  // It doesn't exist, let's push it in the list.
      //console.debug("User with ID=" + userId.toString() + " will be pushed in the list");
      this.usersCheckBoxService.getUserIDsList().push(userId.toString());
      this.alertService.clear();  // Avoid showing that no users are selected (in case the admin clicked to extract before selection).

      // If they are manually reached all of them to be selected, then update service's variable.
      if ( this.usersCheckBoxService.getUserIDsListLength() == this.users.length )
        this.usersCheckBoxService.selectedAllUsers = true;
    }
  }

  public removeUserId(userId: number, idIndex?: number)
  {
    let index: number;

    // Use the idIndex from previous check -if possible- to avoid re-searching for it.
    if ( idIndex != undefined )
      index = idIndex;
    else
      index = this.usersCheckBoxService.getUserIDsList().indexOf(userId.toString(), 0);
    if ( index > -1 ) {
      this.usersCheckBoxService.getUserIDsList().splice(index, 1);

      // If they are manually reached not all of them to be selected, then update service's variable.
      if ( this.usersCheckBoxService.getUserIDsListLength() < this.users.length )
        this.usersCheckBoxService.selectedAllUsers = false;
    }
  }

}
