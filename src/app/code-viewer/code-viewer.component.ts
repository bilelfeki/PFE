import { Component, OnInit } from '@angular/core';
import { CodeGenerated, CodeLinkerService } from '../code-linker.service';

@Component({
  selector: 'app-code-viewer',
  templateUrl: './code-viewer.component.html',
  styleUrls: ['./code-viewer.component.css']
})
export class CodeViewerComponent implements OnInit {
  springbatchCode = "";
  codeGenerated = new CodeGenerated();
  constructor(private codeLinker: CodeLinkerService) { }
  ngOnInit(): void {
    this.getCode()
  }
  getCode() {
    return this.codeLinker.getData().subscribe(code => {
      this.codeGenerated.entityCode = code.entityCode;
      this.codeGenerated.repoCode = code.repoCode;
      this.springbatchCode += code.springBatchCode.job
        + code.springBatchCode.processor
        + code.springBatchCode.reader
        + code.springBatchCode.step + code.springBatchCode.writer
    })
  }

}
