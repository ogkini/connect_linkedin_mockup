import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Title } from "@angular/platform-browser";
import { first } from "rxjs/operators";

import { User, Network } from '../../../_models/index';
import { RelationshipService, ConnectionConfigService, AlertService, DataService } from '../../../_services/index';

@Component({
  selector: 'app-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.scss']
})
export class NetworkComponent implements OnInit {

  title = 'My Network';
  currentUser: User;
  network: Network;
  message: string;
  public profilePhotosEndpoint: string;

  showReceived = true;
  showSent = false;
  showConnections = false;

  public constructor(
    private titleService: Title,
    private relationshipService: RelationshipService,
    private connConfig: ConnectionConfigService,
    private alertService: AlertService,
    private dataService: DataService,
    private route: ActivatedRoute
  ) {
    this.titleService.setTitle(this.title);
    this.currentUser = JSON.parse(localStorage.getItem('currentUser'));
    this.profilePhotosEndpoint = this.connConfig.usersEndpoint;
  }

  ngOnInit() {
    this.getNetwork(this.currentUser.id);
    this.dataService.currentMessage.subscribe(message => this.message = message);
    this.dataService.changeMessage('');
  }

  private getNetwork(userId: number) {
    this.relationshipService.getAll(userId).subscribe(network => { this.network = network; });
  }

  // Declines a friend request
  decline(id: number) {
    this.relationshipService.decline(id)
      .pipe(first())
      .subscribe(response => {
          // Remove the request from the array
          this.network.receivedRequests = this.network.receivedRequests.filter(item => item.id !== id);
        }, error => {
          this.alertService.error(error.error.message);
        }
      );
}

  // Accepts a friend request
  accept(id: number) {
    this.relationshipService.accept(id)
      .pipe(first())
      .subscribe(response => {
          // Move the request from the requests array to the connections
          let newConnection = this.network.receivedRequests.find(item => item.id === id);
          this.network.connections.push(newConnection.sender);
          this.network.receivedRequests = this.network.receivedRequests.filter(item => item.id !== id);
        }, error => {
          this.alertService.error(error.error.message);
        }
      );
}

  // To manage the right side view based on active button
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
