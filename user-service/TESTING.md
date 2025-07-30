# 🧪 IJAA User Service - Testing Guide

This document provides a comprehensive guide to the automated testing implementation for the IJAA User Service.

## 📁 Test Structure

```
src/test/java/com/ijaa/user/
├── config/
│   └── TestConfig.java                    # Test configuration beans
├── util/
│   ├── TestDataBuilder.java               # Test data factory
│   └── TestSecurityUtils.java             # Security test utilities
├── service/
│   ├── AuthServiceTest.java               # AuthService unit tests
│   └── ProfileServiceTest.java            # ProfileService unit tests
├── presenter/rest/api/
│   ├── AuthResourceIntegrationTest.java   # Auth controller integration tests
│   └── ProfileResourceIntegrationTest.java # Profile controller integration tests
├── repository/
│   └── UserRepositoryTest.java            # UserRepository database tests
└── CompleteTestSuite.java                 # Test suite runner
```

## 🧪 Test Types

### 1. Unit Tests (`service/`)
- **Purpose**: Test individual service methods in isolation
- **Framework**: JUnit 5 + Mockito
- **Scope**: Business logic, validation, error handling
- **Dependencies**: Mocked repositories and external services

**Example Test Classes:**
- `AuthServiceTest.java` - Tests user registration, login, JWT generation
- `ProfileServiceTest.java` - Tests profile management, experience, interests

### 2. Integration Tests (`presenter/rest/api/`)
- **Purpose**: Test REST API endpoints end-to-end
- **Framework**: Spring Boot Test + MockMvc
- **Scope**: HTTP requests/responses, validation, authentication
- **Dependencies**: Mocked services, real controllers

**Example Test Classes:**
- `AuthResourceIntegrationTest.java` - Tests `/api/auth/*` endpoints
- `ProfileResourceIntegrationTest.java` - Tests `/api/profile/*` endpoints

### 3. Repository Tests (`repository/`)
- **Purpose**: Test database operations and data persistence
- **Framework**: Spring Boot Test + DataJpaTest
- **Scope**: CRUD operations, database constraints, queries
- **Database**: H2 in-memory database

**Example Test Classes:**
- `UserRepositoryTest.java` - Tests user data persistence

## 🚀 Running Tests

### Prerequisites
```bash
# Ensure you have Java 17 and Maven installed
java -version
mvn -version
```

### Run All Tests
```bash
cd user-service
mvn test
```

### Run Specific Test Types
```bash
# Run only unit tests (service layer)
mvn test -Dtest="*ServiceTest"

# Run only integration tests (controllers)
mvn test -Dtest="*IntegrationTest"

# Run only repository tests
mvn test -Dtest="*RepositoryTest"

# Run specific test class
mvn test -Dtest="AuthServiceTest"

# Run specific test method
mvn test -Dtest="AuthServiceTest#registerUser_Success"
```

### Run Tests with Coverage
```bash
# Generate test coverage report
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

## 📊 Test Coverage

### AuthService Tests
- ✅ User registration with valid data
- ✅ User registration with duplicate username
- ✅ User ID generation retry logic
- ✅ User login with valid credentials
- ✅ User login with invalid credentials
- ✅ User not found scenarios
- ✅ Null/empty input handling

### ProfileService Tests
- ✅ Get profile by user ID
- ✅ Update basic profile information
- ✅ Update visibility settings
- ✅ Create new profile when doesn't exist
- ✅ Experience management (add, get, delete)
- ✅ Interest management (add, get, delete)
- ✅ Validation and error handling

### Controller Integration Tests
- ✅ Valid request scenarios
- ✅ Invalid request validation
- ✅ Authentication and authorization
- ✅ Error response handling
- ✅ Content type validation
- ✅ Malformed JSON handling

### Repository Tests
- ✅ CRUD operations
- ✅ Unique constraint validation
- ✅ Null value handling
- ✅ Database constraint violations

## 🛠️ Test Utilities

### TestDataBuilder
Provides factory methods for creating test data objects:
```java
// Create test users
User user = TestDataBuilder.createTestUser();
User user2 = TestDataBuilder.createTestUser("USER_123", "username");

// Create test requests
SignUpRequest signUpRequest = TestDataBuilder.createSignUpRequest();
SignInRequest signInRequest = TestDataBuilder.createSignInRequest();

// Create test profiles
Profile profile = TestDataBuilder.createTestProfile();
ProfileDto profileDto = TestDataBuilder.createTestProfileDto();
```

### TestSecurityUtils
Provides security-related test utilities:
```java
// Setup security context
TestSecurityUtils.setupSecurityContext("testuser");

// Create mock JWT tokens
String token = TestSecurityUtils.createMockJwtToken("testuser");

// Clear security context
TestSecurityUtils.clearSecurityContext();
```

### TestConfig
Provides test-specific configuration beans:
- ObjectMapper for JSON serialization
- PasswordEncoder for password hashing
- UniqueIdGenerator for ID generation

## 🔧 Test Configuration

### Test Environment
- **Database**: H2 in-memory database
- **Profile**: `test` profile
- **Configuration**: `application-test.yml`

### Test Properties
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
  security:
    user:
      name: testuser
      password: testpass

jwt:
  secret: testSecretKeyForTestingPurposesOnly
  expiration: 3600
```

## 📝 Test Naming Conventions

### Test Method Names
Follow the pattern: `methodName_scenario_expectedResult`

**Examples:**
- `registerUser_Success` - Successful user registration
- `registerUser_UsernameAlreadyExists` - Registration with duplicate username
- `verify_AuthenticationFailed` - Login with invalid credentials
- `getProfileByUserId_ProfileNotFound` - Profile not found scenario

### Test Class Names
- Unit tests: `ClassNameTest.java`
- Integration tests: `ClassNameIntegrationTest.java`
- Repository tests: `RepositoryNameTest.java`

## 🎯 Testing Best Practices

### 1. Arrange-Act-Assert Pattern
```java
@Test
void testMethod_Success() {
    // Arrange - Setup test data and mocks
    SignUpRequest request = TestDataBuilder.createSignUpRequest();
    when(userRepository.existsByUsername("testuser")).thenReturn(false);
    
    // Act - Execute the method under test
    AuthResponse response = authService.registerUser(request);
    
    // Assert - Verify the results
    assertNotNull(response);
    assertEquals("test.jwt.token", response.getToken());
}
```

### 2. Mock Verification
```java
// Verify service interactions
verify(userRepository).existsByUsername("testuser");
verify(userRepository).save(any(User.class));
verify(jwtService).generateToken("testuser");

// Verify no unwanted interactions
verify(userRepository, never()).delete(any(User.class));
```

### 3. Exception Testing
```java
@Test
void testMethod_ThrowsException() {
    // Arrange
    when(repository.method()).thenThrow(new RuntimeException("Error"));
    
    // Act & Assert
    assertThrows(RuntimeException.class, () -> {
        service.method();
    });
}
```

### 4. Integration Test Authentication
```java
@Test
@WithMockUser(username = "testuser")
void authenticatedEndpoint_Success() throws Exception {
    mockMvc.perform(get("/api/profile/123"))
           .andExpect(status().isOk());
}
```

## 🔍 Debugging Tests

### Enable Debug Logging
```yaml
logging:
  level:
    com.ijaa.user: DEBUG
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG
```

### Run Single Test in IDE
1. Right-click on test method
2. Select "Run Test"
3. View console output and debug information

### Database Inspection
For repository tests, you can inspect the H2 database:
```java
@Autowired
private TestEntityManager entityManager;

// Find entity in database
User user = entityManager.find(User.class, userId);
```

## 📈 Continuous Integration

### GitHub Actions Example
```yaml
name: Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '17'
      - run: mvn test
```

## 🚨 Common Issues

### 1. Test Database Issues
- Ensure H2 dependency is included
- Check `application-test.yml` configuration
- Verify `@DataJpaTest` annotation is used

### 2. Security Test Issues
- Add `spring-security-test` dependency
- Use `@WithMockUser` for authentication
- Clear security context between tests

### 3. Mock Issues
- Use `@MockBean` for Spring Boot tests
- Use `@Mock` + `@InjectMocks` for unit tests
- Reset mocks in `@BeforeEach`

### 4. JSON Serialization Issues
- Use `ObjectMapper` for JSON conversion
- Check field names match DTOs
- Verify content type headers

## 📚 Additional Resources

- [Spring Boot Testing Guide](https://spring.io/guides/gs/testing-web/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Security Test](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#test)

---

**Note**: This testing implementation provides comprehensive coverage for the IJAA User Service. All tests follow best practices and can be easily extended for additional functionality. 