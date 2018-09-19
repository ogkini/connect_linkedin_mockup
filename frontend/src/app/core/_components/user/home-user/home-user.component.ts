import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

import { User } from '../../../_models/index';
import { UserService, AlertService } from '../../../_services/index';
import { UserNavBarComponent } from './../user-nav-bar/user-nav-bar.component';

@Component({
  selector: 'app-home-user',
  templateUrl: './home-user.component.html',
  styleUrls: ['./home-user.component.scss']
})
export class HomeUserComponent implements OnInit {

  title = 'Home';
  currentUser: User;
  user: User;

  public constructor(
      private titleService: Title,
      private userService: UserService,
      private alertService: AlertService) {
    this.titleService.setTitle(this.title);
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
  }

  ngOnInit() {
    this.getUserById(this.currentUser.id);
  }

  private getUserById(id: number) {
    this.userService.getById(id).subscribe(user => { this.user = user; });
      // .subscribe((response: any) => {
      //     this.user = response.content;
      //   }, error => {
      //     this.alertService.error(error.error.message);
      //     console.log(error);
      //   }
      // );
  }

}
