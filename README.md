# Carpooling Platform

A multi-module car‑pooling platform repository containing mobile clients (Android & iOS), a Java backend (Struts2-based web API), and a web-based manager/admin UI.

This README gives a concise orientation, quick-start steps to run the project locally, configuration notes, and troubleshooting tips.

---

## Screenshots

<img width="1200" height="800" alt="Carpooling" src="https://github.com/user-attachments/assets/2b5b4d1a-1074-47a4-b4f1-f18ebac78514" />

---

## Highlights / Key Technologies

- Backend: Java web services (Struts2, servlet-based), MyBatis-style mappers, many web actions under `service/src` and `manager/src`.
- Web admin: Traditional Java web app (JSP) in `manager/` (deployable to Tomcat).
- Mobile clients:
  - Android modules: `android/CarPoolingApp` (primary) and `android/MainApp` (companion).
  - iOS projects: `ios/CarPoolingApp/BJPinChe` and `ios/MainApp/BJMainApp` (Xcode projects).
- Database: MySQL schema and seed file — `database/pinche.sql`.
- Integrations: Baidu Maps & Push, UMeng social SDKs, AFNetworking/SDWebImage on iOS, many 3rd-party libs.

---

## Features

Below is a concise features table that maps the platform's main capabilities to the modules and locations in the repository where their implementations live. This provides a quick reference for contributors who want to find and work on a specific capability.

### Key Features

#### 🚗 For Drivers

- Ride Creation: Easily post upcoming trips with start/end points, date, time, and available seats.
- Route Optimization: (If applicable) Integration with mapping services to visualize the travel path.
- Passenger Management: View and manage booking requests from interested travelers.

#### 🎒 For Passengers
- Smart Search: Filter rides by destination, date, and price to find the perfect match.
- Instant Booking: Secure a seat in just a few clicks with real-time availability updates.
- Ride History: Track past and upcoming journeys in a centralized dashboard.

#### 🛠 Technical Highlights
- User Authentication: Secure login and profile management for verified community members.
- Real-time Updates: Stay informed with instant notifications on ride status changes.
- Responsive Design: Fully optimized for both desktop and mobile browsers.

### Features table

| Feature | Module(s) | Location (example files/paths) | Notes |
|---|---:|---|---|
| Ride matching / dispatch | service | `service/src/com/webapi/common/ApiGlobal.java`, `service/src/com/webapi/structure/` | Push-based notification → temporary-grab → server-side locking (in-memory lists). Consider migrating to a scoring model & distributed locks. |
| Booking flow (order creation → acceptance → payment) | android / ios / service / database | Mobile clients in `android/*`, `ios/*`; backend `service/src/*` (SVCOrder*); schema `database/pinche.sql` | Typical lifecycle: create order (mobile) → persist DB → notify drivers → grab/accept → payment & evaluation. |
| Push notifications | service / mobile | `service/src/com/webapi/common/ApiGlobal.java` (Baidu Push), mobile push handlers in Android/iOS projects | Uses Baidu ChannelClient for push. Keys and endpoints are in ApiGlobal and mobile configs. |
| Authentication (admin & API) | manager / service / mobile | `manager/WebContent/WEB-INF/web.xml` (AuthFilter), `manager/src/*` and `service/src/*` (SVCUser, login actions) | Manager uses session-based auth; mobile uses API login actions. Consider token-based auth for mobile. |
| Admin / manager UI | manager | `manager/` (JSPs, `manager/src/struts.xml`, `manager/WebContent/WEB-INF/web.xml`) | Struts-based, session-filtered admin UI for data and operations. |
| Payments | android / service | `android/*` manifests reference `WapPayActivity`; server-side payment hooks in `service/src` | WAP/web-based payment activity referenced; extend with modern gateways and secure callbacks. |
| Geolocation / Maps | android / ios / service | Android manifests `android/*/AndroidManifest.xml`, iOS Info.plist, Baidu Map integrations in mobile projects | Uses Baidu Maps APIs; GPS permissions present in Android manifests. |
| Social login / sharing | android / ios | UMeng configs in AndroidManifest and iOS libs; `android/MainApp` manifests | UMeng & Tencent/WeChat SSO configured; update app keys for your accounts. |
| Driver & user profiles | manager / service / mobile | `service/src/com/.../SVCUser*`, `manager/src/`, `android/*` | CRUD, verification, and profile images handled by respective modules. |
| Evaluation & settlement | service / manager | `service/src/com/webapi/structure/SVCEvaluationCS`, settlement-related classes | End-of-ride evaluation and settlement logic exist server-side; see structures in service. |
| Export / Reporting | manager | `manager/src/com/pinche/common/Common.java` (CSV export helpers), manager JSPs | Utility helpers for generating CSV exports and reports. |
| Logging & diagnostics | service / manager / Tomcat | `manager/src/log4j.properties`, Tomcat logs, log4j usages in `service/src` | Check Tomcat and log4j config files for runtime troubleshooting. |
| Schema & seed data | database | `database/pinche.sql` | Full schema and seed data for local DB initialisation. |

---

## Core Components — Analysis

This section analyzes three core areas of the project based on the repository contents and highlights where the relevant code lives.

### Ride Matching Algorithm

What the code reveals
- The backend contains matching/dispatch logic and concurrency controls:
  - `service/src/com/webapi/common/ApiGlobal.java` includes arrays such as `arrOnceOrderIDs` and `arrLongOrderIDs` and synchronized lock methods (e.g., `lockOnceOrderAcceptance`) used to prevent double-acceptance of the same order.
  - There are domain classes and action handlers for different order types: `SVCOrderTempGrab`, `SVCOrderTempDetails`, `SVCOrderOnOffDutyGrab`, `SVCOrderLongDistanceDetails`, etc., indicating the system differentiates between short (one-time), long-distance, and on/off-duty orders.
  - `ApiGlobal` also contains code that integrates with Baidu Push (imports and usage of `BaiduChannelClient` and related classes), which is used to notify drivers of available orders.

How matching appears to work (inferred)
- Orders are created by users (mobile apps → backend DB). The service notifies candidate drivers via push notifications (Baidu Push).
- Driver-side "grab" operations are handled by temporary-grab endpoints. The backend uses synchronized locks / in-memory lists to avoid multiple drivers accepting the same order simultaneously.
- Matching is likely driven by a combination of order type (once, long-distance, on/off duty), city/region (see `SVCCity`), timing, and basic proximity heuristics (proximity logic is implied by presence of location/route fields in order structures and the mobile clients’ location permissions).
- The current approach looks like a pragmatic first-step dispatch: notify many candidates, first valid grab wins, with server-side locking to guarantee single acceptance.

Suggested improvements
- Introduce a more formal proximity / scoring function (distance, ETA, driver rating, current load).
- Consider persistent distributed locks (DB row lock or Redis lock) to make locking robust across multiple backend instances.
- Add metrics and tracing for matching latency and fairness.

Files referenced
- `service/src/com/webapi/common/ApiGlobal.java`
- `service/src/com/webapi/structure/*` (SVCOrder*, SVCCity)
- Backend push integration references (Baidu push classes imported in ApiGlobal)

---

### User Authentication

What the code reveals
- Manager / admin UI:
  - `manager/WebContent/WEB-INF/web.xml` registers an `AuthFilter` with a `sessionKey` of `user` and redirects to `/bk` on unauthorized access — indicating session-based authentication for the web UI.
  - Login handling and actions are defined (e.g., `com.pinche.authority.manager.action.LoginAction` and related Struts2 actions in `manager/src/struts.xml`).
- Mobile and API:
  - Service side contains `SVCUser`, `SVCUserLogin` structures and login actions under `service/src` (search for login-related actions). Mobile apps call backend endpoints to authenticate users.
  - Android and iOS apps include social SDKs (UMeng), so social login options are integrated or supported in the clients.
- Configuration:
  - Database connection and credential information are in `manager/src/jdbc.properties` and `service/src/jdbc.properties`.

Security posture (inferred)
- Web UI uses server-side session (AuthFilter) — suitable for browser-based admin UI.
- API-based mobile authentication is present but implementation details (tokens, session cookies, expiry) should be verified in `service/src` login actions.
- Social SDKs (UMeng) are included in client projects, indicating possible OAuth-style flows on the mobile side.

Suggested improvements
- If not already used, employ token-based authentication (short-lived tokens / refresh tokens) for mobile APIs to avoid sharing session cookies.
- Use HTTPS for all API endpoints and ensure API keys/credentials are never checked into source (use environment config).
- Add password-strength rules, rate-limiting, and login attempt monitoring.

Files referenced
- `manager/WebContent/WEB-INF/web.xml` (AuthFilter)
- `manager/src/struts.xml` (LoginAction mappings)
- `service/src/*` (SVCUser, SVCUserLogin, login-related actions)
- Android/iOS client projects (UMeng integration, manifests)

---

### Booking Flow

Overview (from code & manifests)
- Mobile user creates a ride request via Android/iOS apps; forms and order submission logic live in mobile project source.
- Backend persists orders to MySQL (`database/pinche.sql` contains schema) and triggers notifications to potential drivers.
- Drivers receive push via Baidu Push and can "grab" the order; the backend has temporary-grab handlers and concurrency controls (see `SVCOrderTempGrab` and `ApiGlobal` locking).
- After a driver accepts, order status is updated and post-acceptance flows occur (ride execution, evaluation, payment). Android includes a `WapPayActivity` referenced in `android/MainApp/AndroidManifest.xml`, indicating WAP-based or web-based payment flows are supported.

Concrete steps (typical booking flow inferred)
1. Rider submits order from mobile client (specifying pickup, destination, time, seat count).
2. Backend creates order record in DB and determines candidate drivers.
3. Backend notifies candidate drivers (Baidu Push via `ApiGlobal`).
4. Drivers request to grab the order (temporary grab endpoint(s)).
5. Backend uses synchronized locking (e.g., `lockOnceOrderAcceptance`) to prevent race conditions; first successful grab sets order to "accepted".
6. Once accepted, the order status is updated and payment flow is initiated (client-side `WapPayActivity` or other payment methods).
7. After trip completion, evaluation and settlement flows are executed (see `SVCEvaluationCS`, settlement-related classes).

Suggested improvements
- Expose clearer API documentation / sample curl requests for order creation and acceptance endpoints.
- Add server-side timeouts and stale-grab cleanup for abandoned temporary grabs.
- Add end-to-end testing for the full booking lifecycle.

Files referenced
- `service/src/com/webapi/common/ApiGlobal.java` (locking, push)
- `service/src/com/webapi/structure/*` (SVCOrder*, SVCUser, evaluation/settlement structures)
- `android/*` manifests (WapPayActivity), iOS projects (payment & libraries)
- `database/pinche.sql` (schema)

---

## Tech Stack

Below are the main technologies used in the project. Icons are rendered with shields.io badges.

<!-- Core languages & platforms -->
<img src="https://img.shields.io/badge/Java-ED8B00?logo=java&logoColor=white" alt="Java"> <img src="https://img.shields.io/badge/Struts2-6DB33F?logo=struts&logoColor=white" alt="Struts2"> <img src="https://img.shields.io/badge/Tomcat-F8DC75?logo=apache-tomcat&logoColor=black" alt="Tomcat">

<!-- Databases & CI -->
<img src="https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white" alt="MySQL"> <img src="https://img.shields.io/badge/Git-000000?logo=git&logoColor=white" alt="Git">

<!-- Mobile -->
<img src="https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white" alt="Android"> <img src="https://img.shields.io/badge/iOS-000000?logo=apple&logoColor=white" alt="iOS">

<!-- Integrations -->
<img src="https://img.shields.io/badge/Baidu%20Maps-2E6DA4?logo=baidu&logoColor=white" alt="Baidu Maps"> <img src="https://img.shields.io/badge/UMeng-F57C00?logo=umeng&logoColor=white" alt="UMeng">

Short notes:
- Java + Struts2 power the backend APIs and manager UI (deploy to Tomcat).
- MySQL stores schema/data (see `database/pinche.sql`).
- Android and iOS clients are native. Android manifests include many necessary permissions (location, network, storage).
- Baidu Push/Maps and UMeng social integrations are present in the codebase and client libraries.

---

## Repository layout (short)

- android/ — Android applications (CarPoolingApp, MainApp)
- ios/ — iOS apps (BJPinChe, BJMainApp)
- service/ — Backend web API (Struts2)
- manager/ — Admin/manager web UI (JSP)
- database/ — SQL schema: `pinche.sql`
- README.md — this file
- LICENSE — MIT

---

## Quick Start (Local Development)

These are high‑level steps to get a working local environment. Because this repository contains legacy project layouts, the easiest approach is to import and run in IDEs (Eclipse / Android Studio / Xcode) and a local Tomcat for Java webapps.

### 1) Prerequisites

- Java JDK 8 (or JDK compatible with project)
- Apache Tomcat 7/8 (or any compatible servlet container)
- MySQL 5.6+ (or compatible)
- Eclipse (for webapp import) OR IntelliJ IDEA / STS
- Android Studio (recommended) and Android SDK (match target/min SDK from manifests)
- Xcode (for iOS projects)
- Git client

### 2) Database

1. Create a MySQL database, e.g. `pinche`.
2. Import schema/data:
   - From repo root:
     - mysql -u root -p pinche < database/pinche.sql
3. Update DB connection properties:
   - `manager/src/jdbc.properties` (manager webapp)
   - `service/src/jdbc.properties` (service/webapi)
   - If you use a different file or environment, search for `jdbc.properties` in the repo and update accordingly.

### 3) Backend Service (service/)

1. Open `service/` in your Java IDE as a Dynamic Web Project (or import as existing Java web project).
2. Ensure `WEB-INF/web.xml` and `src` configuration are present (they are).
3. Edit database and environment configuration files if necessary (`service/src/jdbc.properties`, `service/src/com/webapi/common/ApiGlobal.java`).
   - `ApiGlobal.java` contains global API constants and some endpoint configuration; update host or base URLs as needed.
4. Build and deploy to Tomcat (export WAR or run from IDE).
5. By default, the service will be available at:
   - http://localhost:8080/service/  (or the context path you deployed)
6. Test a simple endpoint (depends on deployed mappings). Check `service/src/struts.xml` and `service/src/com/webapi` packages for action names and routes.

### 4) Manager / Admin Web UI (manager/)

1. Open `manager/` in your Java IDE as a Dynamic Web Project.
2. Configure DB connection in `manager/src/jdbc.properties`.
3. Deploy `manager` app to Tomcat (context path `/manager` is common).
4. Visit manager home (example): http://localhost:8080/manager/ (adjust context path as set)
5. The project uses Struts and maps many actions in `manager/src/struts.xml`.

### 5) Android (android/CarPoolingApp and android/MainApp)

1. In Android Studio:
   - File → New → Import Project...
   - Select `android/CarPoolingApp` (or `android/MainApp`) and import.
   - If the project is an older ADT/Eclipse project, Android Studio will attempt to convert; you may need to create a new Gradle project and add sources if conversion fails.
2. Check and update:
   - `AndroidManifest.xml` (package name, permissions; see `android/CarPoolingApp/AndroidManifest.xml` and `android/MainApp/AndroidManifest.xml`).
   - API base URL in code/config (search for `SERVER_URL`, `ApiGlobal`, or references to your service host).
3. Build & Run on an emulator / physical device. Make sure to enable required SDK packages (Google Play services if needed).

### 6) iOS (ios/CarPoolingApp/BJPinChe and ios/MainApp/BJMainApp)

1. Open the Xcode project/workspace:
   - `ios/CarPoolingApp/BJPinChe.xcodeproj` or workspace.
2. Check Info.plist and code for API host values. The iOS apps include many frameworks (AFNetworking, SDWebImage, UMeng, BaiduMap). You may need to install CocoaPods or add frameworks to the workspace if some frameworks are external — however many are checked in under `Library/`.
3. Select a target device or simulator and Run (⌘R).

---

## Configuration Notes / Where to edit

- Database:
  - `manager/src/jdbc.properties`
  - `service/src/jdbc.properties`
- Backend API/global:
  - `service/src/com/webapi/common/ApiGlobal.java` — update base URLs, keys, push settings.
- Android manifests:
  - `android/CarPoolingApp/AndroidManifest.xml`
  - `android/MainApp/AndroidManifest.xml`
- iOS:
  - Info.plist files under each Xcode project (`BJPinChe-Info.plist`, `BJMainApp-Info.plist`) and `Library/` for embedded SDKs.
- Struts/web mappings:
  - `manager/src/struts.xml`
  - `service/src/struts.xml`
- Web app descriptor:
  - `manager/WebContent/WEB-INF/web.xml` — contains filters (AuthFilter, Struts filter) and welcome page.

---

## Troubleshooting & Tips

- Legacy Android project: if Android Studio can't import cleanly, consider:
  - Create a new Android Studio project and copy source files (src, res, AndroidManifest).
  - Or use an older ADT workspace / Eclipse with Android SDK tools if conversion is difficult.
- Missing native libs or frameworks on iOS:
  - Many frameworks are vendor-supplied in `Library/`. Check Build Settings for library/linker flags.
- Logging & errors:
  - Backend logs configured via log4j (see `manager/src/log4j.properties` or `service` equivalents). Check Tomcat logs if actions fail.
- When you change DB credentials, restart the webapps to pick up changes.
- If push or social SDKs fail, you may need to register your own API keys and update configuration constants in code (ApiGlobal and related partner-config files under Android/iOS modules).

---

## Future Roadmap

Planned improvements and features (prioritized):

- GPS Integration & Real-time Tracking (High)
  - Add a persistent GPS/telemetry pipeline so drivers and riders can share real-time location.
  - Use WebSocket (or MQTT) for live location updates and improve order matching by ETA rather than static proximity.
  - Update mobile apps to stream location with configurable frequency and server-side storage for trip replay.
- Enhanced Matching Engine (High)
  - Move from first-come-first-served to a scoring model combining ETA, driver rating, acceptance rate, and current passenger capacity.
  - Consider multi-criteria optimization for batching nearby ride requests.
- Robust Distributed Locking (Medium)
  - Replace in-memory arrays used for locking with DB row locks or Redis-based distributed locks for multi-instance backend reliability.
- Stronger Authentication & Authorization (Medium)
  - Migrate mobile APIs to token-based auth (JWT or similar), add refresh tokens and enforce proper expiry.
  - Add 2FA for manager accounts.
- Payment Modernization (Medium)
  - Expand payment methods and integrate server-side payment gateways with secure callbacks and reconciliation.
- CI/CD, Automated Tests & Monitoring (Low → High overtime)
  - Add unit/integration tests for booking and matching flows.
  - Add CI pipelines and runtime monitoring (metrics for matching latency, acceptance rates).
- Documentation & API Spec (Low)
  - Publish an OpenAPI/Swagger spec for backend APIs to make integration and testing easier.

---

## Contributing

1. Fork the repository and create a feature branch.
2. Make small, focused commits.
3. Open a pull request with a clear description of the change and instructions to test (if needed).

Please include: which module, steps to reproduce, and expected vs actual behavior.

---

## If you want, I can:

- Add example configuration templates for jdbc.properties (masked credentials).
- Create quick starter scripts (Ant/Gradle/Make) to build/deploy common modules.
- Add labels, issue templates, or a CONTRIBUTING.md.

---

## License

This project is licensed under the MIT License — see [LICENSE](LICENSE).

---
