#!/bin/bash

# IJAA Microservices Docker Development Script

echo "🐳 IJAA Microservices Docker Development"
echo "========================================"

case "$1" in
  "start")
    echo "🚀 Starting all services..."
    docker-compose up -d
    echo "✅ Services started! Check status with: ./docker-dev.sh status"
    ;;
  "stop")
    echo "🛑 Stopping all services..."
    docker-compose down
    echo "✅ Services stopped!"
    ;;
  "restart")
    echo "🔄 Restarting all services..."
    docker-compose down
    docker-compose up -d
    echo "✅ Services restarted!"
    ;;
  "status")
    echo "📊 Service Status:"
    docker-compose ps
    echo ""
    echo "🌐 Service URLs:"
    echo "  Gateway:      http://localhost:8080"
    echo "  Discovery:    http://localhost:8761"
    echo "  Config:       http://localhost:8888"
    echo "  User:         http://localhost:8081"
    echo "  Event:        http://localhost:8082"
    echo "  File:         http://localhost:8083"
    echo ""
    echo "🗄️  Database URLs:"
    echo "  User DB:      localhost:5432"
    echo "  Event DB:     localhost:5433"
    echo "  File DB:      localhost:5434"
    ;;
  "logs")
    if [ -z "$2" ]; then
      echo "📝 Showing logs for all services..."
      docker-compose logs -f
    else
      echo "📝 Showing logs for $2..."
      docker-compose logs -f $2
    fi
    ;;
  "build")
    echo "🔨 Building all services..."
    docker-compose build --no-cache
    echo "✅ Build complete!"
    ;;
  "clean")
    echo "🧹 Cleaning up Docker resources..."
    docker-compose down -v
    docker system prune -f
    echo "✅ Cleanup complete!"
    ;;
  "db-reset")
    echo "🗄️  Resetting databases..."
    docker-compose down -v
    docker-compose up -d user-db event-db file-db
    echo "⏳ Waiting for databases to be ready..."
    sleep 10
    docker-compose up -d
    echo "✅ Databases reset and services restarted!"
    ;;
  *)
    echo "Usage: $0 {start|stop|restart|status|logs|build|clean|db-reset}"
    echo ""
    echo "Commands:"
    echo "  start      - Start all services"
    echo "  stop       - Stop all services"
    echo "  restart    - Restart all services"
    echo "  status     - Show service status and URLs"
    echo "  logs       - Show logs (all or specific service)"
    echo "  build      - Build all services"
    echo "  clean      - Clean up Docker resources"
    echo "  db-reset   - Reset databases and restart services"
    echo ""
    echo "Examples:"
    echo "  ./docker-dev.sh start"
    echo "  ./docker-dev.sh logs user-service"
    echo "  ./docker-dev.sh status"
    ;;
esac
