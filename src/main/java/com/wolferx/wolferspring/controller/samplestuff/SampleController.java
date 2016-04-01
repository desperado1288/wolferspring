package com.wolferx.wolferspring.controller.samplestuff;

import com.wolferx.wolferspring.common.annotation.LoggedUser;
import com.wolferx.wolferspring.config.RouteConfig;
import com.wolferx.wolferspring.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ROLE_DOMAIN_USER')")
public class SampleController {
    private final ServiceGateway serviceGateway;

    @Autowired
    public SampleController(ServiceGateway serviceGateway) {
        this.serviceGateway = serviceGateway;
    }

    @RequestMapping(value = RouteConfig.STUFF_URL, method = RequestMethod.GET)
    public List<User> getSomeStuff() {
        return serviceGateway.getSomeStuff();
    }

    @RequestMapping(value = RouteConfig.STUFF_URL, method = RequestMethod.POST)
    public void createStuff(@RequestBody User newStuff, @LoggedUser User domainUser) {
        serviceGateway.createStuff(newStuff, domainUser);
    }
}
