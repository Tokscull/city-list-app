import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {City} from "../../../models/city";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {CityService} from "../../../services/city/city.service";

@Component({
  selector: 'app-edit-city',
  templateUrl: './edit-city.component.html',
  styleUrls: ['./edit-city.component.scss']
})
export class EditCityComponent implements OnInit {
  @Input() city: City = {};

  selectedFiles?: FileList;
  fileToUpload?: File;

  constructor(public activeModal: NgbActiveModal, private cityService: CityService) { }

  ngOnInit(): void {}

  changeImage(event: any) {
    this.selectedFiles = event.target.files;
  }

  editCity() {
    if (this.selectedFiles) {
      const file: File | null = this.selectedFiles.item(0);
      if (file) {
        this.fileToUpload = file;
        this.cityService.editCity(this.city, this.fileToUpload).subscribe({
          error: err => {
            console.log(err);
          }
        });
      }
    }
    this.activeModal.close();
  }

}
