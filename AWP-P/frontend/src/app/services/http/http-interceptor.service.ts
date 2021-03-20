import { Injectable } from '@angular/core';
import {
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { AuthenticationService } from '../auth/authentication.service';

@Injectable({
  providedIn: 'root',
})
export class HttpInterceptorService implements HttpInterceptor {
  constructor(private authService: AuthenticationService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    if (this.authService.isUserLoggedIn()) {
      req = req.clone({
        setHeaders: {
          Authorization: 'Bearer ' + this.authService.getLoggedInUserJwt(),
        },
      });
    }
    return next.handle(req);
  }
}
