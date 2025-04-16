import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SearchfilterComponent } from "../searchfilter/searchfilter.component";
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-products',
  imports: [CommonModule, RouterOutlet, RouterModule, FormsModule, SearchfilterComponent],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css',
  providers : [ProductService]
})
export class ProductsComponent implements OnInit {

  public productList: any = [];

  ngOnInit(): void {
    console.log('ProductComponent initialized');
    this.productService.getProductList().subscribe(response => {
      this.productList = response;
    })
  }

  viewProductDetails(productId: string) {
    this.router.navigate(['/product-details', productId]);
  }


  constructor(private productService: ProductService, private router: Router) {

  }


}
