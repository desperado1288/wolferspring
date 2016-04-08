package com.wolferx.wolferspring.common.security.external;

public abstract class ServiceGatewayBase {
    private AuthenticatedExternalServiceProvider authenticatedExternalServiceProvider;

    public ServiceGatewayBase(AuthenticatedExternalServiceProvider authenticatedExternalServiceProvider) {
        this.authenticatedExternalServiceProvider = authenticatedExternalServiceProvider;
    }

    protected ExternalWebServiceStub externalService() {
        return authenticatedExternalServiceProvider.provide().getExternalWebService();
    }
}
