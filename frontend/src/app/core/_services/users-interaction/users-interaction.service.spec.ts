import { TestBed, inject } from '@angular/core/testing';

import { UsersInteractionService } from './users-interaction.service';

describe('UsersInteractionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UsersInteractionService]
    });
  });

  it('should be created', inject([UsersInteractionService], (service: UsersInteractionService) => {
    expect(service).toBeTruthy();
  }));
});
