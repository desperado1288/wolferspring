package com.weshare.wesharespring.jdbi.dao;


import com.weshare.wesharespring.entity.Appointment;
import com.weshare.wesharespring.entity.Position;
import com.weshare.wesharespring.entity.Topic;
import com.weshare.wesharespring.jdbi.mapper.PositionMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import java.util.List;

@RegisterMapper(PositionMapper.class)
public abstract class PositionDao {

    @SqlQuery("SELECT * FROM position WHERE position_id = :position_id")
    public abstract Position getById(@Bind("position_id") final Long position_id);

    @SqlQuery("SELECT * FROM position WHERE user_id = :user_id")
    public abstract  List<Position> getByUserId(@Bind("user_id") final Long user_id);


    abstract void close();
}
