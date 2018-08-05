import { CommonModule } from '@angular/common';
import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import { RouterModule } from '@angular/router';

import { HeaderComponent, FooterComponent } from './layout/index';
import { SigninComponent } from './layout/signin/signin.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    MDBBootstrapModule.forRoot()
  ],
  schemas: [ NO_ERRORS_SCHEMA ],
  declarations: [
    HeaderComponent,
    FooterComponent,
    SigninComponent
  ],
  exports: [
    CommonModule,
    RouterModule,
    HeaderComponent,
    FooterComponent
  ]
})
export class SharedModule {}
