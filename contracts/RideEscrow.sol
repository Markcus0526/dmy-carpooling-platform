// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

/// @title RideEscrow — minimal on-chain escrow for the DMY carpooling platform.
/// @notice Passenger deposits native currency (ETH/MATIC/etc.) for a specific
///         order ID. Funds are held by the contract until the platform
///         backend (arbiter) releases them to the driver, or refunds on cancel.
///
/// Trust model: this is "crypto rails, centralized settlement". The arbiter
/// (the platform backend) decides release vs. refund based on off-chain trip
/// state. Users trust the arbiter to act honestly. A dispute period + on-chain
/// arbiter rotation could be added later without changing the deposit ABI.
contract RideEscrow {
    enum Status { None, Deposited, Released, Refunded }

    struct Deposit {
        address payer;
        address driver;
        uint256 amount;
        Status  status;
    }

    address public arbiter;
    address public pendingArbiter;

    mapping(uint256 => Deposit) public deposits;

    event Deposited(uint256 indexed orderId, address indexed payer, address indexed driver, uint256 amount);
    event Released (uint256 indexed orderId, address indexed driver, uint256 amount);
    event Refunded (uint256 indexed orderId, address indexed payer,  uint256 amount);
    event ArbiterTransferStarted (address indexed from, address indexed to);
    event ArbiterTransferAccepted(address indexed newArbiter);

    modifier onlyArbiter() {
        require(msg.sender == arbiter, "not arbiter");
        _;
    }

    constructor(address _arbiter) {
        require(_arbiter != address(0), "arbiter=0");
        arbiter = _arbiter;
    }

    /// Passenger locks funds for `orderId`. Reverts if the order already has a
    /// deposit — an order is one-shot; retries must use a new orderId.
    function deposit(uint256 orderId, address driver) external payable {
        require(msg.value > 0, "no value");
        require(driver != address(0), "driver=0");
        require(deposits[orderId].status == Status.None, "exists");

        deposits[orderId] = Deposit({
            payer:  msg.sender,
            driver: driver,
            amount: msg.value,
            status: Status.Deposited
        });

        emit Deposited(orderId, msg.sender, driver, msg.value);
    }

    /// Backend calls after off-chain trip completion — funds go to driver.
    function release(uint256 orderId) external onlyArbiter {
        Deposit storage d = deposits[orderId];
        require(d.status == Status.Deposited, "bad state");
        d.status = Status.Released;

        (bool ok, ) = d.driver.call{value: d.amount}("");
        require(ok, "transfer failed");
        emit Released(orderId, d.driver, d.amount);
    }

    /// Backend calls after cancel/dispute-refund — funds return to payer.
    function refund(uint256 orderId) external onlyArbiter {
        Deposit storage d = deposits[orderId];
        require(d.status == Status.Deposited, "bad state");
        d.status = Status.Refunded;

        (bool ok, ) = d.payer.call{value: d.amount}("");
        require(ok, "transfer failed");
        emit Refunded(orderId, d.payer, d.amount);
    }

    // --- Arbiter rotation (2-step, to prevent lockout on typo) --------------

    function transferArbiter(address to) external onlyArbiter {
        pendingArbiter = to;
        emit ArbiterTransferStarted(arbiter, to);
    }

    function acceptArbiter() external {
        require(msg.sender == pendingArbiter, "not pending");
        arbiter = pendingArbiter;
        pendingArbiter = address(0);
        emit ArbiterTransferAccepted(msg.sender);
    }
}
