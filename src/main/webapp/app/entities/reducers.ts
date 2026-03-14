import alcohol from 'app/entities/alcohol/alcohol.reducer';
import drinkLog from 'app/entities/drink-log/drink-log.reducer';
import place from 'app/entities/place/place.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  alcohol,
  place,
  drinkLog,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
