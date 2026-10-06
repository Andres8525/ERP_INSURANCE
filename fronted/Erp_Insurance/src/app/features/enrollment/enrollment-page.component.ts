import { Component, EventEmitter, Input, Output } from '@angular/core';

export type EnrollmentStepDirection = 1 | -1;

@Component({
  selector: 'app-enrollment-page',
  standalone: true,
  templateUrl: './enrollment-page.component.html',
})
export class EnrollmentPageComponent {
  @Input({ required: true }) step = 1;

  @Output() stepChange = new EventEmitter<EnrollmentStepDirection>();
}