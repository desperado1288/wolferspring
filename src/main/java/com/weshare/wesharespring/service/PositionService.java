package com.weshare.wesharespring.service;

import com.weshare.wesharespring.common.constant.Constant;
import com.weshare.wesharespring.common.exception.DuplicateItemException;
import com.weshare.wesharespring.common.exception.ItemNotFoundException;
import com.weshare.wesharespring.common.exception.StorageServiceException;
import com.weshare.wesharespring.common.utils.Utils;
import com.weshare.wesharespring.entity.Appointment;
import com.weshare.wesharespring.entity.Response.TopicResponse;
import com.weshare.wesharespring.entity.Position;
import com.weshare.wesharespring.jdbi.dao.AppointmentDao;
import com.weshare.wesharespring.jdbi.dao.PositionDao;
import com.weshare.wesharespring.jdbi.dao.TopicDao;
import org.skife.jdbi.v2.exceptions.DBIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PositionService {

    private final PositionDao positionDao;

    @Autowired
    public PositionService(final PositionDao positionDao) { this.positionDao = positionDao; }

    public Position getPositionById(final Long position_id)
        throws ItemNotFoundException, StorageServiceException {

        try {
            final Position position = positionDao.getById(position_id);

            if (position == null) {
                throw new ItemNotFoundException();
            }

            return position;
        } catch (final DBIException dbiException) {
            throw new StorageServiceException(dbiException);
        }
    }

    public List<Position> getPositionByUserId(final Long user_id)
        throws ItemNotFoundException, StorageServiceException {

        try {
            final List<Position> positionList = positionDao.getByUserId(user_id);

            if (positionList.isEmpty()) {
                throw new ItemNotFoundException();
            }

            return positionList;
        } catch (final DBIException dbiException) {
            throw new StorageServiceException(dbiException);
        }
    }
}
