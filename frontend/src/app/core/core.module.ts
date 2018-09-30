import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { CoreRoutingModule } from './core-routing.module';
import { WelcomeComponent } from './_components/welcome/welcome.component';
import { FooterComponent } from './_components/footer/footer.component';
import { SignInComponent } from './_components/sign-in/sign-in.component';
import { SignUpComponent } from './_components/sign-up/sign-up.component';
import { HomeAdminComponent } from './_components/admin/home-admin/home-admin.component';
import { HomeUserComponent } from './_components/user/home-user/home-user.component';
import { NetworkComponent } from './_components/user/network/network.component';
import { MessagesComponent } from './_components/user/messages/messages.component';
import { NotificationsComponent } from './_components/user/notifications/notifications.component';
import { PageNotFoundComponent } from "./_components/navigation-error/page-not-found/page-not-found.component";

import { MainNavBarComponent } from './_components/main-nav-bar/main-nav-bar.component';
import { AdminNavBarComponent } from './_components/admin/admin-nav-bar/admin-nav-bar.component';
import { UserNavBarComponent } from './_components/user/user-nav-bar/user-nav-bar.component';

import { AuthGuard, RoleGuard } from './_guards';
import { AlertComponent } from './_directives/alert/alert.component';
import {
  AlertService,
  AuthenticationService,
  ConnectionConfigService,
  DataService,
  OccupationService,
  SkillService
} from './_services';
import { UserService, ExperienceService, EducationService, RelationshipService } from './_services';
import { PostService, LikeService, CommentService, ConversationService, NotificationService } from './_services';
import { FileUploaderService, UsersInteractionService } from './_services';
import { PasswordConfirmValidatorDirective } from './_directives/validators/password-confirm-validator.directive';
import { TextValidatorDirective } from "./_directives/validators/text_validator.directive";
import { DatePeriodValidatorDirective } from "./_directives/validators/date-period-validator.directive";
import { DateService } from "./_services/date.service";
import { UserInfoComponent } from "./_components/user/user-info/user-info.component";
import { UserSettingsComponent } from "./_components/user/user-settings/user-settings.component";
import { UserProfileComponent } from "./_components/user/user-profile/user-profile.component";
import { httpInterceptorProviders } from "./_interceptors";
import { AboutComponent } from "./_components/about/about.component";
import { ForbiddenComponent } from "./_components/navigation-error/forbidden/forbidden.component";
import {JobOffersComponent} from "./_components/user/job-offers/job-offers.component";
import {JobOfferService} from "./_services/job-offer.service";
import {JobApplyService} from "./_services/job-apply.service";

@NgModule({
  imports: [
    CommonModule,
    CoreRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MDBBootstrapModule.forRoot()
  ],
  declarations: [
    FooterComponent,
    WelcomeComponent,
    SignInComponent,
    SignUpComponent,
    HomeAdminComponent,
    HomeUserComponent,
    MainNavBarComponent,
    AdminNavBarComponent,
    UserNavBarComponent,
    AlertComponent,
    NetworkComponent,
    MessagesComponent,
    AboutComponent,
    PageNotFoundComponent,
    ForbiddenComponent,
    UserInfoComponent,
    UserSettingsComponent,
    UserProfileComponent,
    JobOffersComponent,
    PasswordConfirmValidatorDirective,
    TextValidatorDirective,
    DatePeriodValidatorDirective,
    NotificationsComponent,
  ],
  exports: [
    FooterComponent,
    WelcomeComponent
  ],
  providers: [
    AuthGuard,
    RoleGuard,
    AlertService,
    AuthenticationService,
    UserService,
    OccupationService,
    ExperienceService,
    EducationService,
    SkillService,
    RelationshipService,
    PostService,
    LikeService,
    CommentService,
    JobOfferService,
    JobApplyService,
    ConversationService,
    ConnectionConfigService,
    NotificationService,
    DataService,
    FileUploaderService,
    DateService,
    UsersInteractionService,
    httpInterceptorProviders
  ],
  schemas: [ NO_ERRORS_SCHEMA ]
})
export class CoreModule { }
