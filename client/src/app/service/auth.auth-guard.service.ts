import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})

export class AuthorizeGuard implements CanActivate{
    
    constructor(private authService: AuthService,
                private router: Router) {}
    
    public canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> | Promise<boolean> | boolean {

        if (!this.authService.loggedIn()) {
            this.router.navigate(['/signin']);
            console.log('me cu');
            return false;
          }
          console.log('me cu0');
          return true;
    }
}