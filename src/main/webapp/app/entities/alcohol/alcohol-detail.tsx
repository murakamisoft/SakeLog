import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Link, useParams } from 'react-router';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './alcohol.reducer';

export const AlcoholDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const alcoholEntity = useAppSelector(state => state.alcohol.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="alcoholDetailsHeading">酒</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{alcoholEntity.id}</dd>
          <dt>
            <span id="alcoholName">酒名</span>
          </dt>
          <dd>{alcoholEntity.alcoholName}</dd>
          <dt>
            <span id="alcoholType">酒種類</span>
          </dt>
          <dd>{alcoholEntity.alcoholType}</dd>
          <dt>
            <span id="brandName">メーカー名</span>
          </dt>
          <dd>{alcoholEntity.brandName}</dd>
          <dt>
            <span id="abv">アルコール度数</span>
          </dt>
          <dd>{alcoholEntity.abv}</dd>
        </dl>
        <Button as={Link as any} to="/alcohol" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">戻る</span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/alcohol/${alcoholEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">編集</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AlcoholDetail;
