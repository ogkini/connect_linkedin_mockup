import { Component, OnInit } from '@angular/core';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

@Component({
  selector: 'app-professional-nav-bar',
  templateUrl: './professional-nav-bar.component.html',
  styleUrls: ['./professional-nav-bar.component.scss']
})

@NgModule({
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})


export class ProfessionalNavBarComponent implements OnInit {

  title: ""

  constructor() { }

  ngOnInit() {
  }

}
