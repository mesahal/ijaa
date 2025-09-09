# AWS Free Tier Deployment Guide for IJAA Microservices

## Overview
This guide will help you deploy the IJAA microservices to AWS using the free tier. We'll use AWS EC2 for hosting services and RDS for PostgreSQL databases.

## Prerequisites
- AWS Account (free tier eligible)
- Basic knowledge of AWS services
- SSH key pair for EC2 access
- Domain name (optional, for production)

## AWS Free Tier Limits
- **EC2**: 750 hours/month (t2.micro instances)
- **RDS**: 750 hours/month (db.t3.micro)
- **EBS**: 30 GB storage
- **Data Transfer**: 15 GB/month outbound

## Architecture Overview
```
Internet → Route 53 → Application Load Balancer → EC2 Instances
                                    ↓
                              RDS PostgreSQL
```

## Step 1: Create RDS Database (Free Tier)

### 1.1 Launch RDS Instance
1. Go to AWS RDS Console
2. Click "Create database"
3. Choose "Standard create"
4. Select "PostgreSQL" as engine
5. Choose "Free tier" template
6. Configure:
   - **DB instance identifier**: `ijaa-main-db`
   - **Master username**: `ijaa_admin`
   - **Master password**: `[strong-password]`
   - **DB instance class**: `db.t3.micro`
   - **Storage**: 20 GB (free tier)
   - **Multi-AZ deployment**: No (free tier limitation)
   - **Public access**: Yes (for development)
   - **VPC security group**: Create new

### 1.2 Create Security Group for RDS
- **Name**: `ijaa-rds-sg`
- **Description**: Security group for IJAA RDS database
- **Inbound rules**:
  - Type: PostgreSQL, Port: 5432, Source: `ijaa-ec2-sg` (EC2 security group)

### 1.3 Create Databases
Connect to your RDS instance and create the required databases:

```sql
-- Connect to RDS instance
psql -h [RDS_ENDPOINT] -U ijaa_admin -d postgres

-- Create databases
CREATE DATABASE ijaa_users;
CREATE DATABASE ijaa_events;
CREATE DATABASE ijaa_files;

-- Create users for each service
CREATE USER ijaa_user WITH PASSWORD 'user_password';
CREATE USER ijaa_event WITH PASSWORD 'event_password';
CREATE USER ijaa_file WITH PASSWORD 'file_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE ijaa_users TO ijaa_user;
GRANT ALL PRIVILEGES ON DATABASE ijaa_events TO ijaa_event;
GRANT ALL PRIVILEGES ON DATABASE ijaa_files TO ijaa_file;
```

## Step 2: Create EC2 Instance (Free Tier)

### 2.1 Launch EC2 Instance
1. Go to AWS EC2 Console
2. Click "Launch Instance"
3. Configure:
   - **Name**: `ijaa-microservices`
   - **AMI**: Amazon Linux 2023 (free tier eligible)
   - **Instance type**: `t2.micro`
   - **Key pair**: Create or select existing
   - **Network settings**: Create security group
   - **Storage**: 8 GB (free tier)

### 2.2 Create Security Group for EC2
- **Name**: `ijaa-ec2-sg`
- **Description**: Security group for IJAA microservices
- **Inbound rules**:
  - Type: SSH, Port: 22, Source: Your IP
  - Type: HTTP, Port: 80, Source: 0.0.0.0/0
  - Type: Custom TCP, Port: 8080, Source: 0.0.0.0/0 (Gateway)
  - Type: Custom TCP, Port: 8081, Source: 0.0.0.0/0 (User Service)
  - Type: Custom TCP, Port: 8082, Source: 0.0.0.0/0 (Event Service)
  - Type: Custom TCP, Port: 8083, Source: 0.0.0.0/0 (File Service)
  - Type: Custom TCP, Port: 8761, Source: 0.0.0.0/0 (Discovery)
  - Type: Custom TCP, Port: 8888, Source: 0.0.0.0/0 (Config)

## Step 3: Install Dependencies on EC2

### 3.1 Connect to EC2 Instance
```bash
ssh -i your-key.pem ec2-user@your-ec2-public-ip
```

### 3.2 Update System and Install Java
```bash
# Update system
sudo yum update -y

# Install Java 17
sudo yum install -y java-17-amazon-corretto

# Verify Java installation
java -version
```

### 3.3 Install Docker
```bash
# Install Docker
sudo yum install -y docker

# Start Docker service
sudo systemctl start docker
sudo systemctl enable docker

# Add ec2-user to docker group
sudo usermod -a -G docker ec2-user

# Logout and login again for group changes to take effect
exit
# SSH back in
```

### 3.4 Install Docker Compose
```bash
# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# Make it executable
sudo chmod +x /usr/local/bin/docker-compose

# Verify installation
docker-compose --version
```

## Step 4: Prepare Application for AWS

### 4.1 Clone Repository
```bash
# Install Git
sudo yum install -y git

# Clone your repository
git clone https://github.com/your-username/ijaa.git
cd ijaa
```

### 4.2 Create AWS Production Configuration
Create `docker-compose-aws.yml`:

```yaml
version: '3.8'

services:
  # Discovery Service
  discovery-service:
    build:
      context: ./discovery-service
      dockerfile: Dockerfile
    container_name: ijaa-discovery-service
    ports:
      - "8761:8761"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SERVER_PORT=8761
      - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/
      - EUREKA_INSTANCE_HOSTNAME=localhost
      - JWT_SECRET=${JWT_SECRET}
    networks:
      - ijaa-network

  # Config Service
  config-service:
    build:
      context: ./config-service
      dockerfile: Dockerfile
    container_name: ijaa-config-service
    ports:
      - "8888:8888"
    environment:
      - SPRING_PROFILES_ACTIVE=native
      - SERVER_PORT=8888
      - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/
      - EUREKA_INSTANCE_HOSTNAME=localhost
      - JWT_SECRET=${JWT_SECRET}
    depends_on:
      - discovery-service
    networks:
      - ijaa-network

  # User Service
  user-service:
    build:
      context: ./user-service
      dockerfile: Dockerfile
    container_name: ijaa-user-service
    ports:
      - "8081:8081"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SERVER_PORT=8081
      - SPRING_DATASOURCE_URL=${USER_DB_URL}
      - SPRING_DATASOURCE_USERNAME=${USER_DB_USERNAME}
      - SPRING_DATASOURCE_PASSWORD=${USER_DB_PASSWORD}
      - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/
      - EUREKA_INSTANCE_HOSTNAME=localhost
      - JWT_SECRET=${JWT_SECRET}
      - PRODUCTION_SERVER_URL=${EC2_PUBLIC_IP}
      - GATEWAY_SERVER_URL=${EC2_PUBLIC_IP}
    depends_on:
      - discovery-service
    networks:
      - ijaa-network

  # Event Service
  event-service:
    build:
      context: ./event-service
      dockerfile: Dockerfile
    container_name: ijaa-event-service
    ports:
      - "8082:8082"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SERVER_PORT=8082
      - SPRING_DATASOURCE_URL=${EVENT_DB_URL}
      - SPRING_DATASOURCE_USERNAME=${EVENT_DB_USERNAME}
      - SPRING_DATASOURCE_PASSWORD=${EVENT_DB_PASSWORD}
      - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/
      - EUREKA_INSTANCE_HOSTNAME=localhost
      - JWT_SECRET=${JWT_SECRET}
      - PRODUCTION_SERVER_URL=${EC2_PUBLIC_IP}
      - GATEWAY_SERVER_URL=${EC2_PUBLIC_IP}
    depends_on:
      - discovery-service
    networks:
      - ijaa-network

  # File Service
  file-service:
    build:
      context: ./file-service
      dockerfile: Dockerfile
    container_name: ijaa-file-service
    ports:
      - "8083:8083"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SERVER_PORT=8083
      - SPRING_DATASOURCE_URL=${FILE_DB_URL}
      - SPRING_DATASOURCE_USERNAME=${FILE_DB_USERNAME}
      - SPRING_DATASOURCE_PASSWORD=${FILE_DB_PASSWORD}
      - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/
      - EUREKA_INSTANCE_HOSTNAME=localhost
      - JWT_SECRET=${JWT_SECRET}
      - PRODUCTION_SERVER_URL=${EC2_PUBLIC_IP}
      - GATEWAY_SERVER_URL=${EC2_PUBLIC_IP}
      - FILE_STORAGE_PATH=/tmp/uploaded-files
    depends_on:
      - discovery-service
    networks:
      - ijaa-network

  # Gateway Service
  gateway-service:
    build:
      context: ./gateway-service
      dockerfile: Dockerfile
    container_name: ijaa-gateway-service
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SERVER_PORT=8080
      - EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://localhost:8761/eureka/
      - EUREKA_INSTANCE_HOSTNAME=localhost
      - JWT_SECRET=${JWT_SECRET}
      - PRODUCTION_SERVER_URL=${EC2_PUBLIC_IP}
    depends_on:
      - discovery-service
      - user-service
      - event-service
      - file-service
    networks:
      - ijaa-network

networks:
  ijaa-network:
    driver: bridge
```

### 4.3 Create Environment File
Create `.env` file:

```bash
# JWT Secret (generate a strong secret)
JWT_SECRET=your-super-secret-jwt-key-here

# EC2 Public IP
EC2_PUBLIC_IP=your-ec2-public-ip

# Database URLs (replace with your RDS endpoint)
USER_DB_URL=jdbc:postgresql://your-rds-endpoint:5432/ijaa_users?sslmode=require
EVENT_DB_URL=jdbc:postgresql://your-rds-endpoint:5432/ijaa_events?sslmode=require
FILE_DB_URL=jdbc:postgresql://your-rds-endpoint:5432/ijaa_files?sslmode=require

# Database Credentials
USER_DB_USERNAME=ijaa_user
USER_DB_PASSWORD=user_password
EVENT_DB_USERNAME=ijaa_event
EVENT_DB_PASSWORD=event_password
FILE_DB_USERNAME=ijaa_file
FILE_DB_PASSWORD=file_password
```

## Step 5: Deploy Application

### 5.1 Build and Start Services
```bash
# Build all services
docker-compose -f docker-compose-aws.yml build

# Start services
docker-compose -f docker-compose-aws.yml up -d

# Check service status
docker-compose -f docker-compose-aws.yml ps

# View logs
docker-compose -f docker-compose-aws.yml logs -f
```

### 5.2 Verify Deployment
```bash
# Check if services are running
curl http://localhost:8761/actuator/health  # Discovery
curl http://localhost:8888/actuator/health  # Config
curl http://localhost:8081/actuator/health  # User
curl http://localhost:8082/actuator/health  # Event
curl http://localhost:8083/actuator/health  # File
curl http://localhost:8080/actuator/health  # Gateway
```

## Step 6: Configure Nginx (Optional but Recommended)

### 6.1 Install Nginx
```bash
sudo yum install -y nginx
sudo systemctl start nginx
sudo systemctl enable nginx
```

### 6.2 Configure Nginx
Create `/etc/nginx/conf.d/ijaa.conf`:

```nginx
server {
    listen 80;
    server_name your-domain.com;  # Replace with your domain

    # Gateway Service
    location /ijaa/ {
        proxy_pass http://localhost:8080/ijaa/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Discovery Service
    location /discovery/ {
        proxy_pass http://localhost:8761/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Health Check
    location /health {
        proxy_pass http://localhost:8080/actuator/health;
    }
}
```

### 6.3 Restart Nginx
```bash
sudo nginx -t  # Test configuration
sudo systemctl restart nginx
```

## Step 7: SSL Certificate (Optional)

### 7.1 Install Certbot
```bash
sudo yum install -y certbot python3-certbot-nginx
```

### 7.2 Get SSL Certificate
```bash
sudo certbot --nginx -d your-domain.com
```

## Step 8: Monitoring and Maintenance

### 8.1 Create Startup Script
Create `/home/ec2-user/start-ijaa.sh`:

```bash
#!/bin/bash
cd /home/ec2-user/ijaa
docker-compose -f docker-compose-aws.yml up -d
```

Make it executable:
```bash
chmod +x /home/ec2-user/start-ijaa.sh
```

### 8.2 Add to Crontab for Auto-start
```bash
crontab -e
# Add this line:
@reboot /home/ec2-user/start-ijaa.sh
```

### 8.3 Log Rotation
Create log rotation configuration to prevent disk space issues.

## Step 9: Cost Optimization

### 9.1 Free Tier Monitoring
- Monitor AWS Billing Dashboard
- Set up billing alerts
- Use AWS Cost Explorer

### 9.2 Resource Optimization
- Use t2.micro instances (free tier)
- Monitor RDS storage usage
- Implement auto-scaling policies

## Troubleshooting

### Common Issues

#### 1. Services Not Starting
```bash
# Check Docker logs
docker-compose -f docker-compose-aws.yml logs

# Check system resources
free -h
df -h
```

#### 2. Database Connection Issues
- Verify RDS security group allows EC2 access
- Check database credentials in .env file
- Ensure RDS instance is running

#### 3. Port Conflicts
```bash
# Check which ports are in use
sudo netstat -tlnp | grep :8080
```

#### 4. Memory Issues
- t2.micro has limited RAM (1GB)
- Consider upgrading to t3.micro if needed
- Monitor memory usage with `htop`

## Security Considerations

### 1. Network Security
- Use security groups to restrict access
- Only open necessary ports
- Use private subnets for RDS

### 2. Application Security
- Use strong JWT secrets
- Implement rate limiting
- Regular security updates

### 3. Database Security
- Use SSL connections
- Strong passwords
- Regular backups

## Backup Strategy

### 1. Database Backups
- Enable automated RDS backups
- Test restore procedures
- Store backups in S3

### 2. Application Backups
- Version control your code
- Backup configuration files
- Document deployment procedures

## Next Steps

### 1. Production Readiness
- Set up monitoring (CloudWatch)
- Implement logging aggregation
- Performance testing

### 2. Scaling
- Load balancer for multiple EC2 instances
- RDS read replicas
- Auto-scaling groups

### 3. CI/CD
- GitHub Actions for automated deployment
- Docker image registry
- Blue-green deployment

## Cost Estimation (Free Tier)

### Monthly Costs (Free Tier)
- **EC2**: $0 (750 hours/month)
- **RDS**: $0 (750 hours/month)
- **EBS**: $0 (30 GB)
- **Data Transfer**: $0 (15 GB/month)

### Post Free Tier (Estimated)
- **EC2 t2.micro**: ~$8-12/month
- **RDS db.t3.micro**: ~$15-20/month
- **EBS Storage**: ~$1-2/month
- **Data Transfer**: ~$5-10/month

**Total**: ~$30-45/month after free tier

## Support and Resources

### AWS Documentation
- [EC2 User Guide](https://docs.aws.amazon.com/ec2/)
- [RDS User Guide](https://docs.aws.amazon.com/rds/)
- [VPC User Guide](https://docs.aws.amazon.com/vpc/)

### Community Resources
- AWS Developer Forums
- Stack Overflow
- GitHub Issues

---

**Note**: This guide assumes basic AWS knowledge. For production deployments, consider additional security measures, monitoring, and backup strategies.
