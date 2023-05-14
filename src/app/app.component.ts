import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'front';
   checked: boolean=true;
   items: MenuItem[]=[] ;
  

    activeItem: MenuItem =this.items[0];

    ngOnInit() {
      this.items[0]={label:"Home",routerLink:"home",icon: 'pi pi-fw pi-home' };
      this.items[1]={label:"File_Integration",routerLink:"file_integration",icon:" pi pi-fw pi-user-edit"}
      this.items[2]={}
      this.activeItem = this.items[1];
    }
}
