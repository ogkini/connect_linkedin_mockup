import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { UserNavBarComponent } from './../user-nav-bar/user-nav-bar.component';

@Component({
  selector: 'app-home-user',
  templateUrl: './home-user.component.html',
  styleUrls: ['./home-user.component.scss']
})
export class HomeUserComponent implements OnInit {

  title = 'Welcome to Connect!';

  public constructor(private titleService: Title ) {
    this.titleService.setTitle( this.title );
  }

  ngOnInit() {
  }

}
