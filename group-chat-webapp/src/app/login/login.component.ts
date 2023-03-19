import { Component, OnInit, AfterViewInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import { LoginService } from '../shared/login.service';

declare let particlesJS: any;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, AfterViewInit {

  loginForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;

  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private securityService: LoginService
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required]
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  ngOnInit() {

  }

  ngAfterViewInit() {
    particlesJS.load('particles-js', 'assets/particlesjs-config.json', function () {
      console.log('particles.json loaded...');
    });
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.errorMessage = '';

    this.loading = true;
    this.securityService.loginProcess(this.f['username'].value);
    if (this.returnUrl) {
      this.router.navigateByUrl(this.returnUrl).catch(reason => console.error(reason));
    }
  }


  isFieldInvalid(fieldName: string) {
    let field = this.loginForm.get(fieldName);
    return (field && (
        (!field.valid && field.touched) ||
        (field.untouched && !this.submitted))
    );
  }
}
