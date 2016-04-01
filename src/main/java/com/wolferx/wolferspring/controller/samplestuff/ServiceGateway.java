package com.wolferx.wolferspring.controller.samplestuff;

import com.wolferx.wolferspring.entity.User;

import java.util.List;

public interface ServiceGateway {
    List<User> getSomeStuff();

    void createStuff(User newStuff, User domainUser);
}
