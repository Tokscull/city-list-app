import { Component, OnInit } from '@angular/core';
import { City } from "../../models/city";
import { CityService } from "../../services/city/city.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { EditCityComponent } from "./edit-city/edit-city.component";
import { TokenStorageService } from "../../services/storage/token-storage.service";
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-city-list',
  templateUrl: './city-list.component.html',
  styleUrls: ['./city-list.component.scss']
})
export class CityListComponent implements OnInit {
  isAdmin = false;
  cities: City[] = [];
  searchName = '';

  page = 1;
  totalItems = 0;
  pageSize = 2;
  pageSizes = [ 2, 4, 6 ];

  constructor(private cityService: CityService, private tokenStorageService: TokenStorageService,
              private modalService: NgbModal) { }

  ngOnInit(): void {
    const user = this.tokenStorageService.getUser();
    this.isAdmin = user.roles.some((e: any) => e.name === 'ROLE_ADMIN');

    this.loadCities();
  }

  getRequestParams(page: number, size: number, searchFiled: string, searchValue: string) {
    let params: any = {};
    if (page) {
      params['page'] = page - 1;
    }
    if (size) {
      params['size'] = size;
    }
    if (searchFiled && searchValue) {
      params['filters'] = [{
        field: searchFiled,
        operator: "LIKE",
        value: searchValue
      }]
    }
    return params;
  }

  loadCities() {
    const params = this.getRequestParams(this.page, this.pageSize, 'name', this.searchName);

    this.cityService.searchCities(params).subscribe({
      next: data => {
        this.totalItems = data.totalElements;
        this.cities = data.content;
        this.cities.forEach(city => {
          city.imagePath = `${environment.apiUrl}/api/images/${city.imagePath}`;
        });
      },
      error: err => {
        console.log(err);
      }
    })
  }

  handlePageChange(event: number): void {
    this.page = event;
    this.loadCities();
  }

  handlePageSizeChange(event: any): void {
    this.pageSize = event.target.value;
    this.page = 1;
    this.loadCities();
  }

  editCity(city: City): void {
    const editCityRef = this.modalService.open(EditCityComponent);
    editCityRef.componentInstance.city = city;

    editCityRef.result.then(() => {
      this.refreshList();
    }, dismiss => {
      console.log(dismiss)
    });
  }

  refreshList(): void {
    this.searchName = '';
    this.page = 1;
    this.pageSize = 2;
    this.loadCities();
  }
}
