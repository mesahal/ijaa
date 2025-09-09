#!/bin/bash

# IJAA Microservices AWS Deployment Script

echo "🚀 IJAA Microservices AWS Deployment"
echo "===================================="

# Check if .env file exists
if [ ! -f .env ]; then
    echo "❌ Error: .env file not found!"
    echo "Please copy env.example to .env and configure your environment variables"
    exit 1
fi

# Load environment variables
source .env

# Validate required environment variables
if [ -z "$JWT_SECRET" ] || [ "$JWT_SECRET" = "your-super-secret-jwt-key-here-make-it-at-least-64-characters-long" ]; then
    echo "❌ Error: JWT_SECRET not configured in .env file"
    exit 1
fi

if [ -z "$EC2_PUBLIC_IP" ] || [ "$EC2_PUBLIC_IP" = "your-ec2-public-ip-here" ]; then
    echo "❌ Error: EC2_PUBLIC_IP not configured in .env file"
    exit 1
fi

if [ -z "$USER_DB_URL" ] || [ "$USER_DB_URL" = "jdbc:postgresql://your-rds-endpoint:5432/ijaa_users?sslmode=require" ]; then
    echo "❌ Error: Database URLs not configured in .env file"
    exit 1
fi

echo "✅ Environment configuration validated"

case "$1" in
  "build")
    echo "🔨 Building all services..."
    docker-compose -f docker-compose-aws.yml build --no-cache
    echo "✅ Build complete!"
    ;;
  "start")
    echo "🚀 Starting all services..."
    docker-compose -f docker-compose-aws.yml up -d
    echo "✅ Services started! Check status with: ./deploy-aws.sh status"
    ;;
  "stop")
    echo "🛑 Stopping all services..."
    docker-compose -f docker-compose-aws.yml down
    echo "✅ Services stopped!"
    ;;
  "restart")
    echo "🔄 Restarting all services..."
    docker-compose -f docker-compose-aws.yml down
    docker-compose -f docker-compose-aws.yml up -d
    echo "✅ Services restarted!"
    ;;
  "status")
    echo "📊 Service Status:"
    docker-compose -f docker-compose-aws.yml ps
    echo ""
    echo "🌐 Service URLs:"
    echo "  Gateway:      http://$EC2_PUBLIC_IP:8080"
    echo "  Discovery:    http://$EC2_PUBLIC_IP:8761"
    echo "  Config:       http://$EC2_PUBLIC_IP:8888"
    echo "  User:         http://$EC2_PUBLIC_IP:8081"
    echo "  Event:        http://$EC2_PUBLIC_IP:8082"
    echo "  File:         http://$EC2_PUBLIC_IP:8083"
    echo ""
    echo "🔍 Health Check URLs:"
    echo "  Gateway:      http://$EC2_PUBLIC_IP:8080/actuator/health"
    echo "  Discovery:    http://$EC2_PUBLIC_IP:8761/actuator/health"
    echo "  Config:       http://$EC2_PUBLIC_IP:8888/actuator/health"
    echo "  User:         http://$EC2_PUBLIC_IP:8081/actuator/health"
    echo "  Event:        http://$EC2_PUBLIC_IP:8082/actuator/health"
    echo "  File:         http://$EC2_PUBLIC_IP:8083/actuator/health"
    ;;
  "logs")
    if [ -z "$2" ]; then
      echo "📝 Showing logs for all services..."
      docker-compose -f docker-compose-aws.yml logs -f
    else
      echo "📝 Showing logs for $2..."
      docker-compose -f docker-compose-aws.yml logs -f $2
    fi
    ;;
  "health")
    echo "🏥 Checking service health..."
    echo ""
    
    services=("discovery-service:8761" "config-service:8888" "user-service:8081" "event-service:8082" "file-service:8083" "gateway-service:8080")
    
    for service in "${services[@]}"; do
      IFS=':' read -r name port <<< "$service"
      echo "Checking $name..."
      if curl -s "http://localhost:$port/actuator/health" > /dev/null; then
        echo "  ✅ $name is healthy"
      else
        echo "  ❌ $name is not responding"
      fi
    done
    ;;
  "clean")
    echo "🧹 Cleaning up Docker resources..."
    docker-compose -f docker-compose-aws.yml down -v
    docker system prune -f
    echo "✅ Cleanup complete!"
    ;;
  "setup")
    echo "⚙️  Setting up AWS deployment..."
    echo ""
    echo "1. Create .env file from env.example:"
    echo "   cp env.example .env"
    echo ""
    echo "2. Edit .env file with your AWS configuration:"
    echo "   - Set JWT_SECRET to a strong secret"
    echo "   - Set EC2_PUBLIC_IP to your EC2 public IP"
    echo "   - Configure database URLs and credentials"
    echo ""
    echo "3. Build and start services:"
    echo "   ./deploy-aws.sh build"
    echo "   ./deploy-aws.sh start"
    echo ""
    echo "4. Check service status:"
    echo "   ./deploy-aws.sh status"
    echo ""
    echo "5. Monitor logs:"
    echo "   ./deploy-aws.sh logs"
    ;;
  *)
    echo "Usage: $0 {build|start|stop|restart|status|logs|health|clean|setup}"
    echo ""
    echo "Commands:"
    echo "  build      - Build all services"
    echo "  start      - Start all services"
    echo "  stop       - Stop all services"
    echo "  restart    - Restart all services"
    echo "  status     - Show service status and URLs"
    echo "  logs       - Show logs (all or specific service)"
    echo "  health     - Check health of all services"
    echo "  clean      - Clean up Docker resources"
    echo "  setup      - Show setup instructions"
    echo ""
    echo "Examples:"
    echo "  ./deploy-aws.sh setup"
    echo "  ./deploy-aws.sh build"
    echo "  ./deploy-aws.sh start"
    echo "  ./deploy-aws.sh status"
    echo "  ./deploy-aws.sh logs user-service"
    ;;
esac
