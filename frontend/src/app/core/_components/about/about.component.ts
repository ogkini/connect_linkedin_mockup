import {Component, OnInit} from '@angular/core';
import { Title } from "@angular/platform-browser";
import { Location } from '@angular/common';
import {User} from "../../_models";


@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.scss']
})
export class AboutComponent implements OnInit {

  title = 'About';
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
