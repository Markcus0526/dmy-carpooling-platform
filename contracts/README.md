# RideEscrow contract

Minimal escrow used by the DMY carpooling platform's crypto payment path.

## Deploy (Hardhat, Base Sepolia example)

```bash
npm init -y
npm i --save-dev hardhat @nomicfoundation/hardhat-toolbox
npx hardhat init            # choose "Create an empty hardhat.config.js"
mkdir -p contracts && cp RideEscrow.sol contracts/
npx hardhat compile
# Set PRIVATE_KEY + BASE_SEPOLIA_RPC in .env
npx hardhat run --network baseSepolia scripts/deploy.js
```

Sample `scripts/deploy.js`:
```js
const hre = require("hardhat");
async function main() {
  const arbiter = process.env.ARBITER_ADDR; // the backend's hot wallet
  const F = await hre.ethers.getContractFactory("RideEscrow");
  const c = await F.deploy(arbiter);
  await c.waitForDeployment();
  console.log("RideEscrow:", await c.getAddress());
}
main();
```

## Backend config

After deploy, copy the address into `service/src/crypto.properties`:
```
crypto.rpc.url=https://sepolia.base.org
crypto.chain.id=84532
crypto.escrow.address=0xYOUR_DEPLOYED_ADDRESS
crypto.min.confirmations=2
crypto.arbiter.privatekey=0x...   # only needed if backend calls release() itself
```

## Frontend flow (rider app)

1. Rider taps "Pay with crypto" on a `chengjiao` (accepted) order.
2. App opens wallet (WalletConnect / MetaMask deep link).
3. App calls `deposit(orderId, driverWallet)` with `value = fareWei`.
4. On tx confirm, app POSTs `txHash + walletAddress + orderId` to the backend's
   `/webservice/payOrderCrypto` endpoint.
5. Backend verifies the tx on-chain and marks the order paid.

## Release flow (backend after trip completion)

The existing `endOnceOrder` / `endOnOffOrder` / `endLongOrder` flows should
call `releaseCryptoEscrow(orderId)` for orders that were paid on-chain. That
endpoint (implemented in `SVCCryptoPayAction`) currently just records the
intent; wiring the actual `release()` call to the contract requires an ETH
signing library (e.g. web3j) which is intentionally left out of this POC to
avoid adding a heavy dependency.
