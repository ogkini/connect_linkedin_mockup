import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { AdminNavBarComponent } from './../admin-nav-bar/admin-nav-bar.component';

@Component({
  selector: 'app-home-admin',
  templateUrl: './home-admin.component.html',
  styleUrls: ['./home-admin.component.scss']
})
export class HomeAdminComponent implements OnInit {

  title = 'Welcome to Connect!';

  public constructor(private titleService: Title ) {
    this.titleService.setTitle( this.title );
  }

  ngOnInit() {
  }

}
