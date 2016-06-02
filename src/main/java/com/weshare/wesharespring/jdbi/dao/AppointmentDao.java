package com.weshare.wesharespring.jdbi.dao;

import com.weshare.wesharespring.entity.Topic;
import com.weshare.wesharespring.jdbi.mapper.AppointmentMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(AppointmentMapper.class)
public abstract class AppointmentDao {

    @SqlQuery("SELECT * FROM appointment WHERE appointment_id  = :appointment_id")
    public abstract Topic getById(
        @Bind("appointment_id") final Long appointmentId);

    @SqlUpdate(
        "INSERT INTO appointment(user_id, topic_id, meetup_time, meetup_address, status, time_created, time_updated)" +
            "VALUES (:user_id, :topic_id, :meetup_time, :meetup_address, :status, :time_now, :time_now)")
    @GetGeneratedKeys
    public abstract Long create(
        @Bind("user_id") final Long userId,
        @Bind("topic_id") final Long topicId,
        @Bind("meetup_time") final Long meetUpTime,
        @Bind("meetup_address") final String meetUpAddress,
        @Bind("status") final Integer status,
        @Bind("time_now") final Long timeNow);

    abstract void close();
}
