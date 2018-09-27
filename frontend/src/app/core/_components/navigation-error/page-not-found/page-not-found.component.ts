import {Component, CUSTOM_ELEMENTS_SCHEMA, Input, NgModule, OnInit} from '@angular/core';
import { Title } from "@angular/platform-browser";
import { Location } from '@angular/common';
import {User} from "../../../_models/index";


@Component({
  selector: 'app-page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrls: ['./page-not-found.component.scss']
})

@NgModule({
  providers: [],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})

export class PageNotFoundComponent implements OnInit {

  title = 'Page not found!';
  public signedInUser: User;

  public constructor(
    private titleService: Title,
    private location: Location
  ) {
    this.titleService.setTitle( this.title );
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));
  }

  ngOnInit() {
  }

  backClicked() {
    this.location.back();
  }

}
