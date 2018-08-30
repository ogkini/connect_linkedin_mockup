import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { UserNavBarComponent } from './../user-nav-bar/user-nav-bar.component';

@Component({
  selector: 'app-welcome-user',
  templateUrl: './welcome-user.component.html',
  styleUrls: ['./welcome-user.component.scss']
})
export class WelcomeUserComponent implements OnInit {

  title = 'Welcome User';

  public constructor(private titleService: Title ) {
    this.titleService.setTitle( this.title );
  }

  ngOnInit() {
  }

}
