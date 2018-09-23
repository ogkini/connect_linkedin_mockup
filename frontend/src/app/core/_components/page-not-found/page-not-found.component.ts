import {Component, CUSTOM_ELEMENTS_SCHEMA, NgModule, OnInit} from '@angular/core';
import {Title} from "@angular/platform-browser";
import { MainNavBarComponent } from './../main-nav-bar/main-nav-bar.component';


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

  public constructor(private titleService: Title ) {
    this.titleService.setTitle( this.title );
  }

  ngOnInit() {
  }

}
