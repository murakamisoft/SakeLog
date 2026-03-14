import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { JhiItemCount, JhiPagination, TextFormat, getPaginationState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';

import { getEntities } from './drink-log.reducer';

export const DrinkLog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const drinkLogList = useAppSelector(state => state.drinkLog.entities);
  const loading = useAppSelector(state => state.drinkLog.loading);
  const totalItems = useAppSelector(state => state.drinkLog.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="drink-log-heading" data-cy="DrinkLogHeading">
        飲酒ログ
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> リストの更新
          </Button>
          <Link to="/drink-log/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; 飲酒ログの作成
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {drinkLogList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  ID <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('drinkDate')}>
                  飲酒日 <FontAwesomeIcon icon={getSortIconByFieldName('drinkDate')} />
                </th>
                <th className="hand" onClick={sort('quantity')}>
                  杯数 <FontAwesomeIcon icon={getSortIconByFieldName('quantity')} />
                </th>
                <th className="hand" onClick={sort('rating')}>
                  評価 <FontAwesomeIcon icon={getSortIconByFieldName('rating')} />
                </th>
                <th className="hand" onClick={sort('memo')}>
                  メモ <FontAwesomeIcon icon={getSortIconByFieldName('memo')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  作成日 <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th>
                  ユーザ <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  酒 <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  場所 <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {drinkLogList.map(drinkLog => (
                <tr key={`entity-${drinkLog.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/drink-log/${drinkLog.id}`} variant="link" size="sm">
                      {drinkLog.id}
                    </Button>
                  </td>
                  <td>{drinkLog.drinkDate ? <TextFormat type="date" value={drinkLog.drinkDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{drinkLog.quantity}</td>
                  <td>{drinkLog.rating}</td>
                  <td>{drinkLog.memo}</td>
                  <td>{drinkLog.createdAt ? <TextFormat type="date" value={drinkLog.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{drinkLog.user ? drinkLog.user.id : ''}</td>
                  <td>{drinkLog.alcohol ? <Link to={`/alcohol/${drinkLog.alcohol.id}`}>{drinkLog.alcohol.id}</Link> : ''}</td>
                  <td>{drinkLog.place ? <Link to={`/place/${drinkLog.place.id}`}>{drinkLog.place.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button as={Link as any} to={`/drink-log/${drinkLog.id}`} variant="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">表示</span>
                      </Button>
                      <Button
                        as={Link as any}
                        to={`/drink-log/${drinkLog.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        variant="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">編集</span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/drink-log/${drinkLog.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
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
          !loading && <div className="alert alert-warning">飲酒ログが見つかりません。</div>
        )}
      </div>
      {totalItems ? (
        <div className={drinkLogList && drinkLogList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default DrinkLog;
