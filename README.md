# 🚗 Carpooling Platform

A comprehensive multi-module car-pooling platform featuring mobile clients (Android & iOS), a Java backend (Struts2-based web API), and a web-based manager/admin UI.

---

## 📱 Screenshots

<p align="center">
  <img width="1200" height="800" alt="Carpooling Platform" src="https://github.com/user-attachments/assets/2b5b4d1a-1074-47a4-b4f1-f18ebac78514" />
</p>

---

## ✨ Key Features

### For Drivers
| Feature | Description |
|---------|-------------|
| 🚗 **Ride Creation** | Post upcoming trips with start/end points, date, time, and available seats |
| 🗺️ **Route Optimization** | Integration with mapping services to visualize travel paths |
| 👥 **Passenger Management** | View and manage booking requests from travelers |

### For Passengers
| Feature | Description |
|---------|-------------|
| 🔍 **Smart Search** | Filter rides by destination, date, and price |
| ⚡ **Instant Booking** | Secure a seat in just a few clicks |
| 📊 **Ride History** | Track past and upcoming journeys |

### Technical Highlights
- 🔐 **User Authentication** - Secure login and profile management
- 🔔 **Real-time Updates** - Instant notifications on ride status changes
- 📱 **Responsive Design** - Optimized for desktop and mobile browsers

---

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Backend** | Java, Struts2, MyBatis, Servlet |
| **Web Server** | Apache Tomcat |
| **Database** | MySQL |
| **Android** | Native Java/Kotlin |
| **iOS** | Native Swift/Objective-C |
| **Maps** | Baidu Maps API |
| **Push Notifications** | Baidu Push |
| **Social SDK** | UMeng (Tencent/WeChat SSO) |
| **Image Loading** | SDWebImage (iOS) |
| **Networking** | AFNetworking (iOS) |

---

## 📂 Project Structure

```
dmy-carpooling-platform/
├── android/                  # Android mobile applications
│   ├── CarPoolingApp/       # Primary carpooling app
│   └── MainApp/             # Companion application
├── ios/                     # iOS mobile applications
│   ├── CarPoolingApp/       # BJPinChe - Carpooling app
│   └── MainApp/             # BJMainApp - Companion app
├── service/                 # Backend REST API (Struts2)
├── manager/                 # Admin/Manager Web UI (JSP)
├── database/                # MySQL schema and seed data
│   └── pinche.sql          # Database schema
├── README.md               # This file
└── LICENSE                 # MIT License
```

---

## 🗄️ Database Schema

The database schema is located in `database/pinche.sql`. It includes:

- User authentication and profiles
- Ride/Order management
- Driver and passenger data
- Payment and transaction records
- Evaluation and rating system
- City and location data

### Quick Database Setup

```bash
# Create database and import schema
mysql -u root -p pinche < database/pinche.sql
```

---

## 🚀 Quick Start

### Prerequisites

| Requirement | Version | Notes |
|-------------|---------|-------|
| Java JDK | 8+ | Compatible with project |
| Apache Tomcat | 7/8+ | Servlet container |
| MySQL | 5.6+ | Database |
| Android Studio | Latest | Android development |
| Xcode | Latest | iOS development |
| Eclipse/IntelliJ | Latest | Java web development |

### Step 1: Database Setup

```bash
# Create MySQL database
mysql -u root -p -e "CREATE DATABASE pinche;"

# Import schema and seed data
mysql -u root -p pinche < database/pinche.sql
```

### Step 2: Configure Database Connection

Update JDBC properties in:
- `manager/src/jdbc.properties` (Manager webapp)
- `service/src/jdbc.properties` (Service API)

### Step 3: Backend Service Setup

```bash
# Import service/ as Dynamic Web Project in Eclipse/IntelliJ
# Configure Tomcat server
# Deploy to http://localhost:8080/service/
```

### Step 4: Manager UI Setup

```bash
# Import manager/ as Dynamic Web Project
# Deploy to http://localhost:8080/manager/
```

### Step 5: Android Development

```bash
# Open in Android Studio
# File → Open → android/CarPoolingApp
# Update AndroidManifest.xml with API endpoints
# Build and run on emulator/device
```

### Step 6: iOS Development

```bash
# Open Xcode project
# open ios/CarPoolingApp/BJPinChe.xcodeproj
# Configure Info.plist with API endpoints
# Run on simulator/device
```

---

## ⚙️ Configuration

### Database Configuration

| File | Purpose |
|------|---------|
| `manager/src/jdbc.properties` | Manager webapp DB connection |
| `service/src/jdbc.properties` | Service API DB connection |

### API Configuration

| File | Purpose |
|------|---------|
| `service/src/com/webapi/common/ApiGlobal.java` | Global API constants, push settings, endpoint URLs |

### Mobile App Configuration

| Platform | Files |
|----------|-------|
| Android | `android/CarPoolingApp/AndroidManifest.xml`, `android/MainApp/AndroidManifest.xml` |
| iOS | `ios/CarPoolingApp/BJPinChe-Info.plist`, `ios/MainApp/BJMainApp-Info.plist` |

### Web Configuration

| File | Purpose |
|------|---------|
| `manager/WebContent/WEB-INF/web.xml` | Filters, servlets, welcome pages |
| `manager/src/struts.xml` | Struts action mappings |
| `service/src/struts.xml` | Service API action mappings |

---

## 🔧 Core Components

### Ride Matching Algorithm

The backend implements a **push-based notification system** with temporary-grab functionality:

- **Order Types**: Once (one-time), Long-distance, On/off-duty
- **Concurrency Control**: Synchronized locking (in-memory lists) to prevent double-acceptance
- **Notifications**: Baidu Push integration for real-time driver notifications

**Key Files:**
- `service/src/com/webapi/common/ApiGlobal.java`
- `service/src/com/webapi/structure/SVCOrder*`

### User Authentication

| Type | Method | Location |
|------|--------|----------|
| Manager UI | Session-based (AuthFilter) | `manager/WebContent/WEB-INF/web.xml` |
| Mobile API | Token/Session-based | `service/src/com/webapi/structure/SVCUser*` |
| Social Login | UMeng OAuth | Android/iOS client apps |

### Booking Flow

```
1. Rider creates order (Mobile App)
        ↓
2. Backend persists to DB & notifies drivers (Baidu Push)
        ↓
3. Driver "grabs" order (Temporary Grab endpoint)
        ↓
4. Backend locks order (Synchronized)
        ↓
5. Order accepted → Payment & Evaluation
```

---

## 📋 Features Matrix

| Feature | Module | Location | Status |
|---------|--------|----------|--------|
| Ride Creation | Android/iOS/Service | `service/src/com/webapi/structure/SVCOrder*` | ✅ |
| Ride Matching | Service | `ApiGlobal.java`, `SVCOrderTempGrab` | ✅ |
| Push Notifications | Service/Mobile | `ApiGlobal.java` (Baidu Push) | ✅ |
| User Authentication | Manager/Service | `AuthFilter`, `SVCUser*` | ✅ |
| Admin Dashboard | Manager | `manager/src/`, JSPs | ✅ |
| Payments | Android/Service | `WapPayActivity` | ✅ |
| Geolocation/Maps | Android/iOS | Baidu Maps API | ✅ |
| Social Login | Android/iOS | UMeng SDK | ✅ |
| Driver Profiles | Manager/Service | `SVCUser*` | ✅ |
| Evaluation System | Service | `SVCEvaluationCS` | ✅ |
| CSV Export | Manager | `Common.java` | ✅ |

---

## 🐛 Troubleshooting

### Common Issues

| Issue | Solution |
|-------|----------|
| Android Studio import fails | Create new Gradle project and copy source files |
| iOS framework missing | Check `Library/` for embedded SDKs |
| Database connection error | Verify JDBC credentials in properties files |
| Push notifications not working | Update Baidu Push API keys in `ApiGlobal.java` |
| Login failures | Check AuthFilter configuration in `web.xml` |

### Logs Location

- **Backend**: Tomcat logs directory
- **Manager**: `manager/src/log4j.properties`
- **Service**: log4j in `service/src`

---

## 🗺️ Roadmap

### High Priority
- [ ] GPS Integration & Real-time Tracking
- [ ] Enhanced Matching Engine (ETA-based scoring)
- [ ] WebSocket for live location updates

### Medium Priority
- [ ] Distributed Locking (Redis/DB)
- [ ] Token-based Authentication (JWT)
- [ ] Payment Gateway Integration

### Low Priority
- [ ] CI/CD Pipeline
- [ ] Automated Tests
- [ ] OpenAPI/Swagger Documentation

---

## 🤝 Contributing

1. **Fork** the repository
2. Create a **feature branch** (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. Open a **Pull Request**

Please include:
- Module being modified
- Steps to reproduce
- Expected vs actual behavior

---

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 📞 Support

For issues and questions:
- Open an issue on GitHub
- Check existing documentation
- Review troubleshooting section

---

**Built with ❤️ for the carpooling community**

