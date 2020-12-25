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
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
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
