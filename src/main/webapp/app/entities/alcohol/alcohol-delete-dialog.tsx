import React, { useEffect, useState } from 'react';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { deleteEntity, getEntity } from './alcohol.reducer';

export const AlcoholDeleteDialog = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const alcoholEntity = useAppSelector(state => state.alcohol.entity);
  const updateSuccess = useAppSelector(state => state.alcohol.updateSuccess);

  const handleClose = () => {
    navigate('/alcohol');
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(alcoholEntity.id));
  };

  return (
    <Modal show onHide={handleClose}>
      <ModalHeader data-cy="alcoholDeleteDialogHeading" closeButton>
        削除の確認
      </ModalHeader>
      <ModalBody id="sakeLogApp.alcohol.delete.question">酒 {alcoholEntity.id}を削除してもよろしいですか？</ModalBody>
      <ModalFooter>
        <Button variant="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; キャンセル
        </Button>
        <Button id="jhi-confirm-delete-alcohol" data-cy="entityConfirmDeleteButton" variant="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; 削除
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default AlcoholDeleteDialog;
