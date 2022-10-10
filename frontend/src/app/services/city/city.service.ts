import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {City} from "../../models/city";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class CityService {

  constructor(private http: HttpClient) {}

  searchCities(params: any): Observable<any> {
    return this.http.post('api/cities/search', params, httpOptions);
  }

  editCity(city: City, file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('image', file);
    formData.append('city', new Blob([JSON.stringify(city)], {type: 'application/json'}));

    return this.http.post(`api/cities/${city.id}`, formData);
  }

}
