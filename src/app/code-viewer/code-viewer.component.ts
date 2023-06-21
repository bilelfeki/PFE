import { Component, Input, OnInit } from '@angular/core';
import { CodeGenerated, CodeLinkerService } from '../code-linker.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-code-viewer',
  templateUrl: './code-viewer.component.html',
  styleUrls: ['./code-viewer.component.css']
})
export class CodeViewerComponent implements OnInit {
  compiled = true
  @Input()
  springbatchCode = "";
  codeGenerated = new CodeGenerated();
  constructor(private codeLinker: CodeLinkerService,
    private http: HttpClient) { }
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
  compileCode() {
    this.compiled = false
    this.http.post("http://127.0.0.1:8080/api/v1/compile", {}).subscribe(
      data => console.log(data)
    )
  }
}
