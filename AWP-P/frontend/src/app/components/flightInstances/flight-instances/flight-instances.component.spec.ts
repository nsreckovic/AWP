import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FlightInstancesComponent } from './flight-instances.component';

describe('FlightInstancesComponent', () => {
  let component: FlightInstancesComponent;
  let fixture: ComponentFixture<FlightInstancesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FlightInstancesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FlightInstancesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
