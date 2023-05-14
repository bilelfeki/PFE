import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FileIntegrationComponent } from './file-integration.component';

describe('FileIntegrationComponent', () => {
  let component: FileIntegrationComponent;
  let fixture: ComponentFixture<FileIntegrationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FileIntegrationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FileIntegrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
