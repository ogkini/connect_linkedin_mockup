import { BrowserModule } from '@angular/platform-browser';
import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { MDBBootstrapModule } from 'angular-bootstrap-md';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';


@NgModule({
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    MDBBootstrapModule.forRoot()
  ],
  declarations: [
    AppComponent
  ],
  schemas: [ NO_ERRORS_SCHEMA ],
  providers: [ ],
  bootstrap: [ AppComponent ]
})
export class AppModule {}
