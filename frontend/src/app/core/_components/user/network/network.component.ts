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

  showReceived = true;
  showSent = false;
  showConnections = false;

  public constructor(
    private titleService: Title,
    private route: ActivatedRoute
  ) {
    this.titleService.setTitle(this.title);
    this.route.params.subscribe( params => {
      this.userId = +params['id'];
      console.debug("UserID coming from url-parameters is:", this.userId);
    })
  }

  ngOnInit() {}

  showReceivedTrue() {
    this.showReceived = true;
    this.showSent = false;
    this.showConnections = false;
  }

  showSentTrue() {
    this.showReceived = false;
    this.showSent = true;
    this.showConnections = false;
  }

  showConnectionsTrue() {
    this.showReceived = false;
    this.showSent = false;
    this.showConnections = true;
  }

}
