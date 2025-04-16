import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Subscription } from 'rxjs';
import { HeaderComponent } from '../header/header.component';
 
@Component({
  selector: 'app-profile',
  standalone: true, // If you are using standalone components
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
  providers: [DatePipe]
})
 
export class ProfileComponent implements OnInit, OnDestroy {
  profileForm: FormGroup;
  photoError: string = '';
  currentPhotoUrl: string | null = null;
  userId: number | null = null;
 
  userIdSubscription: Subscription | undefined;
 
  constructor(private fb: FormBuilder, private userService: UserService, private router: Router, private datePipe: DatePipe) {
    this.profileForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9._]{3,15}$/)]],
      lastName: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9._]{3,15}$/)]],
      nickName: ['', [Validators.pattern(/^[a-zA-Z0-9._]{3,15}$/)]],
      email: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.(com|net|org)$/)]],
      contactNo: ['', [Validators.required, Validators.pattern(/^[6-9]\d{9}$/)]],
      postalCode: ['', [Validators.required, Validators.pattern(/^\d{6}$/)]],
      address1: ['', [Validators.required, Validators.minLength(10)]],
      address2: ['', [Validators.required, Validators.minLength(10)]],
      dob: ['', [Validators.required, this.minimumAgeValidator(18)]],
      photo: [''] // To hold the file object temporarily
    });
 
    // Disable the email field as it's often not editable in the profile
    this.profileForm.controls['email'].disable();
  }
 
  ngOnInit(): void {
    this.userIdSubscription = this.userService.watchUserId().subscribe(id => {
      this.userId = id;
      if (this.userId) {
        this.loadUserProfile();
      } else {
        console.warn('User ID not available.');
        // Optionally handle this scenario (e.g., redirect to login)
      }
    });
  }
 
  ngOnDestroy(): void {
    if (this.userIdSubscription) {
      this.userIdSubscription.unsubscribe();
    }
  }
 
  loadUserProfile(): void {
    if (this.userId) {
      this.userService.getUserDetails(this.userId).subscribe({
        next: (profileData: any) => {
          console.log('Profile Data:', profileData); // Debugging line
          this.profileForm.patchValue({
            firstName: profileData.firstName,
            lastName: profileData.lastName,
            nickName: profileData.nickName,
            email: profileData.email,
            contactNo: profileData.contactNumber,
            postalCode: profileData.address?.split(', ')?.pop(),
            address1: profileData.address?.split(', ')[0],
            address2: profileData.address?.split(', ')[1],
            dob: this.datePipe.transform(profileData.dateOfBirth, 'yyyy-MM-dd')
          });
          this.currentPhotoUrl = profileData.photo; // Set the photo URL
          console.log('Current Photo URL:', this.currentPhotoUrl); // Debugging line
        },
        error: (error) => {
          console.error('Error loading profile:', error);
        }
      });
    } else {
      console.warn('User ID is not available, cannot load profile.');
    }
  }
 
 
  minimumAgeValidator(minAge: number) {
    return (control: any) => {
      const dob = new Date(control.value);
      const today = new Date();
      const age = today.getFullYear() - dob.getFullYear();
      const hasHadBirthday = today.getMonth() > dob.getMonth() ||
        (today.getMonth() === dob.getMonth() && today.getDate() >= dob.getDate());
      return age > minAge || (age === minAge && hasHadBirthday) ? null : { minAge: true };
    };
  }
 
  onFileChange(event: any) {
    const file = event?.target?.files?.[0];
    if (file) {
      if (file.size < 10240 || file.size > 20480) {
        this.photoError = 'Photo must be between 10KB and 20KB.';
      } else {
        this.photoError = '';
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.currentPhotoUrl = e.target.result; // Update the photo URL
        };
        reader.readAsDataURL(file);
      }
    }
  }
 
  removePhoto() {
    const photoInput = document.getElementById('photo') as HTMLInputElement;
    if (photoInput) {
      photoInput.value = '';
      this.photoError = '';
      this.currentPhotoUrl = null; // Clear the photo URL
    }
  }
 
  onSubmit(): void {
 
    
    if (this.profileForm.valid && !this.photoError) {
      const formData = new FormData();
      console.log(formData.get('email'));
      console.log(this.userId);
 
      formData.append('firstName', this.profileForm.get('firstName')?.value || '');
      formData.append('lastName', this.profileForm.get('lastName')?.value || '');
      formData.append('nickName', this.profileForm.get('nickName')?.value || '');
      formData.append('email', this.profileForm.get('email')?.value || '');
      formData.append('contactNumber', this.profileForm.get('contactNo')?.value || '');
      formData.append('address1', this.profileForm.get('address1')?.value || '');
      formData.append('address2', this.profileForm.get('address1')?.value || '');
      formData.append('pincode', this.profileForm.get('pincode')?.value || '');
      formData.append('dateOfBirth', this.profileForm.get('dob')?.value || '');
 
      const photoInput = document.getElementById('photo') as HTMLInputElement;
      if (photoInput?.files?.length) {
        formData.append('imageFile', photoInput.files[0]);
      }
 
      console.log(Array.from(formData.entries()));
 
      this.userService.updateUser(this.userId, formData).subscribe({
        next: (response) => {
          console.log('Profile updated successfully:', response);
          alert('Profile updated successfully!');
          this.loadUserProfile();
        },
        error: (err) => {
          console.error('Profile update failed:', err);
          alert(`Profile update failed: ${err.message}`);
        }
      });
    }
  }
}
 