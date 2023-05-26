import { Component, EventEmitter, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Champ, Entity } from '../interface/entity';
import { EntityService } from '../entity.service';
import { EntityHandlerService } from '../entity-handler.service';



@Component({
  selector: 'app-entity',
  templateUrl: './entity.component.html',
  styleUrls: ['./entity.component.css'],
  providers: []
})
export class EntityComponent {
  @Output() entityEmitter = new EventEmitter<Entity>();
  entityName: string
  entity: Entity
  champs: Champ[];
  constructor(private entityHandlerService: EntityHandlerService) {
    this.entityName = ""
    this.entity = this.entityHandlerService.createEmptyEntity();
    this.champs = this.entity[""]
  }

  addChamp() {
    this.champs.push({ ...this.champs[0], id: this.champs.length + 1 })
  }
  onEntityNameChange() {
    //this.entity = this.entityHandlerService.updateEntityKey(this.entityName, this.entity)
    this.updateEntityKey();
  }
  private updateEntityKey() {
    const tempChamps: Champ[] = this.entity[Object.keys(this.entity)[0]]
    delete this.entity[Object.keys(this.entity)[0]]
    this.entity[this.entityName] = tempChamps;
  }
  submitEntity() {
    this.entityEmitter.emit(this.entity)
    this.entity = this.entityHandlerService.createEmptyEntity();
  }
}