# 🗄️ IJAA Database Separation Guide

## 📋 Overview

This guide provides step-by-step instructions for properly separating the IJAA database into microservice-specific databases to maintain clean boundaries and improve scalability.

## 🎯 Current Status

### ✅ Already Implemented:
- **Event Service**: Already using `ijaa_events` database
- **User Service**: Updated to use `ijaa_users` database
- **Entity Separation**: Event entities are properly separated in event service

### 🔄 What Needs to be Done:
- Create the new databases
- Migrate existing data
- Verify the separation
- Update documentation

---

## 🚀 Implementation Steps

### Step 1: Create the New Databases

```bash
# Connect to PostgreSQL as superuser
sudo -u postgres psql

# Create the new databases
CREATE DATABASE ijaa_users;
CREATE DATABASE ijaa_events;

# Grant permissions
GRANT ALL PRIVILEGES ON DATABASE ijaa_users TO root;
GRANT ALL PRIVILEGES ON DATABASE ijaa_events TO root;

# Exit PostgreSQL
\q
```

### Step 2: Set Up Database Schema

```bash
# Run the database setup script
psql -h localhost -U root -d postgres -f database-setup.sql
```

### Step 3: Migrate Existing Data (if any)

```bash
# First, create a backup of your current database
pg_dump -h localhost -U root -d ijaa > ijaa_backup_$(date +%Y%m%d_%H%M%S).sql

# Run the migration script
psql -h localhost -U root -d postgres -f database-migration.sql
```

### Step 4: Verify the Setup

```bash
# Check User Service Database
psql -h localhost -U root -d ijaa_users -c "\dt"

# Check Event Service Database
psql -h localhost -U root -d ijaa_events -c "\dt"
```

---

## 📊 Database Architecture

### User Service Database (`ijaa_users`)

**Purpose**: User management, authentication, profiles, networking

**Tables**:
- `users` - User accounts and authentication
- `admins` - Administrative users
- `profiles` - User profile information
- `connections` - User networking relationships
- `interests` - User interests and skills
- `experiences` - User work experience
- `announcements` - System announcements
- `reports` - User reports and feedback
- `feature_flags` - Feature control system

### Event Service Database (`ijaa_events`)

**Purpose**: Event management, participation, analytics

**Tables**:
- `events` - Event management
- `event_participations` - Event attendance
- `event_invitations` - Event invitations
- `event_comments` - Event discussions
- `event_media` - Event media files
- `event_reminders` - Event notifications
- `recurring_events` - Recurring event patterns
- `event_templates` - Event templates
- `event_analytics` - Event performance metrics
- `calendar_integrations` - External calendar sync

---

## 🔧 Configuration Updates

### User Service Configuration

The user service configuration has been updated to use the dedicated database:

```yaml
spring:
  application:
    name: "users"
  datasource:
    url: jdbc:postgresql://localhost:5432/ijaa_users
    username: root
    password: Admin@123
    driver-class-name: org.postgresql.Driver
```

### Event Service Configuration

The event service is already properly configured:

```yaml
spring:
  application:
    name: "event"
  datasource:
    url: jdbc:postgresql://localhost:5432/ijaa_events
    username: root
    password: Admin@123
    driver-class-name: org.postgresql.Driver
```

---

## 🧪 Testing the Separation

### 1. Start the Services

```bash
# Start User Service
cd user-service && ./mvnw spring-boot:run

# Start Event Service
cd event-service && ./mvnw spring-boot:run
```

### 2. Verify Database Connections

Check the application logs to ensure each service connects to its respective database:

**User Service Logs**:
```
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
```

**Event Service Logs**:
```
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
```

### 3. Test API Endpoints

**User Service Endpoints**:
```bash
# Test user registration
curl -X POST http://localhost:8081/api/v1/user/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123","email":"test@example.com"}'

# Test user profile
curl -X GET http://localhost:8081/api/v1/user/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Event Service Endpoints**:
```bash
# Test event creation
curl -X POST http://localhost:8082/api/v1/events/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"title":"Test Event","description":"Test Description","startDate":"2024-12-25T10:00:00","endDate":"2024-12-25T12:00:00"}'

# Test event listing
curl -X GET http://localhost:8082/api/v1/events \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 🔍 Verification Checklist

### ✅ Database Setup
- [ ] `ijaa_users` database created
- [ ] `ijaa_events` database created
- [ ] All tables created in respective databases
- [ ] Indexes created for performance
- [ ] Default data inserted (feature flags)

### ✅ Configuration
- [ ] User service points to `ijaa_users`
- [ ] Event service points to `ijaa_events`
- [ ] Database credentials configured correctly
- [ ] Connection pools configured

### ✅ Data Migration
- [ ] Existing data migrated (if applicable)
- [ ] Data integrity verified
- [ ] Sequences updated
- [ ] Backup created

### ✅ Service Testing
- [ ] User service starts successfully
- [ ] Event service starts successfully
- [ ] API endpoints respond correctly
- [ ] Database operations work as expected

### ✅ Integration Testing
- [ ] Service-to-service communication works
- [ ] JWT authentication works across services
- [ ] Gateway routing works correctly
- [ ] End-to-end workflows function properly

---

## 🚨 Important Considerations

### 1. Data Consistency
- Each service owns its data completely
- No shared database tables between services
- Use service-to-service communication for cross-service data needs

### 2. Transaction Management
- Each service manages its own transactions
- No distributed transactions across services
- Use eventual consistency patterns where needed

### 3. Backup Strategy
- Backup each database separately
- Schedule regular backups for both databases
- Test backup restoration procedures

### 4. Monitoring
- Monitor each database separately
- Set up alerts for database performance
- Track service-specific metrics

---

## 🔧 Troubleshooting

### Common Issues

**1. Database Connection Errors**
```bash
# Check if databases exist
psql -h localhost -U root -l | grep ijaa

# Check database permissions
psql -h localhost -U root -d ijaa_users -c "\du"
```

**2. Service Startup Failures**
```bash
# Check application logs
tail -f user-service/app.log
tail -f event-service/app.log

# Check database connectivity
psql -h localhost -U root -d ijaa_users -c "SELECT 1;"
psql -h localhost -U root -d ijaa_events -c "SELECT 1;"
```

**3. Data Migration Issues**
```bash
# Verify table counts
psql -h localhost -U root -d ijaa_users -c "SELECT COUNT(*) FROM users;"
psql -h localhost -U root -d ijaa_events -c "SELECT COUNT(*) FROM events;"
```

### Recovery Procedures

**1. Rollback to Single Database**
```bash
# Update user service configuration back to original database
# Restore from backup if needed
psql -h localhost -U root -d ijaa < ijaa_backup_YYYYMMDD_HHMMSS.sql
```

**2. Recreate Separated Databases**
```bash
# Drop and recreate databases
DROP DATABASE IF EXISTS ijaa_users;
DROP DATABASE IF EXISTS ijaa_events;
# Run database-setup.sql again
```

---

## 📈 Benefits of Database Separation

### 1. **Scalability**
- Each service can scale independently
- Database resources allocated per service needs
- Better performance isolation

### 2. **Maintainability**
- Clear ownership of data
- Easier to understand and modify
- Reduced coupling between services

### 3. **Reliability**
- Service failures don't affect other services
- Independent backup and recovery
- Better fault isolation

### 4. **Development**
- Teams can work independently
- Easier to test and deploy
- Clear service boundaries

---

## 🎯 Next Steps

### Immediate Actions
1. ✅ Update user service database configuration
2. ✅ Create database setup scripts
3. ✅ Create migration scripts
4. 🔄 Execute database setup
5. 🔄 Test service functionality
6. 🔄 Verify data integrity

### Future Enhancements
1. **Database Monitoring**: Set up monitoring for both databases
2. **Performance Optimization**: Optimize queries and indexes
3. **Backup Automation**: Automate backup procedures
4. **Data Archiving**: Implement data archiving strategies
5. **Multi-Region**: Consider multi-region database deployment

---

## 📞 Support

If you encounter any issues during the database separation process:

1. **Check the logs**: Review application and database logs
2. **Verify configuration**: Ensure all configuration files are correct
3. **Test connectivity**: Verify database connections
4. **Review this guide**: Follow the troubleshooting steps
5. **Create backup**: Always have a backup before making changes

---

*This guide ensures a smooth transition to a properly separated microservices database architecture while maintaining data integrity and system functionality.*
