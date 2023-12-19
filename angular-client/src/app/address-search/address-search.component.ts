import {
  AfterViewInit,
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnChanges,
  Output,
  SimpleChanges,
  ViewChild
} from "@angular/core";

import * as citiesData from '../../app/cities_and_countries.json';

interface cites {
  City: string,
}

@Component({
  selector: 'app-address-search',
  templateUrl: './address-search.component.html',
  styleUrls: ["./address-search.component.scss"]
})


export class AddressSearchComponent implements AfterViewInit, OnDestroy, OnChanges {
  public selectedCity = null;
  public shouldAppendError: boolean = false;
  public endpointSearchAddress: string;
  public cities: string[];


//Declare the property
@Output() cityChange:EventEmitter<string> =new EventEmitter<string>();
@Input("city") city:string ;
@Input() whenInputEmpty: boolean;


  constructor() {
    const citiesDataJson = JSON.stringify(citiesData);
    const citiesDataArray = JSON.parse(citiesDataJson);
    this.cities = citiesDataArray;
    console.log(this.cities);

  }

  ngAfterViewInit() {
  }

  addErrorClass() {
    this.whenInputEmpty = true;
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.type) {
      console.log(changes)
    }
  }

  cityChangeHandler(event) {
    console.log(event);
    this.cityChange.emit(event);
  }


  ngOnDestroy() {
    this.selectedCity.removeAllListeners("change");
    this.selectedCity.removeAllListeners("clear");
    this.selectedCity.destroy();
  }
}