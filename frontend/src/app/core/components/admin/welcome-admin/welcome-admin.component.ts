import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { AdminNavBarComponent } from './../admin-nav-bar/admin-nav-bar.component';

@Component({
  selector: 'app-welcome-admin',
  templateUrl: './welcome-admin.component.html',
  styleUrls: ['./welcome-admin.component.scss']
})
export class WelcomeAdminComponent implements OnInit {

  title = 'Welcome admin';

  public constructor(private titleService: Title ) {
    this.titleService.setTitle( this.title );
  }

  ngOnInit() {
  }

}
