import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './drink-log.reducer';

export const DrinkLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const drinkLogEntity = useAppSelector(state => state.drinkLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="drinkLogDetailsHeading">飲酒ログ</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{drinkLogEntity.id}</dd>
          <dt>
            <span id="drinkDate">飲酒日</span>
          </dt>
          <dd>{drinkLogEntity.drinkDate ? <TextFormat value={drinkLogEntity.drinkDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="quantity">杯数</span>
          </dt>
          <dd>{drinkLogEntity.quantity}</dd>
          <dt>
            <span id="rating">評価</span>
          </dt>
          <dd>{drinkLogEntity.rating}</dd>
          <dt>
            <span id="memo">メモ</span>
          </dt>
          <dd>{drinkLogEntity.memo}</dd>
          <dt>
            <span id="createdAt">作成日</span>
          </dt>
          <dd>{drinkLogEntity.createdAt ? <TextFormat value={drinkLogEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>User</dt>
          <dd>{drinkLogEntity.user ? drinkLogEntity.user.id : ''}</dd>
          <dt>Alcohol</dt>
          <dd>{drinkLogEntity.alcohol ? drinkLogEntity.alcohol.id : ''}</dd>
          <dt>Place</dt>
          <dd>{drinkLogEntity.place ? drinkLogEntity.place.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/drink-log" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">戻る</span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/drink-log/${drinkLogEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">編集</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DrinkLogDetail;
