import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Group } from 'src/app/models/group.model';
import { GroupService } from 'src/app/service/groups/group.service';

@Component({
  selector: 'app-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css'],
})
export class GroupComponent implements OnInit {
  groupForm: FormGroup;
  groupName: string;
  group = new Group('', []);
  message = null;
  success: boolean;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private groupService: GroupService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.groupName = this.route.snapshot.params['name'];

    this.groupForm = this.formBuilder.group({
      name: [
        '',
        [
          Validators.required,
          Validators.minLength(2),
          this.forbiddenNameValidator(/new/i),
        ],
      ],
    });

    if (this.groupName !== 'new') {
      this.group = this.groupService.getByName(this.groupName);
      if (this.group == null || this.group == undefined)
        this.router.navigate(['groups']);
      else {
        this.groupForm.setValue({
          name: this.group.name,
        });
      }
    }
  }

  closeAlert() {
    this.message = undefined;
  }

  public get name() {
    return this.groupForm.get('name');
  }

  saveGroup(formData) {
    if (this.groupName === 'new') {
      if (this.groupService.add(new Group(formData.name, [])))
        this.router.navigate(['groups']);
      else {
        this.success = false;
        this.message = 'That group name is taken. Try another.';
      }
    } else {
      // TODO - Edit Group (not requested in homework specification)
    }
  }

  // from https://angular.io/guide/form-validation
  forbiddenNameValidator(nameRe: RegExp): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const forbidden = nameRe.test(control.value);
      return forbidden ? { forbiddenName: { value: control.value } } : null;
    };
  }
}
