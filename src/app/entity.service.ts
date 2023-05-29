import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Entity } from './interface/entity';
import { Config } from './interface/config';
import { CodeGenerated } from './code-linker.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})


export class EntityService {

  /**
 * entitiesString {"ghazi":[{"valeur":"xx","valeur_min":1,"valeur_max":12,"type":"String"},{"valeur":"name","valeur_min":19,"valeur_max":25,"type":"String"}]}
 * configString {"database": "hh", "schema": "GHY"}
 * File normal file
 */
  private api = "http://127.0.0.1:8080/api/v1/file_integrator"
  private _formdata: FormData;

  constructor(private http: HttpClient) {
    this._formdata = new FormData();
  }

  resetFormData() {
    this._formdata = new FormData();

  }

  set formdata(formdata: FormData) {
    this._formdata = formdata
  }
  get formdata() {
    return this._formdata;
  }
  addFile(file: File) {
    this.formdata.set('File', file)
  }
  addentitiesString(entitiesString: Entity) {
    //delete id from each champ
    entitiesString[Object.keys(entitiesString)[0]].forEach(champ => {
      delete champ.id
    })
    this.formdata.set("entitiesString", JSON.stringify(entitiesString))
  }
  addConfig(configString: Config) {
    this.formdata.set('configString', JSON.stringify(configString))
  }
  submitFormData(): Observable<CodeGenerated> {
    console.log(this.formdata.get('entitiesString'));

    return this.http.post<CodeGenerated>(this.api, this.formdata)
  }
}
