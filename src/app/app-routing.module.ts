import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ErrorComponent } from './components/error/error.component';
import { LoginComponent } from './components/users/login/login.component';
import { LogoutComponent } from './components/users/logout/logout.component';
import { HomeComponent } from './components/home/home.component';
import { RouteGuardService } from './services/routes/route-guard.service';
import { RegisterComponent } from './components/users/register/register.component';
import { UsersComponent } from './components/users/users/users.component';
import { EditUserComponent } from './components/users/edit-user/edit-user.component';
import { UserComponent } from './components/users/user/user.component';
import { AirlinesComponent } from './components/airlines/airlines/airlines.component';
import { AirlineComponent } from './components/airlines/airline/airline.component';
import { AirportsComponent } from './components/airports/airports/airports.component';
import { AirportComponent } from './components/airports/airport/airport.component';
import { FlightsComponent } from './components/flights/flights/flights.component';
import { FlightComponent } from './components/flights/flight/flight.component';
import { FlightInstancesComponent } from './components/flightInstances/flight-instances/flight-instances.component';
import { FlightInstanceComponent } from './components/flightInstances/flight-instance/flight-instance.component';

const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [RouteGuardService] },
  
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent, canActivate: [RouteGuardService] },

  { path: 'users', component: UsersComponent, canActivate: [RouteGuardService] },
  { path: 'users/:id/edit', component: EditUserComponent, canActivate: [RouteGuardService] },
  { path: 'users/:id/details', component: UserComponent, canActivate: [RouteGuardService] },
  
  { path: 'airlines', component: AirlinesComponent, canActivate: [RouteGuardService] },
  { path: 'airlines/:operation', component: AirlineComponent, canActivate: [RouteGuardService] },
  { path: 'airlines/:id/:operation', component: AirlineComponent, canActivate: [RouteGuardService] },
  
  { path: 'airports', component: AirportsComponent, canActivate: [RouteGuardService] },
  { path: 'airports/:operation', component: AirportComponent, canActivate: [RouteGuardService] },
  { path: 'airports/:id/:operation', component: AirportComponent, canActivate: [RouteGuardService] },

  { path: 'flights', component: FlightsComponent, canActivate: [RouteGuardService] },
  { path: 'flights/:operation', component: FlightComponent, canActivate: [RouteGuardService] },
  { path: 'flights/:id/:operation', component: FlightComponent, canActivate: [RouteGuardService] },

  { path: 'flightInstances', component: FlightInstancesComponent, canActivate: [RouteGuardService] },
  { path: 'flightInstances/:operation', component: FlightInstanceComponent, canActivate: [RouteGuardService] },
  { path: 'flightInstances/:id/:operation', component: FlightInstanceComponent, canActivate: [RouteGuardService] },

  { path: '**', component: ErrorComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
