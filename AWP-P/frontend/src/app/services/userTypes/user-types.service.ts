import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from 'src/app/app.constants';
import UserType from 'src/app/models/userType/userType.model';

@Injectable({
  providedIn: 'root'
})
export class UserTypesService {
  private readonly userTypes = API_URL + '/userTypes';

  constructor(private httpClient: HttpClient) { }

  getAllUserTypes() {
    return this.httpClient.get<UserType[]>(`${this.userTypes}/all`);
  }
}
