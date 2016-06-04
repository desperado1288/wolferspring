package com.weshare.wesharespring.jdbi.dao;

import com.weshare.wesharespring.entity.Appointment;
import com.weshare.wesharespring.entity.Topic;
import com.weshare.wesharespring.jdbi.mapper.AppointmentMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import java.util.List;

@RegisterMapper(AppointmentMapper.class)
public abstract class AppointmentDao {

    @SqlQuery("SELECT * FROM appointment WHERE appointment_id  = :appointment_id")
    public abstract Appointment getById(
        @Bind("appointment_id") final Long appointmentId);

    @SqlQuery("SELECT * FROM appointment WHERE user_id = :user_id")
    public abstract List<Appointment> getByUserId(
        @Bind("user_id") final Long user_id
    );

    @SqlQuery("SELECT * FROM appointment WHERE topic_id = :topic_id")
    public abstract List<Appointment> getByTopicId(
        @Bind("topic_id") final Long topic_id
    );

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

    @SqlUpdate(
        "UPDATE appointment SET meetup_time = :meetup_time, meetup_address = :meetup_address, " +
            "time_updated = :time_updated WHERE appointment_id = :appointment_id")
    public abstract int updateTimeOrAddress(
        @Bind("appointment_id") final Long appointment_id,
        @Bind("meetup_time") final Long meetup_time,
        @Bind("meetup_address") final String meetup_address,
        @Bind("time_updated") final Long time_updated
    );

    abstract void close();
}
