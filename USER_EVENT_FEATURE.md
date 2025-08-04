# 🎉 User Event Management Feature

## 📋 **Overview**

The User Event Management feature allows regular users (alumni) to create, manage, and view events within the IJAA platform. Users can create their own events, view events created by other users, and manage their own event portfolio.

---

## 🎯 **Key Features**

### **User Event Management:**
- ✅ **Create Events** - Users can create new events with full details
- ✅ **View Own Events** - Users can view all events they have created
- ✅ **Update Own Events** - Users can modify events they created
- ✅ **Delete Own Events** - Users can delete events they created
- ✅ **View All Events** - Users can browse all active events created by other users
- ✅ **Event Ownership** - Users can only modify/delete their own events
- ✅ **Event Discovery** - Users can discover events created by other alumni

---

## 🗄️ **Database Changes**

### **Event Entity Updates:**
```java
@Entity
@Table(name = "events")
public class Event {
    // ... existing fields ...
    
    // New field to track event creator
    @Column(length = 50)
    private String createdByUsername; // Username of the user who created the event
    
    // ... existing fields ...
}
```

### **EventRepository New Methods:**
```java
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // User-specific queries
    List<Event> findByCreatedByUsername(String username);
    List<Event> findByCreatedByUsernameAndActiveTrue(String username);
    List<Event> findByCreatedByUsernameOrderByCreatedAtDesc(String username);
    List<Event> findByCreatedByUsernameAndActiveTrueOrderByStartDateAsc(String username);
    
    // ... existing methods ...
}
```

---

## 🔧 **Service Layer Updates**

### **EventService Interface:**
```java
public interface EventService {
    // ... existing admin methods ...
    
    // User-specific methods
    List<EventResponse> getEventsByUser(String username);
    List<EventResponse> getActiveEventsByUser(String username);
    EventResponse createEventForUser(EventRequest eventRequest, String username);
    EventResponse updateEventForUser(Long eventId, EventRequest eventRequest, String username);
    void deleteEventForUser(Long eventId, String username);
    EventResponse getEventByIdForUser(Long eventId, String username);
}
```

### **EventServiceImpl Key Features:**
- **User Ownership Validation** - Users can only modify/delete their own events
- **Automatic User Tracking** - Events are automatically tagged with creator username
- **Security Checks** - Proper validation to prevent unauthorized access
- **Date Validation** - Ensures event dates are valid and not in the past

---

## 🌐 **API Endpoints**

### **User Event Management APIs:**

#### **1. Get User's Events**
```http
GET /api/v1/user/events/my-events
Authorization: Bearer <jwt_token>
```
**Response:**
```json
{
  "message": "User events retrieved successfully",
  "code": "200",
  "data": [
    {
      "id": 1,
      "title": "Alumni Meet 2024",
      "description": "Annual alumni gathering",
      "startDate": "2024-12-25T18:00:00",
      "endDate": "2024-12-25T22:00:00",
      "location": "IIT Campus",
      "eventType": "MEETING",
      "active": true,
      "isOnline": false,
      "maxParticipants": 100,
      "currentParticipants": 0,
      "organizerName": "John Doe",
      "organizerEmail": "john@example.com",
      "createdByUsername": "john.doe",
      "createdAt": "2024-12-01T10:00:00",
      "updatedAt": "2024-12-01T10:00:00"
    }
  ]
}
```

#### **2. Get User's Active Events**
```http
GET /api/v1/user/events/my-events/active
Authorization: Bearer <jwt_token>
```

#### **3. Get User's Event by ID**
```http
GET /api/v1/user/events/my-events/{eventId}
Authorization: Bearer <jwt_token>
```

#### **4. Create Event**
```http
POST /api/v1/user/events/create
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "title": "Alumni Meet 2024",
  "description": "Annual alumni gathering",
  "startDate": "2024-12-25T18:00:00",
  "endDate": "2024-12-25T22:00:00",
  "location": "IIT Campus",
  "eventType": "MEETING",
  "isOnline": false,
  "meetingLink": null,
  "maxParticipants": 100,
  "organizerName": "John Doe",
  "organizerEmail": "john@example.com"
}
```

#### **5. Update User's Event**
```http
PUT /api/v1/user/events/my-events/{eventId}
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "title": "Updated Alumni Meet 2024",
  "description": "Updated annual alumni gathering",
  "startDate": "2024-12-25T18:00:00",
  "endDate": "2024-12-25T22:00:00",
  "location": "IIT Campus",
  "eventType": "MEETING",
  "isOnline": false,
  "meetingLink": null,
  "maxParticipants": 100,
  "organizerName": "John Doe",
  "organizerEmail": "john@example.com"
}
```

#### **6. Delete User's Event**
```http
DELETE /api/v1/user/events/my-events/{eventId}
Authorization: Bearer <jwt_token>
```

### **Public Event Discovery APIs:**

#### **7. Get All Active Events**
```http
GET /api/v1/user/events/all-events
Authorization: Bearer <jwt_token>
```

#### **8. Get Event by ID (Public)**
```http
GET /api/v1/user/events/all-events/{eventId}
Authorization: Bearer <jwt_token>
```

---

## 🔐 **Security & Authorization**

### **Access Control:**
- **User Role Required** - All endpoints require `USER` role
- **JWT Authentication** - All requests must include valid JWT token
- **Ownership Validation** - Users can only modify/delete their own events
- **Public Read Access** - Users can view all active events

### **Security Features:**
- **Automatic User Tracking** - Events are automatically tagged with creator
- **Ownership Checks** - Service layer validates event ownership
- **Input Validation** - Comprehensive request validation
- **Date Validation** - Prevents creation of past events

### **Error Handling:**
```json
{
  "message": "You can only update events that you created",
  "code": "403",
  "data": null
}
```

---

## 📊 **Event Response Structure**

### **EventResponse DTO:**
```java
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private String eventType;
    private Boolean active;
    private Boolean isOnline;
    private String meetingLink;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private String organizerName;
    private String organizerEmail;
    private String createdByUsername; // NEW: Shows who created the event
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

---

## 🧪 **Testing**

### **Test Coverage:**
- ✅ **UserEventResourceTest** - Controller layer testing
- ✅ **EventService Tests** - Service layer validation
- ✅ **Repository Tests** - Data access layer testing
- ✅ **Integration Tests** - End-to-end functionality

### **Test Scenarios:**
1. **Create Event** - User creates new event
2. **View Own Events** - User views their events
3. **Update Own Event** - User updates their event
4. **Delete Own Event** - User deletes their event
5. **View All Events** - User browses all events
6. **Unauthorized Access** - User tries to modify others' events
7. **Invalid Data** - Validation error handling

---

## 🚀 **Usage Examples**

### **1. User Creates an Event:**
```bash
# Login and get JWT token
curl -X POST http://localhost:8000/api/v1/user/signin \
  -H "Content-Type: application/json" \
  -d '{"username": "john.doe", "password": "password123"}'

# Create event
curl -X POST http://localhost:8000/api/v1/user/events/create \
  -H "Authorization: Bearer <jwt_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Alumni Meet 2024",
    "description": "Annual alumni gathering",
    "startDate": "2024-12-25T18:00:00",
    "endDate": "2024-12-25T22:00:00",
    "location": "IIT Campus",
    "eventType": "MEETING",
    "maxParticipants": 100,
    "organizerName": "John Doe",
    "organizerEmail": "john@example.com"
  }'
```

### **2. User Views Their Events:**
```bash
curl -X GET http://localhost:8000/api/v1/user/events/my-events \
  -H "Authorization: Bearer <jwt_token>"
```

### **3. User Views All Events:**
```bash
curl -X GET http://localhost:8000/api/v1/user/events/all-events \
  -H "Authorization: Bearer <jwt_token>"
```

---

## 🔄 **Migration & Deployment**

### **Database Migration:**
The new `createdByUsername` field will be automatically added to existing events table. For existing events, this field will be `null` until updated.

### **Backward Compatibility:**
- Existing admin event management remains unchanged
- New user event endpoints are additive
- No breaking changes to existing APIs

### **Deployment Steps:**
1. **Database Update** - New column will be added automatically
2. **Service Deployment** - Deploy updated user service
3. **Gateway Configuration** - No changes needed
4. **Testing** - Verify all endpoints work correctly

---

## 📈 **Future Enhancements**

### **Planned Features:**
1. **Event Registration** - Users can register for events
2. **Event Categories** - Categorize events by type
3. **Event Search** - Advanced search and filtering
4. **Event Notifications** - Notify users about new events
5. **Event Comments** - Allow users to comment on events
6. **Event Sharing** - Share events on social media
7. **Event Analytics** - Track event engagement metrics

### **Technical Improvements:**
1. **Caching** - Redis caching for event listings
2. **Pagination** - Handle large event lists
3. **Real-time Updates** - WebSocket notifications
4. **Media Upload** - Event images and attachments
5. **Location Services** - Map integration for events

---

## ✅ **Implementation Status**

### **Completed:**
- ✅ Event entity with user association
- ✅ User-specific repository methods
- ✅ Service layer with ownership validation
- ✅ User event controller with full CRUD
- ✅ Public event discovery endpoints
- ✅ Comprehensive API documentation
- ✅ Security and authorization
- ✅ Error handling and validation
- ✅ Unit and integration tests

### **Ready for Production:**
- ✅ All endpoints tested and working
- ✅ Security measures implemented
- ✅ Documentation complete
- ✅ Backward compatibility maintained
- ✅ Database migration ready

---

## 🎯 **Summary**

The User Event Management feature successfully enables alumni to create and manage their own events while maintaining proper security and access controls. Users can:

1. **Create Events** - Full event creation with all details
2. **Manage Own Events** - Update and delete their events
3. **Discover Events** - Browse events created by other users
4. **Secure Access** - Only modify their own events
5. **Rich Data** - Complete event information with creator tracking

This implementation follows the existing project architecture and maintains consistency with the current codebase while adding powerful new functionality for user engagement. 