package io.getstream.chat.java.models.framework;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.java.Log;

@Log
public class RequestObjectBuilder {

  @SuppressWarnings("unchecked")
  /**
   * Builds a request object from a model object
   *
   * @param <T> the model object class
   * @param <U> the request object class
   * @param requestObjectClass the request object class
   * @param modelObject the model object
   * @return the request object
   * @throws Exception
   */
  public static <T, U> U build(Class<? extends U> requestObjectClass, T modelObject) {
    if (modelObject == null) {
      return null;
    }
    log.fine("Building instance of " + requestObjectClass.getName());
    Object resultBuilder;
    try {
      resultBuilder = requestObjectClass.getMethod("builder").invoke(requestObjectClass);
    } catch (IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException
        | NoSuchMethodException
        | SecurityException e) {
      throw new IllegalArgumentException(
          "Could not find builder method in the request object class "
              + requestObjectClass.getName(),
          e);
    }
    Class<?> resultBuilderClass = resultBuilder.getClass();
    for (Field field : getAllFields(modelObject.getClass())) {
      log.fine(field.getName() + " - Start treatment");
      field.setAccessible(true);
      Class<?> fieldClass = field.getType();
      // Find a method with fieldName as method name and single parameter
      Method resultBuilderMethod = findBuilderMethod(resultBuilderClass, field.getName());
      if (resultBuilderMethod != null) {
        Class<?> methodParameterClass = resultBuilderMethod.getParameters()[0].getType();
        log.fine(
            field.getName()
                + " - methodParameterClass="
                + methodParameterClass.getName()
                + " and fieldClass="
                + fieldClass.getName());
        if (Collection.class.isAssignableFrom(methodParameterClass)
            && List.class.isAssignableFrom(fieldClass)) {
          try {
            // If both List
            ParameterizedType methodParameterParameterizedType =
                (ParameterizedType) resultBuilderMethod.getGenericParameterTypes()[0];
            Class<?> methodParameterGenericClass;
            if (methodParameterParameterizedType.getActualTypeArguments()[0]
                instanceof WildcardType) {
              // Lombok builder creates methods like xxx(Collection<? extends YYY>) for singular
              WildcardType methodParameterWildcardType =
                  (WildcardType) methodParameterParameterizedType.getActualTypeArguments()[0];
              methodParameterGenericClass =
                  (Class<?>) methodParameterWildcardType.getUpperBounds()[0];
            } else {
              methodParameterGenericClass =
                  (Class<?>) methodParameterParameterizedType.getActualTypeArguments()[0];
            }
            ParameterizedType fieldParameterizedType = (ParameterizedType) field.getGenericType();
            Class<?> fieldGenericClass =
                (Class<?>) fieldParameterizedType.getActualTypeArguments()[0];
            log.fine(
                field.getName()
                    + " - Both lists. methodParameterGenericClass="
                    + methodParameterGenericClass
                    + " and fieldGenericClass="
                    + fieldGenericClass);
            if (field.get(modelObject) == null) {
              // Do nothing (cannot call builder method as does not accept null)
            } else if (methodParameterGenericClass.isAssignableFrom(fieldGenericClass)) {
              log.fine(field.getName() + " - Both lists and types match. Calling builder method");
              resultBuilderMethod.invoke(resultBuilder, field.get(modelObject));
            } else {
              log.fine(
                  field.getName() + " - Both lists and RequestObject. RequestObjectBuilder.build");
              List<?> requestObjectsList =
                  ((List<?>) field.get(modelObject))
                      .stream()
                          .map(
                              modelFieldValue -> {
                                return RequestObjectBuilder.build(
                                    methodParameterGenericClass, modelFieldValue);
                              })
                          .collect(Collectors.toList());
              resultBuilderMethod.invoke(resultBuilder, requestObjectsList);
            }
          } catch (IllegalArgumentException
              | IllegalAccessException
              | InvocationTargetException e) {
            throw new IllegalArgumentException(
                "This should not happen. Field is "
                    + field.getName()
                    + " in "
                    + modelObject.getClass().getName(),
                e);
          }
        } else if (methodParameterClass.isAssignableFrom(fieldClass)) {
          // If the types match between field and method
          try {
            log.fine(field.getName() + " - Types match. Calling builder method");
            resultBuilderMethod.invoke(resultBuilder, field.get(modelObject));
          } catch (IllegalAccessException
              | IllegalArgumentException
              | InvocationTargetException e) {
            throw new IllegalArgumentException(
                "This should not happen. Field is "
                    + field.getName()
                    + " in "
                    + modelObject.getClass().getName(),
                e);
          }
        } else {
          // Type is a RequestObject
          try {
            log.fine(field.getName() + " - Types do not match. Calling RequestObjectBuilder.build");
            resultBuilderMethod.invoke(
                resultBuilder,
                RequestObjectBuilder.build(methodParameterClass, field.get(modelObject)));
          } catch (IllegalAccessException
              | IllegalArgumentException
              | InvocationTargetException e) {
            throw new IllegalArgumentException(
                "This should not happen. Field is "
                    + field.getName()
                    + " in "
                    + modelObject.getClass().getName(),
                e);
          }
        }
      } else {
        log.fine(field.getName() + " - Could not find method in builder");
      }
    }
    try {
      U result = (U) resultBuilderClass.getMethod("build").invoke(resultBuilder);
      log.fine("Finished instance of " + requestObjectClass.getName());
      return result;
    } catch (IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException
        | NoSuchMethodException
        | SecurityException e) {
      throw new IllegalArgumentException(
          "Could not find build method in the request object builder class "
              + resultBuilderClass.getName(),
          e);
    }
  }

  /**
   * Find a method in builderClass with name methodName and single parameter
   *
   * @param builderClass the builder class
   * @param methodName the method name
   * @return the method
   */
  private static Method findBuilderMethod(Class<?> builderClass, String methodName) {
    List<Method> methods =
        Arrays.asList(builderClass.getDeclaredMethods()).stream()
            .filter(
                method -> method.getName().equals(methodName) && method.getParameterCount() == 1)
            .collect(Collectors.toList());
    if (methods.size() >= 2) {
      log.warning("Conflicting method name " + methodName + "in builder " + builderClass.getName());
      return null;
    }
    if (methods.isEmpty()) {
      return null;
    }
    return methods.get(0);
  }

  /**
   * Method to get all fields in class and superclass
   *
   * @param type the class
   * @return
   */
  private static List<Field> getAllFields(Class<?> type) {
    List<Field> fields = new ArrayList<>();
    fields.addAll(Arrays.asList(type.getDeclaredFields()));

    if (type.getSuperclass() != null) {
      fields.addAll(getAllFields(type.getSuperclass()));
    }

    return fields;
  }
}
