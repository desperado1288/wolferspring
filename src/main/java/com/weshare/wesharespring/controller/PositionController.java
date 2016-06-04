package com.weshare.wesharespring.controller;

import com.weshare.wesharespring.common.annotation.LoggedUser;
import com.weshare.wesharespring.common.exception.BaseServiceException;
import com.weshare.wesharespring.config.RouteConfig;
import com.weshare.wesharespring.entity.Appointment;
import com.weshare.wesharespring.entity.Response.SuccessResponse;
import com.weshare.wesharespring.entity.Response.TopicResponse;
import com.weshare.wesharespring.entity.Position;
import com.weshare.wesharespring.entity.User;
import com.weshare.wesharespring.service.AppointmentService;
import com.weshare.wesharespring.service.PositionService;
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
@RequestMapping(value = RouteConfig.POSITION_URL, produces = {MediaType.APPLICATION_JSON_VALUE})
public class PositionController {

    private final PositionService positionService;

    private static final Logger logger = LoggerFactory.getLogger(PositionController.class);

    @Autowired
    public PositionController(final PositionService positionService) { this.positionService = positionService; }

    @RequestMapping(value = "/{position_id}", method = RequestMethod.GET)
    public Position getPositionById(@PathVariable("position_id") final Long position_id)
        throws BaseServiceException {
        logger.info("<Start> getPositionById(): position_id: {}", position_id);

        Position position = positionService.getPositionById(position_id);

        logger.info("<End> getPositionById(): position_id: {}", position_id);
        return position;
    }

    @RequestMapping(value = "/user/{user_id}", method = RequestMethod.GET)
    public List<Position> getPositionByUserId(@PathVariable("user_id") final Long user_id)
        throws BaseServiceException {
        logger.info("<Start> getPositionByUserId(): user_id: {}", user_id);

        List<Position> positionList = positionService.getPositionByUserId(user_id);

        logger.info("<End> getPositionByUserId(): user_id: {}", user_id);
        return positionList;
    }
}
