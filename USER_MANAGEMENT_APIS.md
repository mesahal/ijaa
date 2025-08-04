# 👥 User Management APIs - Admin Implementation

## 🎯 **Overview**

The admin user management APIs have been implemented to provide comprehensive user administration capabilities. These APIs allow admins to view, block, unblock, and delete user accounts.

---

## 🔧 **Implemented APIs**

### **1. Get All Users**
```http
GET /api/v1/admin/users
Authorization: Bearer ADMIN_TOKEN
```

**Description:** Retrieve all users in the system (ADMIN only)

**Response:**
```json
{
  "message": "Users retrieved successfully",
  "code": "200",
  "data": [
    {
      "userId": "USER_ABC123XYZ",
      "username": "john.doe",
      "name": "john.doe",
      "email": null,
      "active": true,
      "createdAt": null,
      "updatedAt": null
    }
  ]
}
```

### **2. Block User**
```http
POST /api/v1/admin/users/{userId}/block
Authorization: Bearer ADMIN_TOKEN
```

**Description:** Block a user account (ADMIN only)

**Parameters:**
- `userId` (Long): The ID of the user to block

**Response:**
```json
{
  "message": "User blocked successfully",
  "code": "200",
  "data": {
    "userId": "USER_ABC123XYZ",
    "username": "john.doe",
    "name": "john.doe",
    "email": null,
    "active": false,
    "createdAt": null,
    "updatedAt": null
  }
}
```

### **3. Unblock User**
```http
POST /api/v1/admin/users/{userId}/unblock
Authorization: Bearer ADMIN_TOKEN
```

**Description:** Unblock a user account (ADMIN only)

**Parameters:**
- `userId` (Long): The ID of the user to unblock

**Response:**
```json
{
  "message": "User unblocked successfully",
  "code": "200",
  "data": {
    "userId": "USER_ABC123XYZ",
    "username": "john.doe",
    "name": "john.doe",
    "email": null,
    "active": true,
    "createdAt": null,
    "updatedAt": null
  }
}
```

### **4. Delete User**
```http
DELETE /api/v1/admin/users/{userId}
Authorization: Bearer ADMIN_TOKEN
```

**Description:** Delete a user account (ADMIN only)

**Parameters:**
- `userId` (Long): The ID of the user to delete

**Response:**
```json
{
  "message": "User deleted successfully",
  "code": "200",
  "data": null
}
```

---

## 🗄️ **Database Changes**

### **User Entity Enhancement:**
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String userId;

    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;
}
```

**New Field:**
- `active` (Boolean): Controls whether the user account is active/blocked

---

## 📋 **Service Layer Implementation**

### **AdminService Interface:**
```java
public interface AdminService {
    // User Management Methods
    List<UserResponse> getAllUsers();
    UserResponse blockUser(Long userId);
    UserResponse unblockUser(Long userId);
    void deleteUser(Long userId);
}
```

### **AdminServiceImpl Implementation:**
```java
@Override
public List<UserResponse> getAllUsers() {
    return userRepository.findAll().stream()
            .map(this::createUserResponse)
            .collect(Collectors.toList());
}

@Override
public UserResponse blockUser(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    
    user.setActive(false);
    User updatedUser = userRepository.save(user);
    
    return createUserResponse(updatedUser);
}

@Override
public UserResponse unblockUser(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    
    user.setActive(true);
    User updatedUser = userRepository.save(user);
    
    return createUserResponse(updatedUser);
}

@Override
public void deleteUser(Long userId) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    
    userRepository.delete(user);
}
```

---

## 📊 **Response DTOs**

### **UserResponse:**
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String userId;
    private String username;
    private String name;
    private String email;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

---

## 🔐 **Security Features**

### **Authorization:**
- All APIs require `ADMIN` role
- Protected with `@PreAuthorize("hasRole('ADMIN')")`
- JWT token validation required

### **Error Handling:**
- **404 Not Found:** User not found
- **403 Forbidden:** Insufficient privileges
- **401 Unauthorized:** Missing or invalid token

---

## 🧪 **Testing Examples**

### **1. Get All Users:**
```bash
curl -X GET "http://localhost:8000/api/v1/admin/users" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### **2. Block User:**
```bash
curl -X POST "http://localhost:8000/api/v1/admin/users/1/block" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### **3. Unblock User:**
```bash
curl -X POST "http://localhost:8000/api/v1/admin/users/1/unblock" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### **4. Delete User:**
```bash
curl -X DELETE "http://localhost:8000/api/v1/admin/users/1" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

---

## 🔄 **Integration with Existing Features**

### **1. Authentication Integration:**
- Uses existing JWT authentication system
- Integrates with Spring Security
- Follows existing authorization patterns

### **2. Database Integration:**
- Uses existing User entity
- Extends with `active` field
- Maintains data consistency

### **3. API Gateway Integration:**
- Routes through existing gateway
- Uses existing authentication filter
- Follows existing response patterns

### **4. Error Handling:**
- Uses existing exception handling
- Follows existing response format
- Maintains consistency with other APIs

---

## 📈 **Future Enhancements**

### **1. User Statistics:**
- Add user blocking statistics to dashboard
- Track blocked user count
- Monitor user activity

### **2. Bulk Operations:**
- Bulk block/unblock users
- Bulk delete users
- Batch operations for efficiency

### **3. User Activity Tracking:**
- Track user login attempts
- Monitor suspicious activity
- Implement automatic blocking

### **4. Email Notifications:**
- Notify users when blocked/unblocked
- Send deletion confirmation
- Email-based user management

---

## ✅ **Benefits**

### **1. Complete User Management:**
- View all users in the system
- Block/unblock user accounts
- Delete user accounts permanently

### **2. Security Enhancement:**
- Admin-only access to user management
- Proper authorization checks
- Secure user operations

### **3. System Administration:**
- Full control over user accounts
- Ability to manage problematic users
- System maintenance capabilities

### **4. API Consistency:**
- Follows existing API patterns
- Consistent response format
- Proper error handling

---

## 🎯 **Summary**

The user management APIs provide comprehensive admin capabilities:

1. **✅ Get All Users** - View all users in the system
2. **✅ Block User** - Disable user accounts
3. **✅ Unblock User** - Re-enable user accounts  
4. **✅ Delete User** - Permanently remove user accounts

**All APIs are fully functional and integrated with the existing system architecture!** 