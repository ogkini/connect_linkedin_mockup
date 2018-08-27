import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MDBBootstrapModule } from 'angular-bootstrap-md';

import { ReactiveFormsModule } from '@angular/forms';

import { CoreRoutingModule } from './core-routing.module';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { FooterComponent } from './components/footer/footer.component';
import { SignInComponent } from './components/sign-in/sign-in.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { WelcomeAdminComponent } from './components/admin/welcome-admin/welcome-admin.component';
import { WelcomeProfessionalComponent } from './components/professional/welcome-professional/welcome-professional.component';

import { MainNavBarComponent } from './components/main-nav-bar/main-nav-bar.component';
import { AdminNavBarComponent } from './components/admin/admin-nav-bar/admin-nav-bar.component';
import { ProfessionalNavBarComponent } from './components/professional/professional-nav-bar/professional-nav-bar.component';


@NgModule({
  imports: [
    CommonModule,
    CoreRoutingModule,
    ReactiveFormsModule,
    MDBBootstrapModule.forRoot()
  ],
  declarations: [
    FooterComponent,
    WelcomeComponent,
    SignInComponent,
    SignUpComponent,
    WelcomeAdminComponent,
    WelcomeProfessionalComponent,
    MainNavBarComponent,
    AdminNavBarComponent,
    ProfessionalNavBarComponent
  ],
  exports: [
    FooterComponent,
    WelcomeComponent
  ],
  schemas: [ NO_ERRORS_SCHEMA ]
})
export class CoreModule { }
