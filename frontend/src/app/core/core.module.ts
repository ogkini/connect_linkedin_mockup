import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { CoreRoutingModule } from './core-routing.module';
import { WelcomeComponent } from './_components/welcome/welcome.component';
import { FooterComponent } from './_components/footer/footer.component';
import { SignInComponent } from './_components/sign-in/sign-in.component';
import { SignUpComponent } from './_components/sign-up/sign-up.component';
import { WelcomeAdminComponent } from './_components/admin/welcome-admin/welcome-admin.component';
import { WelcomeUserComponent } from './_components/user/welcome-user/welcome-user.component';

import { MainNavBarComponent } from './_components/main-nav-bar/main-nav-bar.component';
import { AdminNavBarComponent } from './_components/admin/admin-nav-bar/admin-nav-bar.component';
import { UserNavBarComponent } from './_components/user/user-nav-bar/user-nav-bar.component';

import { AlertComponent } from './_directives/alert/alert.component';
import { AlertService, AuthenticationService, UserService, ConnectionConfigService } from './_services/index';

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
    WelcomeAdminComponent,
    WelcomeUserComponent,
    MainNavBarComponent,
    AdminNavBarComponent,
    UserNavBarComponent,
    AlertComponent
  ],
  exports: [
    FooterComponent,
    WelcomeComponent
  ],
  providers: [
    AlertService,
    AuthenticationService,
    UserService,
    ConnectionConfigService
  ],
  schemas: [ NO_ERRORS_SCHEMA ]
})
export class CoreModule { }
