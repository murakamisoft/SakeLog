import React, { useEffect, useState } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { useSearchParams } from 'react-router';

import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { handlePasswordResetFinish, reset } from '../password-reset.reducer';

export const PasswordResetFinishPage = () => {
  const dispatch = useAppDispatch();

  const [searchParams] = useSearchParams();
  const key = searchParams.get('key');

  const [password, setPassword] = useState('');

  useEffect(
    () => () => {
      dispatch(reset());
    },
    [],
  );

  const handleValidSubmit = ({ newPassword }) => dispatch(handlePasswordResetFinish({ key, newPassword }));

  const updatePassword = event => setPassword(event.target.value);

  const getResetForm = () => {
    return (
      <ValidatedForm onSubmit={handleValidSubmit}>
        <ValidatedField
          name="newPassword"
          label="新しいパスワード"
          placeholder="新しいパスワード"
          type="password"
          validate={{
            required: { value: true, message: 'パスワードを入力してください。' },
            minLength: { value: 4, message: 'パスワードは4文字以上で入力してください。' },
            maxLength: { value: 50, message: 'パスワードは50文字以下で入力してください。' },
          }}
          onChange={updatePassword}
          data-cy="resetPassword"
        />
        <PasswordStrengthBar password={password} />
        <ValidatedField
          name="confirmPassword"
          label="新しいパスワード(確認)"
          placeholder="新しいパスワード(確認)"
          type="password"
          validate={{
            required: { value: true, message: 'パスワード(確認)を入力してください。' },
            minLength: { value: 4, message: 'パスワード(確認)は4文字以上で入力してください。' },
            maxLength: { value: 50, message: 'パスワード(確認)は50文字以下で入力してください。' },
            validate: v => v === password || 'パスワードが一致しません!',
          }}
          data-cy="confirmResetPassword"
        />
        <Button variant="success" type="submit" data-cy="submit">
          パスワードの再設定
        </Button>
      </ValidatedForm>
    );
  };

  const successMessage = useAppSelector(state => state.passwordReset.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="4">
          <h1>パスワードのリセット</h1>
          <div>{key && getResetForm()}</div>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordResetFinishPage;
