import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

import { User, Notification } from '../../../_models';
import { NotificationService, UserService, ConnectionConfigService } from '../../../_services';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit {

  title = 'Notifications';
  signedInUser: User;
  notifications: Notification[] = [];

  public profilePhotosEndpoint: string;

  constructor(
      private titleService: Title,
      private notificationService: NotificationService,
      private userService: UserService,
      private connConfig: ConnectionConfigService
  ) {
    this.titleService.setTitle(this.title);
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));

    this.profilePhotosEndpoint = this.connConfig.usersEndpoint;
  }

  ngOnInit() {
    this.getNotifications(this.signedInUser.id);
  }

  private getNotifications(id: number) {
    this.notificationService.getAll(id).subscribe(notifications => { this.notifications = notifications; });
  }

}
