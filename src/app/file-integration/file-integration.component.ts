import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-file-integration',
  templateUrl: './file-integration.component.html',
  styleUrls: ['./file-integration.component.css']
})
export class FileIntegrationComponent  {
max:string[]=[];
column:string[]=[]
min:string[]=[]
addColumn() {
  this.champNumber++
}
      champNumber=0
      schema=""
      database=""
      first: number = 0;
      rows: number = 10;
      tabIndex="1";
      table=""
    onPageChange(event:any) {
        this.first = event.first;
        this.rows = event.rows;
        this.tabIndex=event.first+1   
      }
      onSelect(event:any){
        console.log(event);        
      }
      generateArray(champNumber:number){
        this.max[champNumber]="0"
        this.column[champNumber]="cloumn"+champNumber.toString()
        this.min[champNumber]="0"
        return Array(champNumber)
      }
      submit(){
        console.log(this.max);
        console.log(this.min);
        
        console.log(this.column);
        
      }
    
}
