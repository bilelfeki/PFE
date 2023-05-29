import { TestBed } from '@angular/core/testing';

import { CodeLinkerService } from './code-linker.service';

describe('CodeLinkerService', () => {
  let service: CodeLinkerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CodeLinkerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
