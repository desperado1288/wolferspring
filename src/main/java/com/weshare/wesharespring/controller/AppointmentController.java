package com.weshare.wesharespring.controller;

/**
 * Created by yiding on 6/2/16.
 */

import com.weshare.wesharespring.common.annotation.LoggedUser;
import com.weshare.wesharespring.common.exception.BaseServiceException;
import com.weshare.wesharespring.config.RouteConfig;
import com.weshare.wesharespring.entity.Appointment;
import com.weshare.wesharespring.entity.Response.SuccessResponse;
import com.weshare.wesharespring.entity.Response.TopicResponse;
import com.weshare.wesharespring.entity.Topic;
import com.weshare.wesharespring.entity.User;
import com.weshare.wesharespring.service.AppointmentService;
import com.weshare.wesharespring.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = RouteConfig.APPOINTMENT_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(final AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @RequestMapping(value = "/{appointmentId}", method = RequestMethod.GET)
    public Appointment getAppById(@PathVariable("appointmentId") final Long appointmentId)
        throws BaseServiceException {

        logger.info("<Start> getAppointmentById(): AppointmentId: {}", appointmentId);
        Appointment appointment = appointmentService.getById(appointmentId);
        logger.info("<End> getAppointmentById(): AppointmentId: {}", appointmentId);

        return appointment;
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<Appointment> getAppByUserId(@PathVariable("userId") final Long userId)
        throws BaseServiceException {

        logger.info("<Start> getAppointmentByUserId(): userId: {}", userId);
        List<Appointment> appointmentList = appointmentService.getByUserId(userId);
        logger.info("<End> getAppointmentByUserId(): userId: {}", userId);

        return appointmentList;
    }

    @RequestMapping(value = "/topic/{topicId}", method = RequestMethod.GET)
    public List<Appointment> getAppByTopicId(@PathVariable("topicId") final Long topicId)
        throws BaseServiceException {

        logger.info("<Start> getAppointmentByTopicId(): TopicId: {}", topicId);
        List<Appointment> appointmentList = appointmentService.getByTopicId(topicId);
        logger.info("<End> getAppointmentByTopicId(): TopicId: {}", topicId);

        return appointmentList;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Appointment createApp(@RequestBody final Appointment app, @LoggedUser final User user)
        throws BaseServiceException {

        logger.info("<Start> createAppointment()");
        app.setUserId(user.getUserId());
        final Appointment appResult = appointmentService.createAppointment(app);
        logger.info("<End> createAppointment()");

        return appResult;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Appointment updateApp(@RequestBody final Appointment app, @LoggedUser final User user)
        throws BaseServiceException {

        logger.info("<Start> updateAppointment()");
        app.setUserId(user.getUserId());
        final Appointment appResult = appointmentService.updateAppointment(app);
        logger.info("<End> updateAppointment()");

        return appResult;
    }
}
