import { Component, OnInit, NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { MainNavBarComponent } from './../main-nav-bar/main-nav-bar.component';


@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})

@NgModule({
  providers: [],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})

export class WelcomeComponent implements OnInit {

  title = 'Connect';

  public constructor(private titleService: Title ) {
    this.titleService.setTitle( this.title );
  }

  ngOnInit() {
  }

}
