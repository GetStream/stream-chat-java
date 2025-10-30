package io.getstream.chat.java.services.framework;

import retrofit2.Retrofit;

import static java.lang.reflect.Proxy.newProxyInstance;

class UserServiceFactory {

  private final Retrofit retrofit;

  public UserServiceFactory(Retrofit retrofit) {
    this.retrofit = retrofit;
  }

  @SuppressWarnings("unchecked")
  public final <TService> TService create(Class<TService> svcClass, UserToken userToken) {
    return (TService) newProxyInstance(
        svcClass.getClassLoader(),
        new Class<?>[] { svcClass },
        new UserTokenCallProxy(retrofit.callFactory(), retrofit.create(svcClass), userToken)
    );
  }

}
