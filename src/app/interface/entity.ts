export interface Entity {
    [key: string]: Champ[];

}
export interface Champ {
    id?: number
    valeur: string;
    valeur_min: number;
    valeur_max: number;
    type: string;
}