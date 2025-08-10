# 🗄️ IJAA Database Design - Visual Diagrams

## 📊 Complete Database Entity Relationship Diagram

```mermaid
erDiagram
    %% Core User Management
    users {
        bigint id PK
        varchar user_id UK
        varchar username UK
        varchar password
        boolean active
    }
    
    admins {
        bigint id PK
        varchar name
        varchar email UK
        varchar password_hash
        varchar role
        boolean active
        timestamp created_at
        timestamp updated_at
    }
    
    profiles {
        bigint id PK
        varchar username UK
        varchar user_id UK
        varchar name
        varchar profession
        varchar location
        text bio
        varchar phone
        varchar linkedin
        varchar website
        varchar batch
        varchar email
        varchar facebook
        boolean show_phone
        boolean show_linkedin
        boolean show_website
        boolean show_email
        boolean show_facebook
        int connections
        timestamp created_at
        timestamp updated_at
    }
    
    %% Event Management
    events {
        bigint id PK
        varchar title
        text description
        timestamp start_date
        timestamp end_date
        varchar location
        varchar event_type
        boolean active
        varchar privacy
        varchar invite_message
        boolean is_online
        varchar meeting_link
        int max_participants
        int current_participants
        varchar organizer_name
        varchar organizer_email
        varchar created_by_username
        timestamp created_at
        timestamp updated_at
    }
    
    event_participations {
        bigint id PK
        bigint event_id FK
        varchar participant_username
        varchar status
        varchar message
        timestamp created_at
        timestamp updated_at
    }
    
    event_invitations {
        bigint id PK
        bigint event_id FK
        varchar invited_username
        varchar invited_by_username
        varchar personal_message
        boolean is_read
        boolean is_responded
        timestamp created_at
        timestamp updated_at
    }
    
    event_comments {
        bigint id PK
        bigint event_id FK
        varchar username
        text content
        boolean is_edited
        boolean is_deleted
        bigint parent_comment_id FK
        int likes
        int replies
        timestamp created_at
        timestamp updated_at
    }
    
    event_media {
        bigint id PK
        bigint event_id FK
        varchar uploaded_by_username
        varchar file_name
        varchar file_url
        varchar file_type
        bigint file_size
        varchar caption
        varchar media_type
        boolean is_approved
        int likes
        timestamp created_at
        timestamp updated_at
    }
    
    event_reminders {
        bigint id PK
        bigint event_id FK
        varchar username
        timestamp reminder_time
        varchar reminder_type
        boolean is_sent
        boolean is_active
        varchar custom_message
        varchar channel
        timestamp created_at
        timestamp updated_at
    }
    
    %% Advanced Event Features
    recurring_events {
        bigint id PK
        varchar title
        text description
        timestamp start_date
        timestamp end_date
        varchar location
        varchar event_type
        boolean active
        varchar privacy
        varchar invite_message
        boolean is_online
        varchar meeting_link
        int max_participants
        int current_participants
        varchar organizer_name
        varchar organizer_email
        varchar created_by_username
        varchar recurrence_type
        int recurrence_interval
        timestamp recurrence_end_date
        varchar recurrence_days
        int max_occurrences
        boolean generate_instances
        timestamp created_at
        timestamp updated_at
    }
    
    event_templates {
        bigint id PK
        varchar template_name
        varchar name
        varchar created_by_username
        varchar category
        boolean is_public
        boolean is_active
        varchar title
        text description
        varchar location
        varchar event_type
        boolean is_online
        varchar meeting_link
        int max_participants
        varchar organizer_name
        varchar organizer_email
        varchar invite_message
        varchar privacy
        int default_duration_minutes
        time default_start_time
        time default_end_time
        boolean supports_recurrence
        varchar default_recurrence_type
        int default_recurrence_interval
        varchar default_recurrence_days
        int usage_count
        double average_rating
        int total_ratings
        varchar tags
        timestamp created_at
        timestamp updated_at
    }
    
    %% Analytics
    event_analytics {
        bigint id PK
        bigint event_id FK
        varchar event_title
        varchar organizer_username
        int total_invitations
        int confirmed_attendees
        int actual_attendees
        int no_shows
        int maybe_attendees
        int declined_attendees
        int pending_responses
        int total_comments
        int total_media_uploads
        int total_reminders
        timestamp first_rsvp_time
        timestamp last_rsvp_time
        int average_response_time_hours
        double attendance_rate
        double response_rate
        double engagement_rate
        boolean is_completed
        timestamp event_start_date
        timestamp event_end_date
        timestamp created_at
        timestamp updated_at
        timestamp last_updated
    }
    
    %% Networking
    connections {
        bigint id PK
        varchar requester_username
        varchar receiver_username
        varchar status
        timestamp created_at
    }
    
    interests {
        bigint id PK
        varchar username
        varchar user_id
        varchar interest
        timestamp created_at
        timestamp updated_at
    }
    
    experiences {
        bigint id PK
        varchar username
        varchar user_id
        varchar title
        varchar company
        varchar period
        text description
        timestamp created_at
        timestamp updated_at
    }
    
    %% Alumni Directory (using existing profiles and interests tables)
    %% alumni_profiles - REMOVED (redundant with profiles table)
    %% alumni_skills - REMOVED (redundant with interests table)
    
    %% Calendar Integration
    calendar_integrations {
        bigint id PK
        varchar username
        varchar calendar_type
        varchar calendar_name
        varchar calendar_url
        varchar access_token
        varchar refresh_token
        timestamp token_expiry
        varchar calendar_id
        boolean is_active
        boolean sync_to_external
        boolean sync_from_external
        boolean sync_recurring_events
        boolean sync_reminders
        varchar last_sync_error
        timestamp last_sync_time
        int sync_frequency_hours
        timestamp created_at
        timestamp updated_at
    }
    
    %% Content Management
    announcements {
        bigint id PK
        varchar title
        text content
        varchar category
        boolean active
        boolean is_urgent
        varchar author_name
        varchar author_email
        varchar image_url
        int view_count
        timestamp created_at
        timestamp updated_at
    }
    
    reports {
        bigint id PK
        varchar title
        text description
        varchar category
        varchar status
        varchar priority
        varchar reporter_name
        varchar reporter_email
        varchar assigned_to
        text admin_notes
        varchar attachment_url
        timestamp created_at
        timestamp updated_at
    }
    
    %% Feature Flags
    feature_flags {
        bigint id PK
        varchar feature_name UK
        boolean enabled
        varchar description
        timestamp created_at
        timestamp updated_at
    }
    
    %% Relationships
    users ||--|| profiles : "has"
    events ||--o{ event_participations : "has"
    events ||--o{ event_invitations : "has"
    events ||--o{ event_comments : "has"
    events ||--o{ event_media : "has"
    events ||--o{ event_reminders : "has"
    events ||--o{ event_analytics : "tracks"
    event_comments ||--o{ event_comments : "replies_to"
    recurring_events ||--o{ event_participations : "has"
    event_templates ||--o{ events : "creates"
    profiles ||--o{ interests : "has"
    calendar_integrations ||--o{ events : "syncs"
```

## 🎯 Core Module Relationships

### 1. **User Management Module**
```mermaid
graph TB
    subgraph "User Management"
        A[users] --> B[profiles]
        C[admins]
        D[UserPrincipal]
    end
    
    subgraph "User Data"
        E[interests]
        F[experiences]
        G[connections]
    end
    
    A --> E
    A --> F
    A --> G
    B --> G
```

### 2. **Event Management Module**
```mermaid
graph TB
    subgraph "Event Core"
        A[events] --> B[event_participations]
        A --> C[event_invitations]
        A --> D[event_comments]
        A --> E[event_media]
        A --> F[event_reminders]
    end
    
    subgraph "Advanced Events"
        G[recurring_events] --> B
        H[event_templates] --> A
        I[event_analytics] --> A
    end
    
    subgraph "Event Features"
        D --> J[Threaded Comments]
        E --> K[Media Approval]
        F --> L[Notification Channels]
    end
```

### 3. **Networking & Alumni Directory**
```mermaid
graph TB
    subgraph "Networking"
        A[connections] --> B[Connection Status]
        C[profiles] --> A
    end
    
    subgraph "Alumni Directory"
        D[profiles] --> E[interests]
        F[experiences] --> D
    end
    
    subgraph "Search & Discovery"
        H[Alumni Search]
        I[Profile Visibility]
        J[Connection Count]
    end
    
    D --> H
    D --> I
    D --> J
```

### 4. **Analytics & Reporting**
```mermaid
graph TB
    subgraph "Event Analytics"
        A[event_analytics] --> B[Attendance Tracking]
        A --> C[Engagement Metrics]
        A --> D[Response Time Analysis]
        A --> E[Performance Indicators]
    end
    
    subgraph "Content Management"
        F[announcements] --> G[Content Categories]
        H[reports] --> I[Report Types]
        H --> J[Priority Levels]
    end
    
    subgraph "Feature Control"
        K[feature_flags] --> L[Feature Management]
        K --> M[Rollout Control]
    end
```

## 🔄 Data Flow Diagrams

### 1. **Event Creation & Management Flow**
```mermaid
flowchart TD
    A[User Creates Event] --> B{Event Type?}
    B -->|Single Event| C[events table]
    B -->|Recurring Event| D[recurring_events table]
    B -->|From Template| E[event_templates table]
    
    C --> F[Generate Invitations]
    D --> F
    E --> F
    
    F --> G[event_invitations table]
    G --> H[Send Notifications]
    H --> I[Track Responses]
    I --> J[event_participations table]
    
    J --> K[Update Analytics]
    K --> L[event_analytics table]
    
    C --> M[Event Comments]
    C --> N[Event Media]
    C --> O[Event Reminders]
    
    M --> P[event_comments table]
    N --> Q[event_media table]
    O --> R[event_reminders table]
```

### 2. **User Registration & Profile Flow**
```mermaid
flowchart TD
    A[User Registration] --> B[users table]
    B --> C[Create Profile]
    C --> D[profiles table]
    
    D --> E[Add Interests]
    D --> F[Add Experiences]
    D --> G[Create Alumni Profile]
    
    E --> H[interests table]
    F --> I[experiences table]
    G --> J[profiles table]
    
    J --> K[Add Interests/Skills]
    K --> L[interests table]
    
    D --> M[Connection Requests]
    M --> N[connections table]
    
    N --> O[Update Connection Count]
    O --> P[profiles table]
```

### 3. **Calendar Integration Flow**
```mermaid
flowchart TD
    A[User Connects Calendar] --> B[calendar_integrations table]
    B --> C{Integration Type?}
    
    C -->|Google Calendar| D[Google OAuth]
    C -->|Outlook Calendar| E[Outlook OAuth]
    C -->|Apple Calendar| F[Apple OAuth]
    
    D --> G[Store Tokens]
    E --> G
    F --> G
    
    G --> H[Sync Settings]
    H --> I[Sync Frequency]
    
    I --> J[Sync IJAA Events]
    I --> K[Sync External Events]
    
    J --> L[events table]
    K --> M[External Event Processing]
    
    L --> N[Update Analytics]
    M --> N
```

## 📈 Database Schema Summary

### **Core Tables (15)**
1. `users` - Basic user authentication
2. `admins` - Administrative users
3. `profiles` - User profile information (includes alumni data)
4. `events` - Event management
5. `event_participations` - Event attendance
6. `event_invitations` - Event invitations
7. `event_comments` - Event discussions
8. `event_media` - Event media files
9. `event_reminders` - Event notifications
10. `recurring_events` - Recurring event patterns
11. `event_templates` - Event templates
12. `event_analytics` - Event performance metrics
13. `connections` - User networking
14. `interests` - User interests and skills
15. `experiences` - User work experience

### **Supporting Tables (4)**
16. `calendar_integrations` - External calendar sync
17. `announcements` - System announcements
18. `reports` - User reports and feedback
19. `feature_flags` - Feature control system

### **Key Features**
- **19 Total Tables** with comprehensive relationships
- **Audit Trail** with created_at/updated_at timestamps
- **Soft Deletes** for data preservation
- **Privacy Controls** for events and profiles
- **Analytics Tracking** for performance monitoring
- **Feature Flags** for dynamic feature control
- **Calendar Integration** for external sync
- **Moderation System** for content management

## 🎨 Visual Database Architecture

```mermaid
graph TB
    subgraph "Frontend Layer"
        A[React/Vue/Angular]
    end
    
    subgraph "API Gateway"
        B[Gateway Service]
    end
    
    subgraph "Microservices"
        C[User Service]
        D[Event Service]
        E[Analytics Service]
    end
    
    subgraph "Database Layer"
        F[(PostgreSQL Database)]
    end
    
    subgraph "External Services"
        G[Google Calendar]
        H[Email Service]
        I[File Storage]
    end
    
    A --> B
    B --> C
    B --> D
    B --> E
    C --> F
    D --> F
    E --> F
    D --> G
    D --> H
    D --> I
```

## 🔄 **Database Optimization Summary**

### **Removed Redundant Tables:**
- ❌ `alumni_profiles` - Redundant with existing `profiles` table
- ❌ `alumni_skills` - Redundant with existing `interests` table

### **Benefits of Optimization:**
- ✅ **Reduced Complexity** - From 21 to 19 tables
- ✅ **Eliminated Data Duplication** - Single source of truth for user profiles
- ✅ **Simplified Maintenance** - Fewer tables to manage and maintain
- ✅ **Better Performance** - Reduced joins and data redundancy
- ✅ **Consistent Data Model** - Unified approach to user data

### **Alumni Functionality Now Uses:**
- **`profiles`** table for all alumni profile information
- **`interests`** table for skills, interests, and expertise
- **`experiences`** table for work history and achievements
- **`connections`** table for networking relationships

### **Search & Discovery Features:**
The existing `AlumniSearchService` implementation already correctly uses the `profiles` and `interests` tables for alumni search functionality, making the separate `AlumniSearch` entity unnecessary.

---

This comprehensive diagram view provides a clear understanding of the optimized IJAA database structure, relationships, and data flow patterns. The visual representations make it easy to grasp the complex relationships between different entities and understand how data flows through the system.
