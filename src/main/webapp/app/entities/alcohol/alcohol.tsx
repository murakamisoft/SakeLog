import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { getSortState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC } from 'app/shared/util/pagination.constants';

import { getEntities } from './alcohol.reducer';

export const Alcohol = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const alcoholList = useAppSelector(state => state.alcohol.entities);
  const loading = useAppSelector(state => state.alcohol.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="alcohol-heading" data-cy="AlcoholHeading">
        酒
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> リストの更新
          </Button>
          <Link to="/alcohol/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; 酒の作成
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {alcoholList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('alcoholName')}>
                  酒名 <FontAwesomeIcon icon={getSortIconByFieldName('alcoholName')} />
                </th>
                <th className="hand" onClick={sort('alcoholType')}>
                  酒種類 <FontAwesomeIcon icon={getSortIconByFieldName('alcoholType')} />
                </th>
                <th className="hand" onClick={sort('brandName')}>
                  メーカー名 <FontAwesomeIcon icon={getSortIconByFieldName('brandName')} />
                </th>
                <th className="hand" onClick={sort('abv')}>
                  アルコール度数 <FontAwesomeIcon icon={getSortIconByFieldName('abv')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {alcoholList.map(alcohol => (
                <tr key={`entity-${alcohol.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/alcohol/${alcohol.id}`} variant="link" size="sm">
                      {alcohol.id}
                    </Button>
                  </td>
                  <td>{alcohol.alcoholName}</td>
                  <td>{alcohol.alcoholType}</td>
                  <td>{alcohol.brandName}</td>
                  <td>{alcohol.abv}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button as={Link as any} to={`/alcohol/${alcohol.id}`} variant="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">表示</span>
                      </Button>
                      <Button as={Link as any} to={`/alcohol/${alcohol.id}/edit`} variant="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">編集</span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/alcohol/${alcohol.id}/delete`)}
                        variant="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">削除</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">酒が見つかりません。</div>
        )}
      </div>
    </div>
  );
};

export default Alcohol;
