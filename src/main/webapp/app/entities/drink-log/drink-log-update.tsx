import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getAlcohols } from 'app/entities/alcohol/alcohol.reducer';
import { getEntities as getPlaces } from 'app/entities/place/place.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './drink-log.reducer';

export const DrinkLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const alcohols = useAppSelector(state => state.alcohol.entities);
  const places = useAppSelector(state => state.place.entities);
  const drinkLogEntity = useAppSelector(state => state.drinkLog.entity);
  const loading = useAppSelector(state => state.drinkLog.loading);
  const updating = useAppSelector(state => state.drinkLog.updating);
  const updateSuccess = useAppSelector(state => state.drinkLog.updateSuccess);

  const handleClose = () => {
    navigate(`/drink-log${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getAlcohols({}));
    dispatch(getPlaces({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.drinkDate = convertDateTimeToServer(values.drinkDate);
    if (values.quantity !== undefined && typeof values.quantity !== 'number') {
      values.quantity = Number(values.quantity);
    }
    if (values.rating !== undefined && typeof values.rating !== 'number') {
      values.rating = Number(values.rating);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...drinkLogEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      alcohol: alcohols.find(it => it.id.toString() === values.alcohol?.toString()),
      place: places.find(it => it.id.toString() === values.place?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          drinkDate: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
        }
      : {
          ...drinkLogEntity,
          drinkDate: convertDateTimeFromServer(drinkLogEntity.drinkDate),
          createdAt: convertDateTimeFromServer(drinkLogEntity.createdAt),
          user: drinkLogEntity?.user?.id,
          alcohol: drinkLogEntity?.alcohol?.id,
          place: drinkLogEntity?.place?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sakeLogApp.drinkLog.home.createOrEditLabel" data-cy="DrinkLogCreateUpdateHeading">
            飲酒ログの作成または編集
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew && <ValidatedField name="id" required readOnly id="drink-log-id" label="ID" validate={{ required: true }} />}
              <ValidatedField
                label="飲酒日"
                id="drink-log-drinkDate"
                name="drinkDate"
                data-cy="drinkDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'このフィールドを入力してください。' },
                }}
              />
              <ValidatedField label="杯数" id="drink-log-quantity" name="quantity" data-cy="quantity" type="text" />
              <ValidatedField label="評価" id="drink-log-rating" name="rating" data-cy="rating" type="text" />
              <ValidatedField
                label="メモ"
                id="drink-log-memo"
                name="memo"
                data-cy="memo"
                type="text"
                validate={{
                  maxLength: { value: 1000, message: 'このフィールドは1000文字以下で入力してください。' },
                }}
              />
              <ValidatedField
                label="作成日"
                id="drink-log-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="drink-log-user" name="user" data-cy="user" label="ユーザ" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="drink-log-alcohol" name="alcohol" data-cy="alcohol" label="酒" type="select">
                <option value="" key="0" />
                {alcohols
                  ? alcohols.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.alcoholName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="drink-log-place" name="place" data-cy="place" label="場所" type="select">
                <option value="" key="0" />
                {places
                  ? places.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.placeName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/drink-log" replace variant="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">戻る</span>
              </Button>
              &nbsp;
              <Button variant="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; 保存
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DrinkLogUpdate;
