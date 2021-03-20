import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { ErrorComponent } from './components/error/error.component';
import { FooterComponent } from './components/navigation/footer/footer.component';
import { LoginComponent } from './components/users/login/login.component';
import { RegisterComponent } from './components/users/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { LogoutComponent } from './components/users/logout/logout.component';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpInterceptorService } from './services/http/http-interceptor.service';
import { UserComponent } from './components/users/user/user.component';
import { UsersComponent } from './components/users/users/users.component';
import { NavbarComponent } from './components/navigation/navbar/navbar.component';
import { EditUserComponent } from './components/users/edit-user/edit-user.component';
import { AirlinesComponent } from './components/airlines/airlines/airlines.component';
import { AirlineComponent } from './components/airlines/airline/airline.component';
import { AirportComponent } from './components/airports/airport/airport.component';
import { AirportsComponent } from './components/airports/airports/airports.component';
import { FlightComponent } from './components/flights/flight/flight.component';
import { FlightsComponent } from './components/flights/flights/flights.component';
import { FlightInstanceComponent } from './components/flightInstances/flight-instance/flight-instance.component';
import { FlightInstancesComponent } from './components/flightInstances/flight-instances/flight-instances.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TicketsComponent } from './components/tickets/tickets/tickets.component';
import { NewTicketComponent } from './components/tickets/new-ticket/new-ticket.component';
import { EditTicketComponent } from './components/tickets/edit-ticket/edit-ticket.component';
import { ReservationComponent } from './components/reservations/reservation/reservation.component';
import { ReservationsComponent } from './components/reservations/reservations/reservations.component';

@NgModule({
  declarations: [
    AppComponent,
    ErrorComponent,
    FooterComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    LogoutComponent,
    UserComponent,
    UsersComponent,
    NavbarComponent,
    EditUserComponent,
    AirlinesComponent,
    AirlineComponent,
    AirportComponent,
    AirportsComponent,
    FlightComponent,
    FlightsComponent,
    FlightInstanceComponent,
    FlightInstancesComponent,
    TicketsComponent,
    NewTicketComponent,
    EditTicketComponent,
    ReservationComponent,
    ReservationsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
