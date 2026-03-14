import React, { useEffect, useState } from 'react';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'react-bootstrap';
import { useLocation, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { deleteEntity, getEntity } from './drink-log.reducer';

export const DrinkLogDeleteDialog = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const drinkLogEntity = useAppSelector(state => state.drinkLog.entity);
  const updateSuccess = useAppSelector(state => state.drinkLog.updateSuccess);

  const handleClose = () => {
    navigate(`/drink-log${pageLocation.search}`);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(drinkLogEntity.id));
  };

  return (
    <Modal show onHide={handleClose}>
      <ModalHeader data-cy="drinkLogDeleteDialogHeading" closeButton>
        削除の確認
      </ModalHeader>
      <ModalBody id="sakeLogApp.drinkLog.delete.question">飲酒ログ {drinkLogEntity.id}を削除してもよろしいですか？</ModalBody>
      <ModalFooter>
        <Button variant="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; キャンセル
        </Button>
        <Button id="jhi-confirm-delete-drinkLog" data-cy="entityConfirmDeleteButton" variant="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; 削除
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default DrinkLogDeleteDialog;
