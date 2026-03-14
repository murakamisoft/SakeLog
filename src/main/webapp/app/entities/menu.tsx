import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/alcohol">
        酒
      </MenuItem>
      <MenuItem icon="asterisk" to="/place">
        場所
      </MenuItem>
      <MenuItem icon="asterisk" to="/drink-log">
        飲酒ログ
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
