package io.getstream.chat.java.services.framework;

public interface UserServiceFactory {

  <TService> TService create(Class<TService> svcClass, UserToken userToken);

}
