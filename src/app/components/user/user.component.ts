import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Data } from 'src/app/data';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/service/users/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'],
})
export class UserComponent implements OnInit {
  public userForm: FormGroup;
  error_message: string;
  error = false;
  user = new User(-1, '', '');
  user_id: number;

  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private data: Data,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.user_id = this.route.snapshot.params['id'];
    if (this.user_id != -1) {
      this.userService.getUser(this.user_id).subscribe((data) => {
        if (data === undefined) this.router.navigate(['users']);
        else {
          this.user = data;
          this.userForm.setValue({
            firstName: this.user.firstName,
            lastName: this.user.lastName,
          });
        }
      });
    }

    this.userForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
    });
  }

  public get firstName() {
    return this.userForm.get('firstName');
  }

  public get lastName() {
    return this.userForm.get('lastName');
  }

  saveUser(formData) {
    // Creating new user
    if (this.user.id === -1) {
      this.userService
        .createUser({
          id: 0,
          firstName: formData.firstName,
          lastName: formData.lastName,
        })
        .subscribe(
          (data) => {
            this.data.removeUser();
            this.router.navigate(['users']);
          },
          (error) => {
            this.error = true;
            this.error_message = error.error;
          }
        );

      // Editing existing user
    } else {
      this.userService
        .updateUser({
          id: this.user.id,
          firstName: formData.firstName,
          lastName: formData.lastName,
        })
        .subscribe(
          (data) => {
            this.data.removeUser();
            this.router.navigate(['users']);
          },
          (error) => {
            this.error = true;
            this.error_message = error.error;
          }
        );
    }
  }
}
