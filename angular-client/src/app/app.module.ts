import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ResolverService } from './service/resolver-service.service'
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { TopBarComponent } from './top-bar/top-bar.component';
import { AddressSearchComponent } from './address-search/address-search.component';
import { HttpClientModule } from '@angular/common/http';
import {PrettyJsonModule} from 'angular2-prettyjson';
import { NgxSpinnerModule } from "ngx-spinner";


@NgModule({
  declarations: [
    AppComponent,
    TopBarComponent,
    AddressSearchComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    PrettyJsonModule,
    NgxSpinnerModule
    
  ],
  providers: [ResolverService,AddressSearchComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }
