import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FileIntegrationComponent } from './file-integration/file-integration.component';
import { HomeComponent } from './home/home.component';
import { CodeViewerComponent } from './code-viewer/code-viewer.component';

const routes: Routes = [
  { component: FileIntegrationComponent, path: "file_integration" },
  { component: HomeComponent, path: "home" },
  { component: CodeViewerComponent, path: "generated_code" }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
