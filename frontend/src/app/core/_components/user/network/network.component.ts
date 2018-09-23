import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Title } from "@angular/platform-browser";


@Component({
  selector: 'app-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.scss']
})
export class NetworkComponent implements OnInit {

  title = 'My Network';
  public userId: number;

  public constructor(
    private titleService: Title,
    private route: ActivatedRoute
  ) {
    console.debug("Inside NetworkComponent!");

    this.titleService.setTitle( this.title );
    this.route.params.subscribe( params => {
      this.userId = +params['id']; // (+) converts string 'id' to a number
      console.debug("UserID coming from url-parameters is:", this.userId);
    })
  }

  ngOnInit() {
  }

}
