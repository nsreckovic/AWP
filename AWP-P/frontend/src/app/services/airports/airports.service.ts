import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from 'src/app/app.constants';
import Airport from 'src/app/models/airport/airport.model';
import JsonMessage from 'src/app/models/JsonMessage.models';

@Injectable({
  providedIn: 'root',
})
export class AirportsService {
  private readonly airportsUrl = API_URL + '/airports';

  constructor(private httpClient: HttpClient) {}

  newAirport(airport: Airport) {
    return this.httpClient.post<Airport>(`${this.airportsUrl}`, airport);
  }

  updateAirport(airport: Airport) {
    return this.httpClient.put<Airport>(`${this.airportsUrl}`, airport);
  }

  getAllAirports() {
    return this.httpClient.get<Airport[]>(`${this.airportsUrl}/all`);
  }

  getAirportById(id: number) {
    return this.httpClient.get<Airport>(`${this.airportsUrl}/${id}`);
  }

  deleteAirportById(id: number) {
    return this.httpClient.delete<JsonMessage>(`${this.airportsUrl}/${id}`);
  }
}
