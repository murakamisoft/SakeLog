import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Alcohol from './alcohol';
import DrinkLog from './drink-log';
import Place from './place';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="/alcohol/*" element={<Alcohol />} />
        <Route path="/place/*" element={<Place />} />
        <Route path="/drink-log/*" element={<DrinkLog />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
