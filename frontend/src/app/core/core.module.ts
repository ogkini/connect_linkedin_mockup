import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './_helpers/index';

import { CoreRoutingModule } from './core-routing.module';
import { WelcomeComponent } from './_components/welcome/welcome.component';
import { FooterComponent } from './_components/footer/footer.component';
import { SignInComponent } from './_components/sign-in/sign-in.component';
import { SignUpComponent } from './_components/sign-up/sign-up.component';
import { HomeAdminComponent } from './_components/admin/home-admin/home-admin.component';
import { HomeUserComponent } from './_components/user/home-user/home-user.component';

import { MainNavBarComponent } from './_components/main-nav-bar/main-nav-bar.component';
import { AdminNavBarComponent } from './_components/admin/admin-nav-bar/admin-nav-bar.component';
import { UserNavBarComponent } from './_components/user/user-nav-bar/user-nav-bar.component';

import { FileUploaderService } from './_services/file-uploader/file-uploader.service';

import { AuthGuard, RoleGuard } from './_guards/index';
import { AlertComponent } from './_directives/alert/alert.component';
import { AlertService, AuthenticationService, ConnectionConfigService } from './_services/index';
import { UserService, ExperienceService, EducationService } from './_services/index';
import { PasswordConfirmValidatorDirective } from './_directives/validators/password-confirm-validator.directive';
import { TextValidatorDirective } from "./_directives/validators/text_validator.directive";

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
    PasswordConfirmValidatorDirective,
    TextValidatorDirective
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
    ExperienceService,
    EducationService,
    ConnectionConfigService,
    FileUploaderService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true
    }
  ],
  schemas: [ NO_ERRORS_SCHEMA ]
})
export class CoreModule { }
