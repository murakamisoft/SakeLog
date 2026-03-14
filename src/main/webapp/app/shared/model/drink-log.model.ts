import dayjs from 'dayjs';

import { IAlcohol } from 'app/shared/model/alcohol.model';
import { IPlace } from 'app/shared/model/place.model';
import { IUser } from 'app/shared/model/user.model';

export interface IDrinkLog {
  id?: number;
  drinkDate?: dayjs.Dayjs;
  quantity?: number | null;
  rating?: number | null;
  memo?: string | null;
  createdAt?: dayjs.Dayjs | null;
  user?: IUser | null;
  alcohol?: IAlcohol | null;
  place?: IPlace | null;
}

export const defaultValue: Readonly<IDrinkLog> = {};
