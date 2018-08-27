import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WelcomeProfessionalComponent } from './welcome-professional.component';

describe('WelcomeProfessionalComponent', () => {
  let component: WelcomeProfessionalComponent;
  let fixture: ComponentFixture<WelcomeProfessionalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WelcomeProfessionalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WelcomeProfessionalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
