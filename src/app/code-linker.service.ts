import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';

export interface SpringBatchCode {
  job: string

  processor: string

  reader: string

  step: string

  writer: string

}
export class CodeGenerated {
  entityCode: string;
  repoCode: string;
  springBatchCode: SpringBatchCode;
  constructor(entityCode: string = "", repoCode: string = "", springBatchCode = {
    job: "",

    processor: "",

    reader: "",

    step: "",

    writer: "",
  }) {
    this.entityCode = entityCode,
      this.repoCode = repoCode
    this.springBatchCode = springBatchCode
  }
}
@Injectable({
  providedIn: 'root'
})
export class CodeLinkerService {

  constructor() { }

  private dataSubject: BehaviorSubject<CodeGenerated> = new BehaviorSubject<CodeGenerated>(new CodeGenerated());

  setData(code: CodeGenerated) {
    console.log(code);

    this.dataSubject.next(code);
  }

  getData() {
    return this.dataSubject.asObservable();
  }
}
