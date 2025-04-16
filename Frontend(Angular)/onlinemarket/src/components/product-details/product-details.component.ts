import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute, RouterModule, RouterOutlet } from '@angular/router';
import { UserReviewComponent } from "../user-review/user-review.component";


@Component({
  selector: 'app-product-details',
  imports: [CommonModule, RouterModule, RouterOutlet, UserReviewComponent],
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.css'
})
export class ProductDetailsComponent {
  [x: string]: any;


  public product: any;

  constructor(private route: ActivatedRoute, private http: HttpClient) { }


  ngOnInit() {
    const productId = this.route.snapshot.paramMap.get('id');
    if (productId) {
      this.http.get(`http://localhost:9090/OMP/viewProductDetails/${productId}`).subscribe(
        (product: any) => {
          this.product = product;
        },
        error => {

          this.product = null;
        }
      );
    } else {
      this.product = null;
    }
  }
}



