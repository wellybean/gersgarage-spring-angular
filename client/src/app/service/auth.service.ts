import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
    constructor(private http: HttpClient,
                private router: Router) { }

  signIn(credentials) {
    return this.http.post<{accessToken: string}>('http://localhost:8080/api/auth/signin', credentials);
  }

  signUp(newUser) {
    return this.http.post('http://localhost:8080/api/auth/signup', newUser);
  }

  logout() {
    localStorage.removeItem('accessToken');
  }

  loggedIn(): boolean {
    return localStorage.getItem('accessToken') !==  null;
  }
}
