import { Component, OnInit } from '@angular/core';
import { UserNavBarComponent } from './../user-nav-bar/user-nav-bar.component';

@Component({
  selector: 'app-welcome-user',
  templateUrl: './welcome-user.component.html',
  styleUrls: ['./welcome-user.component.scss']
})
export class WelcomeUserComponent implements OnInit {

  title = 'Welcome User';

  constructor() { }

  ngOnInit() {
  }

}
