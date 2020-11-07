import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {

  loginForm = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  hide = true;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  login() {
    const result = this.authService.signIn(this.loginForm.value).subscribe(
        res => {
          localStorage.setItem('accessToken', res.accessToken);
          const res2 = this.router.navigate(['/user-home']);
          console.log(res, res2);
        }, 
        error => {
          this.router.navigate(['/home']);
          console.log(error);
        }
    );
  }

}
