import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from 'src/app/app.constants';
import FlightInstanceRequestDto from 'src/app/models/flightInstance/flightInstanceRequestDto.model';
import FlightInstanceResponseDto from 'src/app/models/flightInstance/flightInstanceResponseDto.model';
import JsonMessage from 'src/app/models/JsonMessage.models';

@Injectable({
  providedIn: 'root'
})
export class FlightInstancesService {
  private readonly flightInstancesUrl = API_URL + '/flightInstances';

  constructor(private httpClient: HttpClient) { }

  newFlightInstance(flightInstance: FlightInstanceRequestDto) {
    return this.httpClient.post<FlightInstanceResponseDto>(`${this.flightInstancesUrl}`, flightInstance);
  }

  updateFlightInstance(flightInstance: FlightInstanceRequestDto) {
    return this.httpClient.put<FlightInstanceResponseDto>(`${this.flightInstancesUrl}`, flightInstance);
  }

  getAllFlightInstances() {
    return this.httpClient.get<FlightInstanceResponseDto[]>(`${this.flightInstancesUrl}/all`);
  }

  getFlightInstanceById(id: string) {
    return this.httpClient.get<FlightInstanceResponseDto>(`${this.flightInstancesUrl}/${id}`);
  }

  deleteFlightInstanceById(id: string) {
    return this.httpClient.delete<JsonMessage>(`${this.flightInstancesUrl}/${id}`);
  }
}
