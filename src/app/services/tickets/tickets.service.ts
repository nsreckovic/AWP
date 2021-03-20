import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from 'src/app/app.constants';
import JsonMessage from 'src/app/models/JsonMessage.models';
import TicketFilter from 'src/app/models/ticket/ticketFilter.model';
import TicketRequestDto from 'src/app/models/ticket/ticketRequestDto.model';
import TicketResponseDto from 'src/app/models/ticket/ticketResponseDto.model';

@Injectable({
  providedIn: 'root'
})
export class TicketsService {
  private readonly ticketsUrl = API_URL + '/tickets';

  constructor(private httpClient: HttpClient) { }

  newTicket(ticketRequest: TicketRequestDto) {
    return this.httpClient.post<TicketResponseDto[]>(`${this.ticketsUrl}`, ticketRequest);
  }

  updateTicket(ticket: TicketRequestDto) {
    return this.httpClient.put<TicketResponseDto>(`${this.ticketsUrl}`, ticket);
  }

  getAllTickets(filter: TicketFilter) {
    return this.httpClient.post<TicketResponseDto[]>(`${this.ticketsUrl}/all`, filter);
  }

  getAllAvailableFromTicketsByFilter(filter: TicketFilter) {
    return this.httpClient.post<TicketResponseDto[]>(`${this.ticketsUrl}/from`, filter);
  }

  getAllAvailableReturnTicketsByFilter(filter: TicketFilter) {
    return this.httpClient.post<TicketResponseDto[]>(`${this.ticketsUrl}/return`, filter);
  }

  getTicketById(id: number) {
    return this.httpClient.get<TicketResponseDto>(`${this.ticketsUrl}/${id}`);
  }

  deleteTicketById(id: number) {
    return this.httpClient.delete<JsonMessage>(`${this.ticketsUrl}/${id}`);
  }
}
