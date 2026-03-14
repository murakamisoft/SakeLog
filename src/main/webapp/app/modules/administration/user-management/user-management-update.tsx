import React, { useEffect } from 'react';
import { Button, Col, FormText, Row } from 'react-bootstrap';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { faArrowLeft, faSave } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createUser, getRoles, getUser, reset, updateUser } from './user-management.reducer';

export const UserManagementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { login } = useParams<'login'>();
  const isNew = login === undefined;

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getUser(login));
    }
    dispatch(getRoles());
    return () => {
      dispatch(reset());
    };
  }, [login]);

  const handleClose = () => {
    navigate('/admin/user-management');
  };

  const saveUser = values => {
    if (isNew) {
      dispatch(createUser(values));
    } else {
      dispatch(updateUser(values));
    }
    handleClose();
  };

  const user = useAppSelector(state => state.userManagement.user);
  const loading = useAppSelector(state => state.userManagement.loading);
  const updating = useAppSelector(state => state.userManagement.updating);
  const authorities = useAppSelector(state => state.userManagement.authorities);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1 data-cy="UserManagementCreateUpdateHeading">ユーザーの作成または編集</h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm onSubmit={saveUser} defaultValues={user}>
              {user.id && <ValidatedField type="text" name="id" data-cy="id" required readOnly label="ID" validate={{ required: true }} />}
              <ValidatedField
                type="text"
                name="login"
                data-cy="login"
                label="ログイン"
                validate={{
                  required: {
                    value: true,
                    message: 'ユーザー名が必要です。',
                  },
                  pattern: {
                    value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                    message: 'ユーザー名が無効です',
                  },
                  minLength: {
                    value: 1,
                    message: 'ユーザー名は1文字以上で入力してください',
                  },
                  maxLength: {
                    value: 50,
                    message: 'ユーザー名は50文字以下で入力してください',
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="firstName"
                data-cy="firstName"
                label="名"
                validate={{
                  maxLength: {
                    value: 50,
                    message: 'このフィールドは50文字以下で入力してください。',
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="lastName"
                data-cy="lastName"
                label="姓"
                validate={{
                  maxLength: {
                    value: 50,
                    message: 'このフィールドは50文字以下で入力してください。',
                  },
                }}
              />
              <FormText>This field cannot be longer than 50 characters.</FormText>
              <ValidatedField
                name="email"
                data-cy="email"
                label="メールアドレス"
                placeholder="あなたのメールアドレス"
                type="email"
                validate={{
                  required: {
                    value: true,
                    message: 'メールアドレスを入力してください。',
                  },
                  minLength: {
                    value: 5,
                    message: 'メールアドレスは5文字以上で入力してください。',
                  },
                  maxLength: {
                    value: 254,
                    message: 'メールアドレスは50文字以下で入力してください。',
                  },
                  validate: v => isEmail(v) || '正しいメールアドレスを入力してください。',
                }}
              />
              <ValidatedField type="checkbox" name="activated" data-cy="activated" check value={true} disabled={!user.id} label="有効" />
              <ValidatedField type="select" name="authorities" data-cy="profiles" multiple label="プロファイル">
                {authorities.map(role => (
                  <option value={role} key={role}>
                    {role}
                  </option>
                ))}
              </ValidatedField>
              <Button as={Link as any} to="/admin/user-management" replace variant="info" data-cy="entityCreateCancelButton">
                <FontAwesomeIcon icon={faArrowLeft} />
                &nbsp;
                <span className="d-none d-md-inline">戻る</span>
              </Button>
              &nbsp;
              <Button variant="primary" type="submit" disabled={updating} data-cy="entityCreateSaveButton">
                <FontAwesomeIcon icon={faSave} />
                &nbsp; 保存
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UserManagementUpdate;
