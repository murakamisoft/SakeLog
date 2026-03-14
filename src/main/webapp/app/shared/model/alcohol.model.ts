export interface IAlcohol {
  id?: number;
  alcoholName?: string;
  alcoholType?: string | null;
  brandName?: string | null;
  abv?: number | null;
}

export const defaultValue: Readonly<IAlcohol> = {};
