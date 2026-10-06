import { Component, Input } from '@angular/core';
import { PatientProfile } from '../../models/erp.models';

@Component({
  selector: 'app-patient-profile',
  standalone: true,
  templateUrl: './patient-profile.component.html',
})
export class PatientProfileComponent {
  @Input({ required: true }) patient!: PatientProfile;
}