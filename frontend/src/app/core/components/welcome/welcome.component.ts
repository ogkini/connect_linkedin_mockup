import { Component, OnInit } from '@angular/core';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
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

  title = 'Welcome';

  constructor() { }

  ngOnInit() {
  }

}
