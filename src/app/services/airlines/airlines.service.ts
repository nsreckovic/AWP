import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { API_URL } from 'src/app/app.constants';
import Airline from 'src/app/models/airline/airline.model';

@Injectable({
  providedIn: 'root',
})
export class AirlinesService {
  private readonly airlinesUrl = API_URL + '/airlines';

  constructor(private httpClient: HttpClient) {}

  newAirline(airline: Airline) {
    return this.httpClient.post<Airline>(`${this.airlinesUrl}`, airline);
  }

  updateAirline(airline: Airline) {
    return this.httpClient.put<Airline>(`${this.airlinesUrl}`, airline);
  }

  getAllAirlines() {
    return this.httpClient.get<Airline[]>(`${this.airlinesUrl}/all`);
  }

  getAirlineById(id: number) {
    return this.httpClient.get<Airline>(`${this.airlinesUrl}/${id}`);
  }

  deleteAirlineById(id: number) {
    return this.httpClient.delete(`${this.airlinesUrl}/${id}`);
  }
}
