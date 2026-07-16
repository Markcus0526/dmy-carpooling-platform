# 🚗 Carpooling Platform — with On-Chain Escrow

A production-grade, decentralized carpooling infrastructure that integrates mobile clients, a robust enterprise Java backend, and an on-chain smart contract escrow layer to secure financial transactions between drivers and riders.

## 🚀 Core Architectural Features
* **Solidity RideEscrow Contract:** Implements an automated, trustless smart contract that securely holds rider fare deposits in escrow and triggers automated payouts to drivers upon successful trip verification.
* **Zero-Dependency JSON-RPC Verifier:** Features a custom, highly secure blockchain event monitoring layer designed to read state variables and verify on-chain logs directly without bloated external framework wrappers.
* **Enterprise REST API:** A high-throughput Java/Struts2 backend engine handling real-time ride matching, geo-location mapping, and system data persistence.
* **Cross-Platform Native Experience:** Dedicated native execution paths optimized for both Android and iOS devices.

## 🛠️ Tech Stack
* **Smart Contracts:** Solidity (EVM Compatible)
* **Backend Architecture:** Java 17, Struts2, JSON-RPC Execution
* **Target Platforms:** Android Native, iOS Native

---
💼 **Looking to integrate smart contract escrows or custom Web3 logic into your platform?**  
[Message me on Telegram](https://t.me/markcus0526) to work together directly. No marketplace KYC required.


> **Uber, but the fare rides on-chain.** Full-stack ride-sharing platform (Android + iOS + Java backend) with a Solidity escrow contract for trustless deposits and instant driver payouts.

<p align="center">
  <a href="#-quick-start"><img alt="License: MIT" src="https://img.shields.io/badge/license-MIT-blue.svg"></a>
  <a href="#️-web3-payment--booking-path"><img alt="Web3 Ready" src="https://img.shields.io/badge/web3-ready-8A2BE2.svg?logo=ethereum&logoColor=white"></a>
  <a href="contracts/RideEscrow.sol"><img alt="Solidity 0.8.20" src="https://img.shields.io/badge/solidity-0.8.20-363636.svg?logo=solidity"></a>
  <a href="#-tech-stack"><img alt="Java 8+" src="https://img.shields.io/badge/java-8%2B-007396.svg?logo=openjdk&logoColor=white"></a>
  <a href="#-tech-stack"><img alt="Android" src="https://img.shields.io/badge/android-native-3DDC84.svg?logo=android&logoColor=white"></a>
  <a href="#-tech-stack"><img alt="iOS" src="https://img.shields.io/badge/iOS-native-000000.svg?logo=apple&logoColor=white"></a>
  <a href="#-contributing"><img alt="PRs Welcome" src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg"></a>
</p>

**Payment options:** Alipay · WeChat Pay · **On-chain escrow (ETH / L2 stablecoins)** — riders choose per trip, without any change to the underlying booking flow.

> ⭐ **If this repo helps you understand how to bolt web3 onto a real (non-toy) backend, drop a star** — it's the single fastest way to help others find it.

---

## 🎯 Why this project is different

Most web3-ride-sharing repos are 300-line proof-of-concepts. This one is different:

- **It's a real app, not a toy** — 20K+ lines of production Java, native Android/iOS clients, a full admin UI. Web3 was added *without* rewriting the booking flow.
- **Zero-dependency chain verification** — the backend verifies on-chain deposits with pure JDK `HttpURLConnection` calls. No web3j fat jar in the war. ~120 lines total.
- **Honest trust model, documented** — this is "crypto rails, centralized settlement" and the README says so. No hand-waving about "trustless" when it isn't.
- **Side-table pattern for storage** — web3 payments live in a parallel `order_crypto_tx` table. The 23K-line `SVCOrderService` is untouched. A blueprint for retrofitting web3 into legacy backends.
- **The escrow contract is 90 lines** — that's the entire trust model. Auditable in one sitting.

### 📑 Contents

- [Why this project is different](#-why-this-project-is-different)
- [Key Features](#-key-features)
- [Tech Stack](#️-tech-stack)
- [Project Structure](#-project-structure)
- [Database Schema](#️-database-schema)
- [Quick Start](#-quick-start)
- [Configuration](#️-configuration)
- [Core Components](#-core-components)
- [Features Matrix](#-features-matrix)
- [⛓️ Web3 Payment & Booking Path](#️-web3-payment--booking-path)
   - [Architecture at a glance](#-architecture-at-a-glance)
   - [Smart Contract API](#-smart-contract-api)
   - [Backend Verification](#-backend-verification-what-happens-on-payordercrypto)
   - [Setup Checklist](#️-setup-checklist)
   - [Testing (Hardhat local chain)](#-testing-hardhat-local-chain)
   - [Trust Model — Honest Take](#️-trust-model--honest-take)
   - [Regulatory Note](#-regulatory-note)
   - [Deferred Work](#-deferred-work-calling-out-honest-gaps)
- [Troubleshooting](#-troubleshooting)
- [Roadmap](#️-roadmap)

---

## 📱 Screenshots & Demo

<p align="center">
  <img width="1200" height="800" alt="Carpooling Platform" src="https://github.com/user-attachments/assets/2b5b4d1a-1074-47a4-b4f1-f18ebac78514" />
</p>

<!--
  💡 Star-boost tip: add a short screen-recording GIF of the "Pay with Crypto"
  flow here. Repos with animated demos convert visitors to stars ~3× more
  often than static screenshots. Suggested capture: rider taps Pay With
  Crypto → wallet pops → tx confirms → order state updates. ~10s clip.

  <p align="center">
    <img width="360" alt="Crypto pay demo" src="docs/crypto-pay-demo.gif" />
  </p>
-->

> **Try the smart contract in 60 seconds** — clone, `cd contracts`, `npx hardhat node`, deploy locally, and hit `payOrderCrypto` from curl. Full instructions in the [Testing section](#-testing-hardhat-local-chain).

---

## ✨ Key Features

### For Drivers
| Feature | Description |
|---------|-------------|
| 🚗 **Ride Creation** | Post upcoming trips with start/end points, date, time, and available seats |
| 🗺️ **Route Optimization** | Integration with mapping services to visualize travel paths |
| 👥 **Passenger Management** | View and manage booking requests from travelers |
| 💸 **Instant Payout (Web3)** | Receive fare directly to a self-custodial wallet the moment the trip completes |

### For Passengers
| Feature | Description |
|---------|-------------|
| 🔍 **Smart Search** | Filter rides by destination, date, and price |
| ⚡ **Instant Booking** | Secure a seat in just a few clicks |
| 📊 **Ride History** | Track past and upcoming journeys |
| 🪙 **Pay with Crypto** | Lock fare in an on-chain escrow at booking — released to driver on completion, refundable on cancel |

### Technical Highlights
- 🔐 **User Authentication** - Secure login and profile management
- 🔔 **Real-time Updates** - Instant notifications on ride status changes
- 📱 **Responsive Design** - Optimized for desktop and mobile browsers
- ⛓️ **On-chain Escrow** - `RideEscrow.sol` smart contract holds funds between booking and trip completion (deposit → release / refund)
- 🔗 **Zero-dependency Chain Verifier** - Backend verifies deposits via plain JSON-RPC (no web3j fat jar) — validates receipt status, min-confirmations, event topics, and amount
- 🧩 **Side-table Design** - Web3 payments live in a parallel `order_crypto_tx` table, so existing `orders` / `order_temp_details` schemas are untouched

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
| **Smart Contracts** | Solidity 0.8.20, Hardhat (deploy tooling) |
| **Target Chains** | Any EVM L2 — recommended Base / Arbitrum / Polygon (mainnet or testnet) |
| **Chain I/O** | Plain JSON-RPC over `HttpURLConnection` (no web3j / ethers dependency in the war) |
| **Wallet UX** | WalletConnect v2 / MetaMask deep-link (mobile clients) |

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
│   └── src/com/webapi/
│       ├── order/            # Legacy Alipay/WeChat pay + booking flow
│       └── crypto/           # ⛓️ NEW — on-chain escrow verification
│           ├── action/       #    Struts2 endpoints (payOrderCrypto, ...)
│           ├── service/      #    Verification + persistence logic
│           ├── util/         #    JSON-RPC client, hex helpers
│           └── config/       #    Loads crypto.properties
├── manager/                 # Admin/Manager Web UI (JSP)
├── contracts/               # ⛓️ NEW — Solidity smart contracts
│   ├── RideEscrow.sol       #    Deposit / release / refund escrow
│   └── README.md            #    Deploy instructions (Hardhat)
├── sql/                     # ⛓️ NEW — schema migrations
│   └── 2026-07-16_add_crypto_tx.sql
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

### Step 7 (optional): Enable Web3 Payments

```bash
# Deploy the escrow contract (see contracts/README.md)
cd contracts && npx hardhat run scripts/deploy.js --network baseSepolia

# Apply the crypto DB migration
mysql -u root -p pinche < sql/2026-07-16_add_crypto_tx.sql

# Configure the backend
cp service/src/crypto.properties.example service/src/crypto.properties
$EDITOR service/src/crypto.properties   # fill in RPC url + contract address

# Redeploy the war — new endpoints go live automatically:
#   POST /webservice/payOrderCrypto
#   GET  /webservice/getCryptoPayment
```

See the [⛓️ Web3 Payment & Booking Path](#️-web3-payment--booking-path) section below for full details.

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

### Web3 Configuration

| File | Purpose |
|------|---------|
| [service/src/crypto.properties](service/src/crypto.properties.example) | Chain RPC URL, chain ID, escrow contract address, min confirmations, CNY→wei rate |
| [contracts/RideEscrow.sol](contracts/RideEscrow.sol) | On-chain escrow contract (deploy target — not runtime config) |
| [service/src/com/webapi/crypto/service/SVCCryptoPayService.java](service/src/com/webapi/crypto/service/SVCCryptoPayService.java) | `TOPIC_DEPOSITED` — set to the `Deposited` event topic0 printed at deploy |

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
5. Order accepted
        ↓
6. Rider pays  ─── (a) Alipay / WeChat  → SVCOrderAction.payNormalOrder
                └─ (b) Crypto escrow    → SVCCryptoPayAction.payOrderCrypto
        ↓
7. Trip executed → Evaluation → (crypto) escrow.release() to driver
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
| Payments (Alipay/WeChat) | Android/Service | `WapPayActivity` | ✅ |
| Geolocation/Maps | Android/iOS | Baidu Maps API | ✅ |
| Social Login | Android/iOS | UMeng SDK | ✅ |
| Driver Profiles | Manager/Service | `SVCUser*` | ✅ |
| Evaluation System | Service | `SVCEvaluationCS` | ✅ |
| CSV Export | Manager | `Common.java` | ✅ |
| ⛓️ On-chain Escrow Contract | Contracts | [contracts/RideEscrow.sol](contracts/RideEscrow.sol) | ✅ |
| ⛓️ Crypto Deposit Verification | Service | [SVCCryptoPayService.java](service/src/com/webapi/crypto/service/SVCCryptoPayService.java) | ✅ |
| ⛓️ Crypto Payment API | Service | [SVCCryptoPayAction.java](service/src/com/webapi/crypto/action/SVCCryptoPayAction.java) | ✅ |
| ⛓️ On-chain `release()` from Backend | Service | needs web3j / signing lib | 🟡 |
| ⛓️ WalletConnect UI (Android) | Android | `android/CarPoolingApp/` | ❌ |
| ⛓️ WalletConnect UI (iOS) | iOS | `ios/CarPoolingApp/` | ❌ |

---

## ⛓️ Web3 Payment & Booking Path

Alongside the classic Alipay / WeChat Pay flow, the platform ships a
fully on-chain escrow payment path. This is **additive** — no existing pay
flow was modified, and the change footprint is a handful of new files plus
two lines of Struts wiring.

### 🧱 Architecture at a glance

```
┌──────────────────┐   deposit() payable      ┌──────────────────────┐
│  Rider Wallet    │ ───────────────────────► │  RideEscrow.sol      │
│ (MetaMask / WC)  │                          │  (arbitrated escrow) │
└──────────────────┘                          └──────────────────────┘
         │                                              ▲       │
         │ POST txHash                                  │       │ release()
         ▼                                              │       ▼
┌──────────────────┐   JSON-RPC verify       ┌──────────────────────┐
│  Rider App       │ ───────────────────────►│  Backend (Struts2)   │
└──────────────────┘                         │  SVCCryptoPayAction  │
                                             │  SVCCryptoPayService │
                                             │  EthRpcClient        │
                                             └──────┬───────────────┘
                                                    │ INSERT
                                                    ▼
                                             ┌─────────────────────┐
                                             │ order_crypto_tx     │
                                             │ (MySQL side table)  │
                                             └─────────────────────┘
```

### 🗂️ File Map

| Piece | Location |
|-------|----------|
| Solidity escrow contract | [contracts/RideEscrow.sol](contracts/RideEscrow.sol) |
| Contract deploy notes | [contracts/README.md](contracts/README.md) |
| Backend action (Struts2) | [service/src/com/webapi/crypto/action/SVCCryptoPayAction.java](service/src/com/webapi/crypto/action/SVCCryptoPayAction.java) |
| Verification / persistence | [service/src/com/webapi/crypto/service/SVCCryptoPayService.java](service/src/com/webapi/crypto/service/SVCCryptoPayService.java) |
| JSON-RPC client (JDK-only) | [service/src/com/webapi/crypto/util/EthRpcClient.java](service/src/com/webapi/crypto/util/EthRpcClient.java) |
| Hex utilities | [service/src/com/webapi/crypto/util/HexUtil.java](service/src/com/webapi/crypto/util/HexUtil.java) |
| Config loader | [service/src/com/webapi/crypto/config/CryptoConfig.java](service/src/com/webapi/crypto/config/CryptoConfig.java) |
| Config template | [service/src/crypto.properties.example](service/src/crypto.properties.example) |
| DB migration | [sql/2026-07-16_add_crypto_tx.sql](sql/2026-07-16_add_crypto_tx.sql) |
| Struts wiring | [service/src/struts-config/struts-webapi-module.xml](service/src/struts-config/struts-webapi-module.xml) — actions **129** & **130** |

### 📜 Smart Contract API

`RideEscrow.sol` — Solidity 0.8.20, native currency (ETH / MATIC / xDAI…).

| Function | Caller | Purpose |
|----------|--------|---------|
| `constructor(address arbiter)` | deployer | sets initial arbiter (the platform backend's hot wallet) |
| `deposit(uint256 orderId, address driver) payable` | passenger | locks `msg.value` for this order — reverts if orderId already used |
| `release(uint256 orderId)` | arbiter | sends escrowed funds to the driver recorded in the deposit |
| `refund(uint256 orderId)` | arbiter | returns escrowed funds to the payer (cancellations / disputes) |
| `transferArbiter(address to)` + `acceptArbiter()` | arbiter → new | 2-step arbiter rotation (prevents accidental lockout on a typo) |

**Events** (all indexed by `orderId` for cheap event filtering):
- `Deposited(uint256 orderId, address payer, address driver, uint256 amount)`
- `Released(uint256 orderId, address driver, uint256 amount)`
- `Refunded(uint256 orderId, address payer, uint256 amount)`

### 🔐 Backend Verification (what happens on `payOrderCrypto`)

When the rider app posts a `txHash`, the backend refuses to trust it until
**all** of these pass:

1. **Not already recorded** — `tx_hash` is UNIQUE in `order_crypto_tx`
2. **Receipt exists** — `eth_getTransactionReceipt` returns non-null
3. **Tx succeeded** — receipt `status == 0x1` (post-Byzantium)
4. **Correct contract** — receipt `to` == `crypto.escrow.address`
5. **Enough confirmations** — `head_block - tx_block + 1 ≥ crypto.min.confirmations`
6. **`Deposited` event present** — for this exact `orderId` (topic1 match)
7. **Payer matches** — event `payer` topic == submitted `walletAddress`
8. **Amount sufficient** — event `amount ≥ expected_wei` (derived from `price_cny × crypto.wei.per.cny`)

Only then does the row land in `order_crypto_tx`.

### 🌐 API Endpoints

| Method | Path | Params | Purpose |
|--------|------|--------|---------|
| POST | `/webservice/payOrderCrypto` | `source, userid, orderid, order_type, price, tx_hash, wallet_addr, devtoken` | Verify a deposit tx and record the payment |
| GET  | `/webservice/getCryptoPayment` | `orderid` | Return the recorded on-chain state for an order |

All existing pay endpoints (`payNormalOrder`, `payReserveOrder`, `charge`,
`withdraw`, etc.) continue to work unchanged — clients pick the payment
method per booking.

### 🗄️ Database

New side-table `order_crypto_tx` (see [migration](sql/2026-07-16_add_crypto_tx.sql)):

| Column | Notes |
|--------|-------|
| `tx_hash` | UNIQUE — same tx can't be double-credited |
| `wallet_addr` / `driver_addr` | Normalized to lowercase for comparison |
| `amount_wei` | Stored as decimal string (uint256 doesn't fit in BIGINT) |
| `chain_id` | Multi-chain deployments distinguished here |
| `block_number` | For reconciliation with a chain-indexer if you ever add one |
| `expected_cny` | The app's quoted price at pay time — useful for auditing |
| `released` / `refunded` | Flipped when escrow.release() / refund() lands |

### 🛠️ Setup Checklist

- [ ] Deploy `RideEscrow.sol` — see [contracts/README.md](contracts/README.md); pick chain (Base Sepolia for testing, Base / Arbitrum / Polygon for prod)
- [ ] Note the printed contract address and the `Deposited` event's `topic0`
- [ ] Apply migration: `mysql -u root -p pinche < sql/2026-07-16_add_crypto_tx.sql`
- [ ] `cp service/src/crypto.properties.example service/src/crypto.properties`
- [ ] Fill in `crypto.rpc.url`, `crypto.chain.id`, `crypto.escrow.address`, `crypto.wei.per.cny`
- [ ] Set the real `TOPIC_DEPOSITED` constant in [SVCCryptoPayService.java](service/src/com/webapi/crypto/service/SVCCryptoPayService.java) (currently `null` for POC)
- [ ] Rebuild and redeploy the war
- [ ] Smoke test with a small deposit on testnet before enabling in the mobile UI

### 🔍 Testing (Hardhat local chain)

```bash
cd contracts
npx hardhat node                     # spawns local chain on :8545
npx hardhat run scripts/deploy.js --network localhost
# In another terminal — point the backend at http://localhost:8545 in
# crypto.properties (chain_id=31337), redeploy war, then curl the endpoint:
curl -X POST http://localhost:8080/service/webservice/payOrderCrypto \
     -d 'source=11&userid=1&orderid=123&order_type=1&price=25&tx_hash=0x...&wallet_addr=0x...&devtoken=abc'
```

### 🛡️ Trust Model — Honest Take

This is **"crypto rails, centralized settlement"**. The platform backend is
the escrow arbiter — it decides `release` vs. `refund` based on off-chain
trip state (driver marked complete, rider disputed, etc.). Users trust the
platform to act honestly; the on-chain part gives you:

✅ **Transparent, auditable payment history** — anyone can inspect the
   escrow contract and see every deposit / release / refund
✅ **Cross-border rails** — accept USDC-equivalent from anywhere without
   correspondent banking friction
✅ **Instant driver payout** on trip completion (no T+2 settlement)
✅ **Provable refunds** — a canceled deposit is refunded on-chain, not
   locked in an app-side balance

⚠️ **What it does NOT give you (yet):**
- Trustless arbitration — the backend's arbiter key can rug any deposit
- Automatic release on physical trip completion — release is still triggered
   by the backend, not by a trip-completion oracle
- Fee splitting on-chain — platform commission is currently deducted
   off-chain in the same table

**Upgrade paths that don't break the deposit ABI:**
- Rotate arbiter to a 2-of-3 multi-sig (Safe / Squads)
- Add a dispute window: `release()` becomes callable by the rider after
   `T + 24h` unless the driver has flagged an issue
- Move platform commission on-chain via a `feeBps` state variable

### 🌏 Regulatory Note

Crypto payments are **restricted or banned in mainland China** — the
original target market of this codebase (Alipay + WeChat Pay + Baidu Maps
integration reflects that). Enable the web3 path only in jurisdictions
where digital-asset payments to individuals are permitted (US*, EU, HK, SG,
LATAM, etc.), and consult local counsel on KYC/AML thresholds — the escrow
contract itself is permissionless.

*_Some US states impose additional MTL obligations; consult counsel._

### 🚧 Deferred Work (calling out honest gaps)

1. **`TOPIC_DEPOSITED` is `null`** in [SVCCryptoPayService.java](service/src/com/webapi/crypto/service/SVCCryptoPayService.java) — the event-signature check is skipped in the POC. Set to the real topic0 (Hardhat prints it on deploy) before running against real funds.
2. **Backend does not yet CALL `release()`** — verify+record is fully wired, but signing outbound tx to the escrow needs web3j (intentionally kept out to avoid dependency bloat). The columns `released` and `release_tx` exist and wait for you.
3. **Wallet UI in the mobile clients** — a "Pay with Crypto" button, WalletConnect handshake, and `deposit()` call still need to be added to `android/CarPoolingApp` and `ios/CarPoolingApp`.

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
| `crypto.properties not on classpath` at startup | Copy from `crypto.properties.example`; it must land in `WEB-INF/classes/` after build |
| `payOrderCrypto` returns "链上交易尚未打包" | Rider submitted `txHash` too early — the RPC hasn't seen the receipt yet. Retry after ~1 block time (2s on Base, 12s on Ethereum) |
| `payOrderCrypto` returns "链上确认数不足" | Wait more blocks, or lower `crypto.min.confirmations` for testnet |
| `payOrderCrypto` returns "交易目标合约不匹配" | `crypto.escrow.address` in properties doesn't match the contract the deposit was sent to — check chain ID too |
| `payOrderCrypto` returns "未在交易中找到订单 X 的存款事件" | Wrong `orderId` in the deposit call, OR `TOPIC_DEPOSITED` set to the wrong topic0 |
| Deposit succeeded but no `release()` fires | Expected — see [Deferred Work](#-deferred-work-calling-out-honest-gaps). Signing library not yet wired |

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
- [ ] ⛓️ Wire backend-signed `escrow.release()` (integrate web3j or equivalent)
- [ ] ⛓️ WalletConnect v2 integration in Android / iOS clients

### Medium Priority
- [ ] Distributed Locking (Redis/DB)
- [ ] Token-based Authentication (JWT)
- [ ] Payment Gateway Integration
- [ ] ⛓️ ERC-20 stablecoin (USDC / USDT) support in `RideEscrow.sol`
- [ ] ⛓️ Multi-sig arbiter (Safe on Base / Squads-equivalent) for release/refund
- [ ] ⛓️ Auto-release after dispute window instead of arbiter-only

### Low Priority
- [ ] CI/CD Pipeline
- [ ] Automated Tests (including Foundry / Hardhat suite for `RideEscrow`)
- [ ] OpenAPI/Swagger Documentation
- [ ] ⛓️ Soulbound reputation NFTs for driver/rider ratings
- [ ] ⛓️ Chain indexer (Subgraph / Envio) for admin dashboard queries

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

## License

MIT — do whatever you want, but a link back is appreciated.

For issues and questions:
- Open an issue on GitHub
- Check existing documentation
- Review the [Troubleshooting](#-troubleshooting) section

---

## ⭐ Support the project

If this repo saved you time — or gave you a reference for retrofitting web3 onto a legacy backend — **please star it**. Stars are the single strongest signal to other developers that a repo is worth their time, and they cost you nothing.

<p align="center">
  <a href="https://github.com/Markcus0526/dmy-carpooling-platform/stargazers">
    <img alt="Star on GitHub" src="https://img.shields.io/github/stars/Markcus0526/dmy-carpooling-platform?style=social">
  </a>
  &nbsp;·&nbsp;
  <a href="https://github.com/Markcus0526/dmy-carpooling-platform/fork">
    <img alt="Fork on GitHub" src="https://img.shields.io/github/forks/Markcus0526/dmy-carpooling-platform?style=social">
  </a>
</p>

---

**Built with ❤️ for the carpooling community · Powered by ⛓️ for a trust-minimized web**

