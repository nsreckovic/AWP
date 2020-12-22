import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from 'src/app/app.constants';
import Flight from 'src/app/models/flight/flight.model';
import FlightRequestDto from 'src/app/models/flight/flightRequestDto.model';

@Injectable({
  providedIn: 'root'
})
export class FlightsService {
  private readonly flightsUrl = API_URL + '/flights';

  constructor(private httpClient: HttpClient) { }

  newFlight(flight: FlightRequestDto) {
    return this.httpClient.post<Flight>(`${this.flightsUrl}`, flight);
  }

  updateFlight(flight: FlightRequestDto) {
    return this.httpClient.put<Flight>(`${this.flightsUrl}`, flight);
  }

  getAllFlights() {
    return this.httpClient.get<Flight[]>(`${this.flightsUrl}/all`);
  }

  getFlightById(id: string) {
    return this.httpClient.get<Flight>(`${this.flightsUrl}/${id}`);
  }

  deleteFlightById(id: string) {
    return this.httpClient.delete(`${this.flightsUrl}/${id}`);
  }
}
