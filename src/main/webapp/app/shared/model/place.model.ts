export interface IPlace {
  id?: number;
  placeName?: string;
  placeType?: string | null;
  city?: string | null;
}

export const defaultValue: Readonly<IPlace> = {};
