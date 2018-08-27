import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfessionalNavBarComponent } from './professional-nav-bar.component';

describe('ProfessionalNavBarComponent', () => {
  let component: ProfessionalNavBarComponent;
  let fixture: ComponentFixture<ProfessionalNavBarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProfessionalNavBarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfessionalNavBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
