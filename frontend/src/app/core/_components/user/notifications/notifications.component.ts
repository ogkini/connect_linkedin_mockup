import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

import {User, Notification, Post} from '../../../_models';
import {
  NotificationService,
  UserService,
  ConnectionConfigService,
  AuthenticationService
} from '../../../_services';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit {

  title = 'Notifications';
  public signedInUser: User;
  notifications: Notification[] = [];
  userId: number;
  public postToShow: Post;
  public profilePhotosEndpoint: string;

  constructor(
      private titleService: Title,
      private notificationService: NotificationService,
      private userService: UserService,
      private connConfig: ConnectionConfigService,
      private route: ActivatedRoute,
      private authenticationService: AuthenticationService
  ) {
    this.titleService.setTitle(this.title);
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));
    this.route.params.subscribe(params => {
      this.userId = +params['id'];
      this.authenticationService.forbidUnauthorizedAccess(this.signedInUser, this.userId);
    });
    this.profilePhotosEndpoint = this.connConfig.usersEndpoint;
  }

  ngOnInit() {
    this.getNotifications(this.signedInUser.id);
  }

  private getNotifications(id: number) {
    this.notificationService.getAll(id).subscribe(notifications => { this.notifications = notifications; });
  }

  public setPostToShow(post: Post) {
    this.postToShow = post;
  }

}
