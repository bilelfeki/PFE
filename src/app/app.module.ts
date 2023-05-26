import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { InputSwitchModule } from 'primeng/inputswitch';
import { FormsModule } from '@angular/forms';
import { TabMenuModule } from 'primeng/tabmenu';
import { FileIntegrationComponent } from './file-integration/file-integration.component';
import { HomeComponent } from './home/home.component';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { MenubarModule } from 'primeng/menubar';
import { AnimateModule } from 'primeng/animate';
import { SidebarModule } from 'primeng/sidebar';
import { MenuModule } from 'primeng/menu';
import { PaginatorModule } from 'primeng/paginator';
import { InputTextModule } from 'primeng/inputtext';
import { FileUploadModule } from 'primeng/fileupload';
import { HttpClientModule } from '@angular/common/http';
import { SplitButtonModule } from 'primeng/splitbutton';
import { DividerModule } from 'primeng/divider';
import { EntityComponent } from './entity/entity.component';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { TreeSelectModule } from 'primeng/treeselect';
import { CascadeSelectModule } from 'primeng/cascadeselect';

@NgModule({
  declarations: [
    AppComponent,
    FileIntegrationComponent,
    HomeComponent,
    EntityComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    InputSwitchModule,
    FormsModule,
    TabMenuModule,
    CardModule,
    ButtonModule,
    MenubarModule,
    CascadeSelectModule,
    AnimateModule,
    SidebarModule,
    MenuModule,
    TreeSelectModule,
    PaginatorModule,
    InputTextModule,
    FileUploadModule,
    HttpClientModule,
    SplitButtonModule,
    DividerModule,
    TableModule,
    ToastModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
