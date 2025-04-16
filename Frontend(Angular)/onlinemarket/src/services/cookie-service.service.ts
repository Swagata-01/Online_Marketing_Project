import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class CookieServiceService {

  constructor(private cookieService : CookieService) { }


  setCookie(cookieValue : String){

      const expires = new Date(Date.now() + 20*60*1000);
      this.cookieService.set("userEmail",cookieValue.toString(),expires,'/');

  }

  getCookie() : String{
    return this.cookieService.get("userEmail");
  }

  isLoggedIn() : boolean{
      return this.cookieService.check("userEmail");
  }

  deleteCookie(){
    this.cookieService.delete("userEmail");
  }
}
