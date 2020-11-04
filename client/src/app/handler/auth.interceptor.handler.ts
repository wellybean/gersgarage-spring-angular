import { HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { AuthService } from '../service/auth.service';

@Injectable()
export class AppInterceptor implements HttpInterceptor {
    
    constructor(private authService: AuthService) {}

    intercept(req: HttpRequest, next: HttpHandler) {
        const token = this.authService.getJWTToken();
        req = req.clone({
            url:  req.url,
            setHeaders: {
                Authorization: `Bearer ${token}`
            }
        });
        return next.handle(req);
    }
}