import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FlightInstanceComponent } from './flight-instance.component';

describe('FlightInstanceComponent', () => {
  let component: FlightInstanceComponent;
  let fixture: ComponentFixture<FlightInstanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FlightInstanceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FlightInstanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
