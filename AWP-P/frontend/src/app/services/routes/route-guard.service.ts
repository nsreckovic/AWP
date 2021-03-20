import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { AuthenticationService } from '../auth/authentication.service';

@Injectable({
  providedIn: 'root',
})
export class RouteGuardService {
  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.authService.isUserLoggedIn()) return true;
    this.router.navigate(['/login']);
    return false;
  }

}
