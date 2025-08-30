# Google Sign-In Implementation Guide

## Overview

This document describes the implementation of Google OAuth2 Sign-In functionality for the IJAA (International Jute Alumni Association) project. The implementation allows users to authenticate using their Google accounts, providing a seamless login experience.

## 🚀 **Features Implemented**

### **Core Functionality**
- ✅ **Google OAuth2 Integration**: Secure authentication using Google's OAuth2 protocol
- ✅ **Automatic User Creation**: New users are automatically created when they first sign in with Google
- ✅ **Existing User Login**: Users who have previously signed in with Google can log in seamlessly
- ✅ **Profile Information Sync**: Automatically syncs user profile information from Google
- ✅ **Feature Flag Protection**: Google Sign-In is protected by feature flags for easy toggling
- ✅ **JWT Token Generation**: Generates JWT tokens for authenticated Google users
- ✅ **Error Handling**: Comprehensive error handling for various OAuth scenarios

### **Security Features**
- ✅ **Token Verification**: Verifies Google ID tokens and access tokens
- ✅ **Audience Validation**: Ensures tokens are intended for our application
- ✅ **Email Verification**: Checks if email is verified by Google
- ✅ **Conflict Resolution**: Handles cases where email is already registered locally

## 🏗️ **Architecture**

### **Components**

1. **GoogleOAuthService**: Handles Google OAuth token verification and user info extraction
2. **AuthService**: Manages authentication logic and user creation
3. **GoogleOAuthResource**: REST controller for Google Sign-In endpoints
4. **User Entity**: Extended with Google OAuth fields
5. **Feature Flags**: Controls Google Sign-In availability

### **Data Flow**

```
Frontend → Gateway → User Service → Google OAuth Service → Google APIs
    ↓
JWT Token ← Auth Service ← User Creation/Login ← Token Verification
```

## 📋 **API Endpoints**

### **1. Google Sign-In**
```http
POST /ijaa/api/v1/user/google/signin
Content-Type: application/json

{
    "googleToken": "ya29.a0AfB_byC...",
    "idToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:**
```json
{
    "success": true,
    "message": "Google Sign-In successful",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "userId": "USER_ABC123XYZ"
    }
}
```

### **2. Get Google OAuth Configuration**
```http
GET /ijaa/api/v1/user/google/config
```

**Response:**
```json
{
    "success": true,
    "message": "Google OAuth configuration retrieved successfully",
    "data": {
        "clientId": "123456789-abcdef.apps.googleusercontent.com"
    }
}
```

## 🔧 **Configuration**

### **Environment Variables**
```bash
# Google OAuth2 Configuration
GOOGLE_CLIENT_ID=your-google-client-id-here
GOOGLE_CLIENT_SECRET=your-google-client-secret-here
```

### **Application Properties**
```yaml
google:
  oauth2:
    client-id: ${GOOGLE_CLIENT_ID:your-google-client-id-here}
    client-secret: ${GOOGLE_CLIENT_SECRET:your-google-client-secret-here}
```

## 🗄️ **Database Schema**

### **New Fields Added to Users Table**
```sql
ALTER TABLE users 
ADD COLUMN email VARCHAR(100) UNIQUE,
ADD COLUMN first_name VARCHAR(100),
ADD COLUMN last_name VARCHAR(100),
ADD COLUMN profile_picture_url VARCHAR(255),
ADD COLUMN google_id VARCHAR(100) UNIQUE,
ADD COLUMN auth_provider VARCHAR(20) DEFAULT 'LOCAL',
ADD COLUMN locale VARCHAR(100),
ADD COLUMN email_verified VARCHAR(10);
```

### **Indexes Created**
```sql
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_google_id ON users(google_id);
CREATE INDEX idx_users_auth_provider ON users(auth_provider);
```

## 🔐 **Security Implementation**

### **Token Verification Process**
1. **ID Token Verification**: Verifies Google ID token using Google's public keys
2. **Audience Validation**: Ensures token is intended for our application
3. **Access Token Verification**: Optionally verifies access token with Google APIs
4. **User Information Extraction**: Safely extracts user information from verified tokens

### **User Creation Logic**
1. **Email Check**: Verifies if email is already registered
2. **Conflict Resolution**: Handles cases where email exists with local authentication
3. **Profile Sync**: Automatically syncs Google profile information
4. **Secure Password**: Generates random password for Google users (not used for login)

## 🚩 **Feature Flag Integration**

### **Feature Flag: `user.google-signin`**
- **Purpose**: Controls Google Sign-In functionality
- **Default**: Enabled
- **Protection**: All Google Sign-In endpoints are protected by this feature flag

### **Usage in Code**
```java
@RequiresFeature("user.google-signin")
public ResponseEntity<ApiResponse<AuthResponse>> googleSignIn(...)
```

## 🧪 **Testing**

### **Test Scenarios**
1. **New User Sign-In**: First-time Google user registration
2. **Existing User Sign-In**: Returning Google user login
3. **Email Conflict**: User with same email exists locally
4. **Invalid Token**: Malformed or expired tokens
5. **Feature Flag Disabled**: Google Sign-In when feature is disabled

### **Running Tests**
```bash
cd user-service
mvn test -Dtest=GoogleOAuthServiceTest
mvn test -Dtest=AuthServiceTest#googleSignIn
```

## 🔄 **Frontend Integration**

### **Google Sign-In Button Implementation**
```javascript
// Initialize Google Sign-In
function initializeGoogleSignIn() {
    google.accounts.id.initialize({
        client_id: 'YOUR_GOOGLE_CLIENT_ID',
        callback: handleGoogleSignIn
    });
}

// Handle Google Sign-In
async function handleGoogleSignIn(response) {
    try {
        const result = await fetch('/ijaa/api/v1/user/google/signin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                googleToken: response.access_token,
                idToken: response.credential
            })
        });
        
        const data = await result.json();
        if (data.success) {
            // Store JWT token and redirect
            localStorage.setItem('token', data.data.token);
            window.location.href = '/dashboard';
        }
    } catch (error) {
        console.error('Google Sign-In failed:', error);
    }
}
```

### **Feature Flag Check**
```javascript
// Check if Google Sign-In is enabled
async function checkGoogleSignInEnabled() {
    try {
        const response = await fetch('/ijaa/api/v1/admin/feature-flags/user.google-signin/enabled');
        const data = await response.json();
        return data.data.enabled;
    } catch (error) {
        console.error('Failed to check feature flag:', error);
        return false;
    }
}
```

## 🚨 **Error Handling**

### **Common Error Scenarios**
1. **Invalid Token**: 400 Bad Request
2. **Email Already Registered**: 409 Conflict
3. **Feature Disabled**: 403 Forbidden
4. **Google API Error**: 500 Internal Server Error

### **Error Response Format**
```json
{
    "success": false,
    "message": "Error description",
    "data": null
}
```

## 📊 **Monitoring & Logging**

### **Key Log Events**
- Google token verification success/failure
- User creation from Google OAuth
- Email conflict resolution
- Feature flag checks

### **Metrics to Monitor**
- Google Sign-In success rate
- Token verification performance
- User creation rate
- Error rates by type

## 🔄 **Deployment Checklist**

### **Pre-Deployment**
- [ ] Set up Google OAuth2 credentials in Google Cloud Console
- [ ] Configure environment variables
- [ ] Run database migration
- [ ] Enable feature flag
- [ ] Test with Google test accounts

### **Post-Deployment**
- [ ] Verify Google Sign-In endpoints are accessible
- [ ] Test feature flag functionality
- [ ] Monitor error logs
- [ ] Validate JWT token generation
- [ ] Test user creation and login flows

## 🔧 **Troubleshooting**

### **Common Issues**

1. **"Invalid Google OAuth token"**
   - Check Google Client ID configuration
   - Verify token format and expiration
   - Ensure proper Google OAuth setup

2. **"Email already registered with local account"**
   - User needs to use local login or reset password
   - Consider implementing account linking

3. **"Feature flag disabled"**
   - Enable the `user.google-signin` feature flag
   - Check feature flag configuration

4. **"Google API error"**
   - Check network connectivity to Google APIs
   - Verify Google Cloud Console configuration
   - Check API quotas and limits

## 📚 **References**

- [Google OAuth2 Documentation](https://developers.google.com/identity/protocols/oauth2)
- [Google Sign-In for Web](https://developers.google.com/identity/sign-in/web)
- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [JWT Token Verification](https://developers.google.com/identity/sign-in/web/backend-auth)

---

**Implementation Status**: ✅ Complete  
**Last Updated**: August 2025  
**Next Review**: Monthly
