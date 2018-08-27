import { Component, OnInit } from '@angular/core';
import { ProfessionalNavBarComponent } from './../professional-nav-bar/professional-nav-bar.component';

@Component({
  selector: 'app-welcome-professional',
  templateUrl: './welcome-professional.component.html',
  styleUrls: ['./welcome-professional.component.scss']
})
export class WelcomeProfessionalComponent implements OnInit {

  title = 'Welcome Professional';

  constructor() { }

  ngOnInit() {
  }

}
