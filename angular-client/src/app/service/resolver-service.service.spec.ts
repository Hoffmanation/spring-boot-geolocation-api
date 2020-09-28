import { TestBed, inject } from '@angular/core/testing';

import { ResolverService } from './resolver-service.service';

describe('MovieServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MovieServiceService]
    });
  });

  it('should be created', inject([ResolverService], (service: MovieServiceService) => {
    expect(service).toBeTruthy();
  }));
});
