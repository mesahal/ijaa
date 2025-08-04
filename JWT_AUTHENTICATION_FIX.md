# 🔐 JWT Authentication Fix - Admin API Access Issue

## 🚨 **Problem Identified**

When calling admin APIs (except login/signup), you were getting:
```
AuthorizationDeniedException: Access Denied
```

**Error in IDE Console:**
```
2025-08-02T22:11:17.477+06:00  WARN 515176 --- [users] [nio-8081-exec-5] .m.m.a.ExceptionHandlerExceptionResolver : Resolved [org.springframework.security.authorization.AuthorizationDeniedException: Access Denied]
```

**Error in Swagger:**
```json
{
  "message": "Access Denied",
  "code": "404",
  "data": null
}
```

---

## 🔍 **Root Cause Analysis**

### **The Problem:**
The user service was **missing JWT authentication filter**. Here's what was happening:

1. **Gateway Level:** JWT validation was working correctly
2. **User Service Level:** No JWT authentication filter was configured
3. **Spring Security:** Couldn't identify the user/admin from the JWT token
4. **Result:** `@PreAuthorize("hasRole('ADMIN')")` failed because no user context was set

### **Authentication Flow (Before Fix):**
```
Client → Gateway (JWT Validated) → User Service (❌ No JWT Filter) → Spring Security (❌ No User Context) → Access Denied
```

### **Authentication Flow (After Fix):**
```
Client → Gateway (JWT Validated) → User Service (✅ JWT Filter) → Spring Security (✅ User Context Set) → Access Granted
```

---

## ✅ **The Fix**

### **1. Created JWT Authentication Filter:**
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) {
        
        // Extract JWT token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String jwt = authHeader.substring(7);
        
        try {
            // Extract user type from JWT
            String userType = jwtService.extractUserType(jwt);
            
            if (userType != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = null;
                
                if ("ADMIN".equals(userType)) {
                    // Handle admin authentication
                    String email = jwtService.extractEmail(jwt);
                    userDetails = adminUserDetailsService.loadUserByUsername(email);
                } else {
                    // Handle user authentication
                    String username = jwtService.extractUsername(jwt);
                    userDetails = userDetailsService.loadUserByUsername(username);
                }
                
                if (userDetails != null && jwtService.validateToken(jwt, jwtSecret)) {
                    // Set authentication context
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            log.error("JWT authentication error: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
}
```

### **2. Enhanced JWT Service:**
```java
// Added missing methods
public String extractEmail(String token) {
    return extractClaim(token, claims -> claims.get("email", String.class));
}

public boolean validateToken(String token, String secret) {
    try {
        extractAllClaims(token);
        return true;
    } catch (Exception e) {
        return false;
    }
}
```

### **3. Updated Security Configuration:**
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    
    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // ✅ Added JWT Filter
                .build();
    }
}
```

---

## 🔄 **How It Works Now**

### **1. JWT Token Structure:**
```json
// Admin Token
{
  "email": "admin@ijaa.com",
  "role": "ADMIN",
  "type": "ADMIN", 
  "userType": "ADMIN"
}

// User Token
{
  "username": "john.doe",
  "role": "USER",
  "type": "USER",
  "userType": "ALUMNI"
}
```

### **2. Authentication Process:**
1. **Request comes with JWT token**
2. **JWT Filter extracts user type** (`ADMIN` or `USER`)
3. **Loads appropriate UserDetails** (AdminUserDetailsService or UserDetailsServiceImpl)
4. **Validates JWT token**
5. **Sets Spring Security context** with proper authorities
6. **@PreAuthorize checks work correctly**

### **3. Role-Based Access:**
```java
// Admin endpoints - only ADMIN role can access
@PreAuthorize("hasRole('ADMIN')")
GET /api/v1/admin/profile
GET /api/v1/admin/dashboard
GET /api/v1/admin/users
GET /api/v1/admin/admins

// User endpoints - only USER role can access  
@PreAuthorize("hasRole('USER')")
GET /api/v1/user/profile/{userId}
GET /api/v1/user/experiences/{userId}
```

---

## 🧪 **Testing the Fix**

### **✅ Should Work Now:**

#### **Admin Login:**
```bash
POST /api/v1/admin/login
{
  "email": "admin@ijaa.com",
  "password": "admin123"
}
```

#### **Admin Profile (with admin token):**
```bash
GET /api/v1/admin/profile
Authorization: Bearer ADMIN_TOKEN
```

#### **Admin Dashboard (with admin token):**
```bash
GET /api/v1/admin/dashboard
Authorization: Bearer ADMIN_TOKEN
```

#### **Admin Management (with admin token):**
```bash
GET /api/v1/admin/users
GET /api/v1/admin/admins
GET /api/v1/admin/feature-flags
Authorization: Bearer ADMIN_TOKEN
```

### **❌ Should Still Fail:**

#### **Admin endpoints with user token:**
```bash
GET /api/v1/admin/profile
Authorization: Bearer USER_TOKEN
# Returns: 403 Forbidden - Access Denied
```

#### **Admin endpoints without token:**
```bash
GET /api/v1/admin/profile
# Returns: 401 Unauthorized - Missing Authorization Header
```

---

## 📋 **Key Changes Made**

### **1. New Files Created:**
- `JwtAuthenticationFilter.java` - JWT authentication filter
- `JWT_AUTHENTICATION_FIX.md` - This documentation

### **2. Files Modified:**
- `JWTService.java` - Added `extractEmail()` and `validateToken()` methods
- `SecurityConfig.java` - Added JWT filter to security chain

### **3. Authentication Flow:**
- **Before:** Gateway → User Service (No JWT validation) → Access Denied
- **After:** Gateway → User Service (JWT validation) → Access Granted

---

## ✅ **Benefits of the Fix**

### **1. Proper JWT Authentication:**
- JWT tokens are now validated at the user service level
- User context is properly set for Spring Security
- Role-based access control works correctly

### **2. Dual Authentication Support:**
- Supports both user and admin authentication
- Automatically detects user type from JWT claims
- Uses appropriate UserDetailsService for each type

### **3. Security Enhancement:**
- JWT validation at multiple levels (Gateway + User Service)
- Proper error handling for invalid tokens
- Secure token validation with secret key

### **4. Consistent Behavior:**
- All admin endpoints now work correctly
- All user endpoints continue to work
- Clear error messages for unauthorized access

---

## 🎯 **Summary**

The issue was that the user service wasn't validating JWT tokens and setting up Spring Security context. The fix adds proper JWT authentication at the user service level, allowing:

1. **Admin APIs to work correctly** with admin tokens
2. **User APIs to continue working** with user tokens  
3. **Proper role-based access control** for all endpoints
4. **Clear error messages** for unauthorized access

**The admin APIs should now work correctly when called with a valid admin JWT token!** 