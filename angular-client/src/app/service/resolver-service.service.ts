import { Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { GeoLocation } from '../model/GeoLocation';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../environments/environment';
import { Subscription } from 'rxjs/Subscription';

@Injectable()
export class ResolverService {

  private usersUrl: string;
  private endpoints: Object;


  constructor(private http: HttpClient) {
    this.usersUrl = environment.baseUrl + ':' + environment.port
    this.endpoints = environment.endpoints;
  }


  public getLocation(search: string): Observable<GeoLocation> {
    return this.http.get<GeoLocation>(this.usersUrl + this.endpoints['getLocation'] + '?search=' + search);
  }




}
