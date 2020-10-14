import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ErrorComponent } from './components/error/error.component';
import { GroupsComponent } from './components/groups/groups.component';
import { LoginComponent } from './components/login/login.component';
import { LogoutComponent } from './components/logout/logout.component';
import { RouteGuardService } from './service/routes/route-guard.service';
import { UsersComponent } from './components/users/users.component';
import { UserComponent } from './components/user/user.component';
import { GroupComponent } from './components/group/group.component';
import { UserDetailsComponent } from './components/user-details/user-details.component';
import { GroupDetailsComponent } from './components/group-details/group-details.component';
import { HomeComponent } from './components/home/home.component';

const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [RouteGuardService] },
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent, canActivate: [RouteGuardService] },

  { path: 'users', component: UsersComponent, canActivate: [RouteGuardService] },
  { path: 'users/:id', component: UserComponent, canActivate: [RouteGuardService] },
  { path: 'users/:id/details', component: UserDetailsComponent, canActivate: [RouteGuardService] },

  { path: 'groups', component: GroupsComponent, canActivate: [RouteGuardService] },
  { path: 'groups/:name', component: GroupComponent, canActivate: [RouteGuardService] },
  { path: 'groups/:name/details', component: GroupDetailsComponent, canActivate: [RouteGuardService] },

  { path: '**', component: ErrorComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
