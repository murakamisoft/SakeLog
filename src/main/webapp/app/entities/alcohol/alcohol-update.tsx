import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './alcohol.reducer';

export const AlcoholUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const alcoholEntity = useAppSelector(state => state.alcohol.entity);
  const loading = useAppSelector(state => state.alcohol.loading);
  const updating = useAppSelector(state => state.alcohol.updating);
  const updateSuccess = useAppSelector(state => state.alcohol.updateSuccess);

  const handleClose = () => {
    navigate('/alcohol');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.abv !== undefined && typeof values.abv !== 'number') {
      values.abv = Number(values.abv);
    }

    const entity = {
      ...alcoholEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...alcoholEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sakeLogApp.alcohol.home.createOrEditLabel" data-cy="AlcoholCreateUpdateHeading">
            酒の作成または編集
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew && <ValidatedField name="id" required readOnly id="alcohol-id" label="ID" validate={{ required: true }} />}
              <ValidatedField
                label="Alcohol Name"
                id="alcohol-alcoholName"
                name="alcoholName"
                data-cy="alcoholName"
                type="text"
                validate={{
                  required: { value: true, message: 'このフィールドを入力してください。' },
                  maxLength: { value: 200, message: 'このフィールドは200文字以下で入力してください。' },
                }}
              />
              <ValidatedField
                label="Alcohol Type"
                id="alcohol-alcoholType"
                name="alcoholType"
                data-cy="alcoholType"
                type="text"
                validate={{
                  maxLength: { value: 50, message: 'このフィールドは50文字以下で入力してください。' },
                }}
              />
              <ValidatedField
                label="Brand Name"
                id="alcohol-brandName"
                name="brandName"
                data-cy="brandName"
                type="text"
                validate={{
                  maxLength: { value: 200, message: 'このフィールドは200文字以下で入力してください。' },
                }}
              />
              <ValidatedField label="Abv" id="alcohol-abv" name="abv" data-cy="abv" type="text" />
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/alcohol" replace variant="info">
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

export default AlcoholUpdate;
