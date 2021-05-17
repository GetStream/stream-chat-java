# Guide to contribute to the Stream Chat Java SDK
## Requirements
To contribute to this project, you need to have [Lombok](https://projectlombok.org/) installed in your IDE.
You also need to use javac >= 11 to compile (ensure that your JAVA_HOME points to such a location), even if the produced library is compatible with Java 8 (for some reasons, jdk 8 compiler fails on some Lombok annotations).

## Setup the project
The project is a Maven project. For convenience, Eclipse files are provided in the repository, but it should be straightfoward to install in any other Java IDE.

## Architecture explanation
The code is composed of:
### Model classes
They are the data objects. They correspond to the formats of the objects contained in the API responses.
### Request classes
They are builders for API requests, and also contain methods to perform the calls.
### RequestData and RequestObject classes
They are data objects. They correspond to the formats of the API requests.
### Response classes
They are data objects. They correspond to the formats of the API responses.
### Service interfaces
They are Retrofit service interfaces, that describe the API endpoints.
### The StreamServiceGenerator class
This class is responsible to make the Service interfaces usable to call the API. It is used by all Request classes to define the `generateCall` method.  
It also handles authentication and logging.
### The StreamResponse interface and related classes
StreamResponse is the interface that correspond to an API response. Most APIs responses are StreamResponseObject (mean they contain duration and rate limit data).
### The StreamRequest and StreamServiceHandler classes
StreamRequest is the generic Request class, that defines the `request` and `requestAsync` methods, which call the `StreamServiceHandler`.  
The `StreamServiceHandler` class defines the synchronous and asynchronous processing. It also enriched the Response object with rate limit data when available.
### Test classes
They are organized by model. Each endpoint has at least one test related.

## Code rules
- The code should be formatted using Google formatter.
- All attributes, parameters and return values should be annotated with either `@Nullable` or `@NotNull`
- New implementations should follow the same principles as the existing ones (see how to section below)
- In Models, collections of submodel should be List (other collections and arrays are not supported by RequestObjectBuilder)

## How to
### Enable logging
Logging is enabled by default in tests. If you want to create a main class and activate logging, you should do the following:
```java
StreamServiceGenerator.logLevel = HttpLoggingInterceptor.Level.BODY;
```

### Add a new endpoint with request body schema or json query parameter
#### Create the Model and Service
- If they already exist, skip this section
- Model class should have `@Data @NoArgsConstructor`

#### Create the RequestObject classes
- They should be created in the model, after other RequestObject classes
- All fields should be `@Nullable`
- Collection fields should be `@Singular`
- These classes should have a `@Builder`
- If there is a corresponding Model object, they should have a `buildFrom` method and `@Setter`

#### Create the xxxResponse class
- It should be created in the model, after other Response classes
- All fields should have `@JsonProperty("xxx")`
- The xxxResponse class should extend `StreamResponseObject` and have `@Data @NoArgsConstructor @EqualsAndHashCode(callSuper = true)` (or implement `StreamResponseWithRateLimit`and have `@Data @NoArgsConstructor` if the response is directly a model object)

#### Create the xxxRequestData class
- It should be created in the model, after other RequestData/Request classes
- All fields should be `@Nullable` except collections that should be never be null which should have @Singular (which will make builder initialize them)

#### Create the xxxRequest
- It should be created inside the xxxRequestData class
- The xxxRequestData class should define this class as a Builder `@Builder(builderClassName = "xxxRequest", builderMethodName = "", buildMethodName = "internalBuild")`.

#### Add the endpoint path variables to the xxxRequest class
- They should be `@NotNull` attributes
- The xxxRequest class should have a private constructor with all of them

#### Add the request creation method
- It should be added at the end of the model
- It should be static
- It should have the same parameters as the xxxRequest constructor
- It should be documented with Javadoc

#### Add the service method
- It should be added at the end of the service class
- It should have the same name as the method in the model to build the xxxRequest
- All parameters should be marked with `@Body` or `@Query` or `@Path`. You can use the `@ToJson` annotation if you need the object to be transformed into a json String

#### Make the xxxRequest extend StreamRequest<xxxResponse>
You need to implement the `generateCall` method, calling the service using the `StreamServiceGenerator`

#### Update README.md (features and examples)

### Add a new endpoint without request body schema or json query parameter
#### Create the Model and Service
- If they already exist, skip this section
- Model class should have `@Data @NoArgsConstructor`

#### Create the xxxResponse class
- It should be created in the model, after other Response classes
- All fields should have `@JsonProperty("xxx")`
- The xxxResponse class should extend `StreamResponseObject` and have `@Data @NoArgsConstructor @EqualsAndHashCode(callSuper = true)` (or implement `StreamResponseWithRateLimit`and have `@Data @NoArgsConstructor` if the response is directly a model object)

#### Create the xxxRequest
- It should be created in the model, after other RequestData/Request classes
- Define the parameters as private attributes
- The class should be annotated with `@RequiredArgsConstructor`
- The class should have helper style methods for non required args

#### Add the request creation method
- It should be added at the end of the model
- It should be static
- It should have the same parameters as the xxxRequest constructor
- It should be documented with Javadoc

#### Add the service method
- It should be added at the end of the service class
- It should have the same name as the method in the model to build the xxxRequest
- All parameters should be marked with `@Query` or `@Path`

#### Make the xxxRequest extend StreamRequest<xxxResponse>
You need to implement the `generateCall` method, calling the service using the `StreamServiceGenerator`

#### Update README.md (features and examples)

### Add new fields to requests or responses
If you want to add new fields, just add the corresponding attributes in the Request, RequestData or Response class. That's all :)
