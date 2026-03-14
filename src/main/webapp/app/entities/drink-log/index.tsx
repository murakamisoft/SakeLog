import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DrinkLog from './drink-log';
import DrinkLogDeleteDialog from './drink-log-delete-dialog';
import DrinkLogDetail from './drink-log-detail';
import DrinkLogUpdate from './drink-log-update';

const DrinkLogRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DrinkLog />} />
    <Route path="new" element={<DrinkLogUpdate />} />
    <Route path=":id">
      <Route index element={<DrinkLogDetail />} />
      <Route path="edit" element={<DrinkLogUpdate />} />
      <Route path="delete" element={<DrinkLogDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DrinkLogRoutes;
