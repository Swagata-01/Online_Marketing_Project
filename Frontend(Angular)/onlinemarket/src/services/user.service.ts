import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, tap, BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';
import { CookieServiceService } from './cookie-service.service';
import { IUserDetails, IUserIdResponse, IProductDTO } from '../model/class/interface/Products';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userIdSubject = new BehaviorSubject<number | null>(null);
  userId$ = this.userIdSubject.asObservable();
  userDetails: IUserDetails | null = null;
  private _userId: number | null = null; // Private backing field for userId

  constructor(private http: HttpClient, private cookieService: CookieServiceService, private router: Router) {
    this.loadUserIdFromLocalStorage();
    this.loadUserDetailsFromLocalStorage();
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>('http://localhost:9090/OMP/login', { email, password });
  }

  getUserIdByEmail(email: string): Observable<IUserIdResponse> {
    const params = new HttpParams().set('emailId', email);
    return this.http.get<IUserIdResponse>('http://localhost:9090/OMP/getUserIdByEmail', { params }).pipe(
      tap(response => console.log('Raw user ID response:', response))
    );
  }

  getUserDetails(userId: number): Observable<IUserDetails> {
    const params = new HttpParams().set('userId', userId.toString());
    return this.http.get<IUserDetails>('http://localhost:9090/OMP/myDetails',{params});
  }

  getProductSubscriptionList(userId: number): Observable<IProductDTO[]> {
    const params = new HttpParams().set('userId', userId.toString());
    return this.http.get<IProductDTO[]>('http://localhost:9090/OMP/getProductSubscriptionList', { params });
  }

  register(formData: FormData): Observable<any> {
    return this.http.post('http://localhost:9090/OMP/register', formData, {
      headers: {
        'Accept': 'application/json'
      }
    });
  }

  updateUser(userId: any, formData: FormData): Observable<any> {
    return this.http.put(`http://localhost:9090/OMP/updateUser/${userId}`, formData, {
      headers: {
        'Accept': 'application/json'
      }
    });
  }

  handleLoginSuccess(email: string): void {
    this.cookieService.setCookie(email);
    console.log('Login successful, fetching user ID for:', email);
    this.getUserIdByEmail(email).subscribe({
      next: (response: IUserIdResponse) => {
        this.setUserId(response); // Use the setter
        localStorage.setItem('userId', response.toString());
        console.log('User ID stored in local storage:', response);
        this.router.navigate(['/home']).then(() => {
          window.location.reload(); // Consider if full reload is necessary
        });
      },
      error: (err) => {
        console.error('Error fetching user ID:', err);
      },
    });
  }

  loadUserIdFromLocalStorage(): void {
    const storedUserId = localStorage.getItem('userId');
    if (storedUserId) {
      this.setUserId(parseInt(storedUserId, 10)); // Use the setter
      console.log('User ID loaded from local storage:', this._userId);
    }
  }

  loadUserDetailsFromLocalStorage(): void {
    const storedUserDetails = localStorage.getItem('userDetails');
    if (storedUserDetails) {
      try {
        this.userDetails = JSON.parse(storedUserDetails);
        console.log('User details loaded from local storage:', this.userDetails);
      } catch (error) {
        console.error('Error parsing user details from local storage:', error);
        localStorage.removeItem('userDetails');
      }
    }
  }

  saveUserDetailsToLocalStorage(details: IUserDetails): void {
    localStorage.setItem('userDetails', JSON.stringify(details));
    this.userDetails = details;
    console.log('User details saved to local storage:', this.userDetails);
  }

  clearUserDetails(): void {
    this.userDetails = null;
    localStorage.removeItem('userDetails');
  }

  setUserId(userId: number | null): void {
    this.userIdSubject.next(userId);
    this._userId = userId; // Update the backing field
  }

  get userId(): number | null {
    return this._userId; // Return the value of the backing field
  }

  watchUserId(): Observable<number | null> {
    return this.userId$;
  }
}