import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from 'src/app/app.constants';
import JsonMessage from 'src/app/models/JsonMessage.models';
import ReservationFilter from 'src/app/models/reservation/reservationFilter.model';
import ReservationRequestDto from 'src/app/models/reservation/reservationRequestDto.model';
import ReservationResponseDto from 'src/app/models/reservation/reservationResponseDto.model';

@Injectable({
  providedIn: 'root'
})
export class ReservationsService {
  private readonly reservationsUrl = API_URL + '/reservations';

  constructor(private httpClient: HttpClient) { }

  newReservation(reservation: ReservationRequestDto) {
    return this.httpClient.post<ReservationResponseDto>(`${this.reservationsUrl}`, reservation);
  }

  updateReservation(reservation: ReservationRequestDto) {
    return this.httpClient.put<ReservationResponseDto>(`${this.reservationsUrl}`, reservation);
  }

  getAllReservations(filter: ReservationFilter) {
    return this.httpClient.post<ReservationResponseDto[]>(`${this.reservationsUrl}/all`, filter);
  }

  getReservationById(id: number) {
    return this.httpClient.get<ReservationResponseDto>(`${this.reservationsUrl}/${id}`);
  }

  deleteReservationById(id: number) {
    return this.httpClient.delete<JsonMessage>(`${this.reservationsUrl}/${id}`);
  }
}
