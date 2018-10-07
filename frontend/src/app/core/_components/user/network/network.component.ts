import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Title } from "@angular/platform-browser";
import { first } from "rxjs/operators";

import { User, Network } from '../../../_models';
import {
  RelationshipService,
  ConnectionConfigService,
  AlertService,
  DataService,
  UserService,
  UsersInteractionService,
  ConversationService
} from '../../../_services';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-network',
  templateUrl: './network.component.html',
  styleUrls: ['./network.component.scss']
})
export class NetworkComponent implements OnInit {

  title = 'My Network';
  signedInUser: User;
  userId: number;
  network: Network;
  searchForm: FormGroup;
  message: string;
  public profilePhotosEndpoint: string;
  public usersFromSearch: User[];
  submitted = false;
  isAdmin = false;

  showConnections = true; // First thing to see when the user visits "Network".
  showReceived = false;
  showSent = false;
  showSearchBar = false;
  showSearchResults = false;

  public constructor(
    private titleService: Title,
    private userService: UserService,
    private relationshipService: RelationshipService,
    private connConfig: ConnectionConfigService,
    private alertService: AlertService,
    private dataService: DataService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private usersInteraction: UsersInteractionService,
    private conversationService: ConversationService
  ) {
    this.titleService.setTitle(this.title);
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));

    this.route.params.subscribe(params => {
      this.userId = +params['id'];
      this.getNetwork(this.userId);
    });

    this.profilePhotosEndpoint = this.connConfig.usersEndpoint;

    // If we have another user browsing this one's network.. control what the other see.
    if (this.signedInUser.id != this.userId) {
      this.showConnections = true;  // Allow the admin to inspect user's connections.
      this.showReceived = false;  // Reset
      if (this.signedInUser.email == 'admin@mail.com') {
        this.isAdmin = true;
      }
    }
  }

  ngOnInit() {
    this.dataService.currentMessage.subscribe(message => this.message = message);
    this.dataService.changeMessage('');

    // Use FormBuilder to create a form group
    this.searchForm = this.formBuilder.group({
      searchTerm: ['', [Validators.required]]
    });
  }

  // Convenience getter for easy access to form fields
  get form() {
    return this.searchForm.controls;
  }

  private getNetwork(userId: number) {
    this.relationshipService.getAll(userId).subscribe(network => { this.network = network; });
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

  // To manage the right side view based on active button
  showReceivedTrue() {
    this.showSearchBar = false;
    this.showSearchResults = false;
    this.showReceived = true;
    this.showSent = false;
    this.showConnections = false;
  }

  showSentTrue() {
    this.showSearchBar = false;
    this.showSearchResults = false;
    this.showReceived = false;
    this.showSent = true;
    this.showConnections = false;
  }

  showConnectionsTrue() {
    this.showSearchBar = false;
    this.showSearchResults = false;
    this.showReceived = false;
    this.showSent = false;
    this.showConnections = true;
  }

  showSearchBarTrue() {
    this.showSearchBar = true;
    this.showSearchResults = false;
    this.showConnections = false;
    this.showReceived = false;
    this.showSent = false;
  }

  onSearchSubmit() {
    this.submitted = true;
    this.alertService.clear();

    let searchTerm: string;

    if (this.searchForm.invalid) {
      return null;
    } else {
      searchTerm = this.form.searchTerm.value;
    }

    // Get results from backend
    this.userService.getAllRelatedToSearchTerm(searchTerm).subscribe(
      results => {
        this.usersFromSearch = results;
        if (this.usersFromSearch == null || this.usersFromSearch.length == 0) {
          this.alertService.warning("No users found matching: \"" + searchTerm + "\"");
        } else {
          this.showSearchResults = true;
        }
      },
      error => {
        console.error(error);
      }
    )
  }

  sendFriendRequest(userId: number) {
    this.usersInteraction.sendFriendRequest(userId);
  }

  startConversation(userId: number) {}

}
