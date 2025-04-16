import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IProductDTO } from '../model/class/interface/Products';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  http: HttpClient;

  constructor(http: HttpClient) {
    this.http = http;
  }

  getTopSubscribedProducts(): Observable<IProductDTO[]> {
    return this.http.get<IProductDTO[]>("http://localhost:9090/OMP/topSubscribedProduct");
  }

  getTopRatedProducts(): Observable<IProductDTO[]> {
    return this.http.get<IProductDTO[]>("http://localhost:9090/OMP/topRatedProducts");
  }

  getProductList(): Observable<any> {
    return this.http.get("http://localhost:9090/OMP/viewAllProducts");
  }

  private baseUrl = 'http://localhost:9090/OMP'; // Update with your backend URL

  addProduct(name: string, description: string, imageFile: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('name', name);
    formData.append('description', description);
    formData.append('imageFile', imageFile);

    return this.http.post(`${this.baseUrl}/admin/addProduct`, formData);
  }

  updateProduct(name: string, upName: string, description: string, imageFile?: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('name', name);
    if (upName) formData.append('upName', upName);
    if (description) formData.append('description', description);
    if (imageFile) formData.append('imageFile', imageFile);

    return this.http.put(`${this.baseUrl}/admin/updateProduct/${name}`, formData);
  }

  uploadMultipleProducts(file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);

    return this.http.post(`${this.baseUrl}/admin/uploadMultipleRecords`, formData);
  }


  searchProductByName(productName: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/searchProductByName`, { params: { productName } });
  }

  getProductImageByName(name: string): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/product/imageByName/${name}`, { responseType: 'blob' });
  }

}