import { Component, OnInit, OnDestroy, Input, OnChanges, SimpleChanges } from '@angular/core'; // Import OnChanges and SimpleChanges
import { IUserDetails } from '../../model/class/interface/Products';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  providers : [UserService]
})
export class HeaderComponent implements OnInit, OnDestroy, OnChanges { // Implement OnChanges
  @Input() loggedIn: boolean = false;
  userDetails: IUserDetails | null = null;
  userIconSource: string = 'assets/images/profile.png'; // Default user icon
  userDetailsSubscription: Subscription | undefined;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUserDetails();
    this.loggedIn = this.userService.userId !== null; // Update loggedIn status on init (might be redundant now with ngOnChanges)
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['loggedIn']) {
      if (!changes['loggedIn'].currentValue) {
        this.userIconSource = 'assets/images/profile.png'; // Reset on logout
        this.userDetails = null; // Optionally clear user details as well
      } else if (changes['loggedIn'].currentValue && this.userService.userId) {
        // If logged in and we have a userId, reload user details (in case component was already initialized as logged out)
        this.loadUserDetails();
      }
    }
  }

  ngOnDestroy(): void {
    if (this.userDetailsSubscription) {
      this.userDetailsSubscription.unsubscribe();
    }
  }

  loadUserDetails(): void {
    const userId = this.userService.userId;
    console.log('User ID in HeaderComponent:', userId);

    if (userId !== null) {
      this.userDetailsSubscription = this.userService.getUserDetails(userId).subscribe({
        next: (details) => {
          this.userDetails = details;
          this.updateHeaderContent();
          console.log('User details in HeaderComponent:', this.userDetails);
        },
        error: (error) => {
          console.error('Error fetching user details in HeaderComponent:', error);
        }
      });
    } else {
      this.userDetails = null; // Ensure userDetails is null when not logged in
      this.updateHeaderContent();
      console.log('User ID is null in HeaderComponent');
    }
  }

  updateHeaderContent(): void {
    if (this.loggedIn && this.userDetails?.photo) {
      this.userIconSource = this.userDetails.photo;
    } else {
      this.userIconSource = 'assets/images/profile.png'; // Reset to default
    }
  }
}