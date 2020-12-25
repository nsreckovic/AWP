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

const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [RouteGuardService] },
  
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent, canActivate: [RouteGuardService] },

  { path: 'users', component: UsersComponent, canActivate: [RouteGuardService] },
  { path: 'users/:id/edit', component: EditUserComponent, canActivate: [RouteGuardService] },
  { path: 'users/:id/details', component: UserComponent, canActivate: [RouteGuardService] },



  { path: '**', component: ErrorComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
