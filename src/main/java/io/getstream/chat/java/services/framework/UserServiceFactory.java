package io.getstream.chat.java.services.framework;

interface UserServiceFactory {

  <TService> TService create(Class<TService> svcClass, UserToken userToken);

}
