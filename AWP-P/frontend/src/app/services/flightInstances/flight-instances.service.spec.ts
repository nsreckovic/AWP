import { TestBed } from '@angular/core/testing';

import { FlightInstancesService } from './flight-instances.service';

describe('FlightInstancesService', () => {
  let service: FlightInstancesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FlightInstancesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
