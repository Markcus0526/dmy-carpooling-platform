-- 2026-07-16 : add order_crypto_tx table for on-chain payment records.
--
-- Rationale: rather than adding a nullable tx_hash column to every order-
-- related table (orders / order_temp_details / order_onoffduty_details /
-- order_longdistance_details), we keep crypto payments in a single side
-- table. Joining is by (order_id, order_type). This means zero migrations
-- on the hot order tables.

CREATE TABLE IF NOT EXISTS order_crypto_tx (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    order_id      BIGINT       NOT NULL,
    order_type    INT          NOT NULL COMMENT '1=once, 2=onoff, 3=long',
    user_id       BIGINT       NOT NULL,
    tx_hash       VARCHAR(66)  NOT NULL COMMENT '0x-prefixed 32-byte tx hash',
    wallet_addr   VARCHAR(42)  NOT NULL COMMENT 'passenger wallet, 0x + 40 hex',
    driver_addr   VARCHAR(42)  NOT NULL COMMENT 'driver wallet from the deposit event',
    amount_wei    VARCHAR(80)  NOT NULL COMMENT 'stored as decimal string; 2^256 fits in 78 chars',
    chain_id      BIGINT       NOT NULL,
    block_number  BIGINT       NOT NULL,
    expected_cny  DOUBLE       NOT NULL COMMENT 'app-side quoted price, for reconciliation',
    released      TINYINT      NOT NULL DEFAULT 0 COMMENT 'set when escrow.release() succeeds',
    refunded      TINYINT      NOT NULL DEFAULT 0 COMMENT 'set when escrow.refund() succeeds',
    release_tx    VARCHAR(66)          DEFAULT NULL,
    refund_tx     VARCHAR(66)          DEFAULT NULL,
    created_at    DATETIME     NOT NULL,
    updated_at    DATETIME              DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_tx_hash (tx_hash),
    KEY idx_order (order_id, order_type),
    KEY idx_user  (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
