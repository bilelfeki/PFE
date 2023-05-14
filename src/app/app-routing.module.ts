import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FileIntegrationComponent } from './file-integration/file-integration.component';
import { HomeComponent } from './home/home.component';

const routes: Routes = [
  {component:FileIntegrationComponent,path:"file_integration"},
  {component:HomeComponent,path:"home"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
