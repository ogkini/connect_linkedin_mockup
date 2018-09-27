import {Component, OnInit} from '@angular/core';
import {Title} from "@angular/platform-browser";
import {Location} from "@angular/common";
import {User} from "../../../_models";

@Component({
  selector: 'app-forbidden',
  templateUrl: './forbidden.component.html',
  styleUrls: ['./forbidden.component.scss']
})
export class ForbiddenComponent implements OnInit {

  title = 'Forbidden page!';
  signedInUser: User;

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
    window.history.go(-2);  // -1 to go to the forbidden-page and -1 to go to the page before the forbidden-one.
  }

}
