package com.weshare.wesharespring.service;

/**
 * Created by yiding on 6/2/16.
 */
import com.weshare.wesharespring.common.constant.Constant;
import com.weshare.wesharespring.common.exception.DuplicateItemException;
import com.weshare.wesharespring.common.exception.ItemNotFoundException;
import com.weshare.wesharespring.common.exception.StorageServiceException;
import com.weshare.wesharespring.common.utils.Utils;
import com.weshare.wesharespring.entity.Appointment;
import com.weshare.wesharespring.entity.Response.TopicResponse;
import com.weshare.wesharespring.entity.Topic;
import com.weshare.wesharespring.jdbi.dao.AppointmentDao;
import com.weshare.wesharespring.jdbi.dao.TopicDao;
import org.skife.jdbi.v2.exceptions.DBIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentDao appointmentDao;

    @Autowired
    public AppointmentService(final AppointmentDao appointmentDao) { this.appointmentDao = appointmentDao; }

    public Appointment getById(final Long appointment_id)
        throws ItemNotFoundException, StorageServiceException {

        try {
            final Appointment appointment = appointmentDao.getById(appointment_id);
            if (appointment == null) {
                throw new ItemNotFoundException();
            }
            return appointment;
        } catch (final DBIException e) {
            throw new StorageServiceException(e);
        }
    }

    public List<Appointment> getByUserId(final Long user_id)
        throws ItemNotFoundException, StorageServiceException {

        try {
            final List<Appointment> appointmentList = appointmentDao.getByUserId(user_id);
            if (appointmentList.isEmpty()) {
                throw new ItemNotFoundException();
            }
            return appointmentList;
        } catch (final DBIException e) {
            throw new StorageServiceException(e);
        }
    }

    public List<Appointment> getByTopicId(final Long topic_id)
        throws ItemNotFoundException, StorageServiceException {

        try {
            final List<Appointment> appointmentList = appointmentDao.getByTopicId(topic_id);
            if (appointmentList.isEmpty()) {
                throw new ItemNotFoundException();
            }
            return appointmentList;
        } catch (final DBIException e) {
            throw new StorageServiceException(e);
        }
    }

    public Appointment createAppointment(final Appointment appointment)
        throws DuplicateItemException, ItemNotFoundException, StorageServiceException {

        try {
            final Long timeNow = Utils.getCurrentTimeStamp();
            final Long appointment_id = appointmentDao.create(appointment.getUserId(), appointment.getTopicId(),
                appointment.getMeetupTime(), appointment.getMeetupAddress(), Constant.APPOINTMENT_STATUS_ACTIVE, timeNow);

            return getById(appointment_id);
        } catch (final DBIException dbiException) {
            if (Utils.isDuplicateEntryException(dbiException)) {
                throw new DuplicateItemException(dbiException);
            }
            throw new StorageServiceException(dbiException);
        }
    }

    public Appointment updateAppointment(final Appointment appointment)
        throws ItemNotFoundException, StorageServiceException {

        try {
            final Long timeNow = Utils.getCurrentTimeStamp();
            final int change = appointmentDao.updateTimeOrAddress(appointment.getAppointmentId(), appointment.getMeetupTime(),
                appointment.getMeetupAddress(), timeNow);
            if (change == 0) {
                throw new ItemNotFoundException();
            }
            return getById(appointment.getAppointmentId());
        } catch (final DBIException e) {
            throw new StorageServiceException(e);
        }
    }
}
