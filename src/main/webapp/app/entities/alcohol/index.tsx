import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Alcohol from './alcohol';
import AlcoholDeleteDialog from './alcohol-delete-dialog';
import AlcoholDetail from './alcohol-detail';
import AlcoholUpdate from './alcohol-update';

const AlcoholRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Alcohol />} />
    <Route path="new" element={<AlcoholUpdate />} />
    <Route path=":id">
      <Route index element={<AlcoholDetail />} />
      <Route path="edit" element={<AlcoholUpdate />} />
      <Route path="delete" element={<AlcoholDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AlcoholRoutes;
