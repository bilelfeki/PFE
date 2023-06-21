import { Component, OnDestroy, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Config } from '../interface/config';
import { Entity } from '../interface/entity';
import { EntityService } from '../entity.service';
import { EntityHandlerService } from '../entity-handler.service';
import { FileEvent } from '../interface/FileEvent';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { CodeLinkerService } from '../code-linker.service';
@Component({
  selector: 'app-file-integration',
  templateUrl: './file-integration.component.html',
  styleUrls: ['./file-integration.component.css']
})

export class FileIntegrationComponent implements OnDestroy {
  fileName = ""
  config: Config
  x!: Subscription
  fileSelected = false;
  constructor(
    private entityService: EntityService,
    private codeLinker: CodeLinkerService,
    private router: Router) {
    this.config = {
      schema: "",
      database: ""
    };
  }
  ngOnDestroy(): void {
    //this.x.unsubscribe()
  }
  first = 0;
  rows = 1;
  onPageChange(event: any) {
    this.first = event.first;
  }
  onSelectFile(event: FileEvent) {
    this.fileSelected = true
    this.fileName = event.currentFiles[0].name.split(".")[0]
    this.entityService.addFile(event.currentFiles[0])
    console.log(event.currentFiles);
  }
  onNextClick() {
    this.first += 1
  }
  getEntity(entity: Entity) {
    console.log(entity);
    console.log(this.config);
    //this.entityService.resetFormData()
    this.entityService.addConfig(this.config);
    this.entityService.addentitiesString(entity);
    this.entityService.submitFormData().subscribe(data => {
      this.codeLinker.setData(data)
      this.router.navigate(['/generated_code'])
    })
    this.router.navigate(['/generated_code'])
  }
  onUpdateFileClick() {
    this.fileSelected = false
    this.first = 0
  }
}
