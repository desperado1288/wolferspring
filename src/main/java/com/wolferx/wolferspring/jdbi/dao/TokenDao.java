package com.wolferx.wolferspring.jdbi.dao;

import com.wolferx.wolferspring.entity.Token;
import com.wolferx.wolferspring.jdbi.mapper.TokenMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.Date;

@RegisterMapper(TokenMapper.class)
public abstract class TokenDao {

    @SqlUpdate(
        "INSERT INTO token(user_id, device, ip, refresh_token, time_created, time_updated) " +
            "VALUES (:user_id, :device, :ip, :refresh_token, :time_created, :time_created)")
    public abstract int create(
        @Bind("user_id") Long userId,
        @Bind("device") String device,
        @Bind("ip") String ip,
        @Bind("refresh_token") String freshToken,
        @Bind("time_created") Date timeCreated);

    @SqlUpdate(
        "UPDATE token SET device = :device, ip = :ip, refresh_token = :refresh_token, " +
            "time_updated = :time_updated WHERE user_id = :user_id")
    public abstract int update(
        @Bind("user_id") Long userId,
        @Bind("device") String device,
        @Bind("ip") String ip,
        @Bind("refresh_token") String refreshToken,
        @Bind("time_updated") Date timeUpdate);

    @SqlQuery("SELECT refresh_token FROM token WHERE user_id = :user_id")
    public abstract Token getByUserId(@Bind("user_id") Long userId);

    @SqlQuery("SELECT refresh_token FROM token WHERE user_id = :user_id")
    public abstract String getRefreshTokenByUserId(@Bind("user_id") Long userId);

    @Transaction
    public Integer upsert(Long userId, String device, String ip, String refreshToken, Date timeNow) {

        if (getRefreshTokenByUserId(userId).isEmpty()) {
            return create(userId, device, ip, refreshToken, timeNow);
        }
        return update(userId, device, ip, refreshToken, timeNow);
    }
}
