import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddressSearchComponent } from './address-search.component';

describe('AddressSearchComponent', () => {
  let component: AddressSearchComponent;
  let fixture: ComponentFixture<AddressSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddressSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddressSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
