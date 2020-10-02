import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { ResolverService } from '../service/resolver-service.service';
import { GeoLocation } from '../model/GeoLocation';
import { AddressSearchComponent } from '../address-search/address-search.component';
import { NgxSpinnerService } from "ngx-spinner";
import { environment } from '../../environments/environment';

@Component({
	selector: 'app-top-bar',
	templateUrl: './top-bar.component.html',
	styleUrls: []
})
export class TopBarComponent implements OnInit {

	public search: string;
	public endpointSearchAddress: string;
	public location: any[] = [];
	public locationResponse: GeoLocation;
	public locationPrint: string;
	public showTable: boolean = true;
	public showJson: boolean = false;
	public emptyInput: boolean = false;
	private scrollContainer: any;
	@ViewChild('scrolToFirstBox') scrolToFirstBox: ElementRef;
	@ViewChild('scrolToSecondBox') scrolToSecondBox: ElementRef;

	constructor(public resolverService: ResolverService, public addressBarComp: AddressSearchComponent, private spinner: NgxSpinnerService) {
		console.log('%c Geo-Resolver Version:' + environment.version, 'color: #70c7be, font-size: 24px;');
	 }

	ngOnInit() {
	}

	scrollTo(el: HTMLElement) {
		el.scrollIntoView({ behavior: "smooth" });
	}


	showTableResults() {
		this.showJson = false;
		this.showTable = true;
	}

	showJsonResults() {
		this.showJson = true;
		this.showTable = false;

	}


	testResolver() {
		if (!this.search) {
			this.emptyInput = true;
			this.addressBarComp.addErrorClass();
			return;
		}
		this.spinner.show();
		this.location = [];
		this.emptyInput = false;
		this.locationResponse = new GeoLocation();
		this.resolverService.getLocation(this.search).subscribe(response => {
			this.locationResponse = response['entity'];
			for (let obj in this.locationResponse) {
				this.location.push({ key: obj, value: this.locationResponse[obj] })
			}
			this.locationPrint = JSON.stringify(this.locationResponse, null, 2)
				.replace(/ /g, '&nbsp;')
				.replace(/\n/g, '<br/>');
			this.spinner.hide();
			this.scrollTo(this.scrolToSecondBox.nativeElement);
		});
	}


	onSuggestion(event: any) {
		this.search = event.suggestion.value
		this.endpointSearchAddress = this.search.replace(/ /g, '');
		this.emptyInput = false;
	}


	onClear(event: any) {
		this.search = "";
		this.emptyInput = false;

	}

}
