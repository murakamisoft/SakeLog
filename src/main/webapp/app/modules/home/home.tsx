import './home.scss';

import React from 'react';
import { Alert, Col, Row } from 'react-bootstrap';
import { Link } from 'react-router';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h1 className="display-4">ようこそ, Java Hipster!</h1>
        <p className="lead">ここはトップページです。</p>
        {account?.login ? (
          <div>
            <Alert variant="success">&quot;{account.login}&quot;としてログインしています。</Alert>
          </div>
        ) : (
          <div>
            <Alert variant="warning">
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                ログイン
              </Link>
              を試したければ, デフォルトアカウント:
              <br />- 管理者 (ユーザー名=&quot;admin&quot;, パスワード=&quot;admin&quot;) <br />- ユーザー (login=&quot;user&quot;,
              password=&quot;user&quot;).
            </Alert>

            <Alert variant="warning">
              アカウントをまだお持ちでないですか?&nbsp;
              <Link to="/account/register" className="alert-link">
                アカウントを登録する
              </Link>
            </Alert>
          </div>
        )}
        <p>JHipsterに関するお問い合わせは、こちらまで:</p>

        <ul>
          <li>
            <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer">
              JHipsterのサイト
            </a>
          </li>
          <li>
            <a href="https://stackoverflow.com/tags/jhipster/info" target="_blank" rel="noopener noreferrer">
              Stack Overflow のJHipster
            </a>
          </li>
          <li>
            <a href="https://github.com/jhipster/generator-jhipster/issues?state=open" target="_blank" rel="noopener noreferrer">
              JHipsterのバグトラッカー
            </a>
          </li>
          <li>
            <a href="https://gitter.im/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
              JHipsterの公開チャットルーム
            </a>
          </li>
          <li>
            <a href="https://twitter.com/jhipster" target="_blank" rel="noopener noreferrer">
              Twitterの @jhipster
            </a>
          </li>
        </ul>

        <p>
          JHipsterを気に入ったら、是非スターをつけてください。{' '}
          <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
            GitHub
          </a>
          !
        </p>
      </Col>
    </Row>
  );
};

export default Home;
