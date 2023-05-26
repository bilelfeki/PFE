import { Injectable } from '@angular/core';
import { Champ, Entity } from './interface/entity';
import { EntityService } from './entity.service';

@Injectable({
  providedIn: 'root'
})
export class EntityHandlerService {

  entity!: Entity;
  createEmptyEntity(): Entity {
    return {
      "": [{
        id: 1,
        valeur: "champ",
        valeur_min: 0,
        valeur_max: 10000,
        type: "String"
      }]
    }
  }
  //this method should only used when you want to rename the entity 
  renameEntity(entity: Entity, entityName: string) {
    const keys = Object.keys(entity);
    if (keys.length > 0) {
      entity[keys[0]] = entity[entityName]
    }
  }

  updateEntityKey(entityName: string, entity: Entity) {
    const tempChamps: Champ[] = entity[Object.keys(entity)[0]]
    delete entity[Object.keys(this.entity)[0]]
    entity[entityName] = tempChamps;
    return entity;
  }

}
