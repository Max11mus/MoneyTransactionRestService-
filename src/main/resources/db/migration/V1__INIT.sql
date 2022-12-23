--DDL
SET TIME ZONE 'UTC';

CREATE SCHEMA IF NOT EXISTS ums;

CREATE TABLE IF NOT exists ums.transaction_ums (
	transaction_id uuid NOT NULL,
	date_time timestamp NOT NULL,
	owner_id uuid NOT NULL,
	receiver_id uuid NOT NULL,
	amount numeric(19, 2) NOT NULL,
    currency varchar(3) NOT NULL,
	CONSTRAINT transaction_ums_pkey PRIMARY KEY (transaction_id)
);

--DATA

INSERT INTO ums.transaction_ums(
    transaction_id,
    date_time,
    owner_id,
    receiver_id,
    amount,
    currency
)
VALUES(
    'dbe415e2-b575-43f4-8aaa-cc85207c81fb'::uuid,
    '2022-06-04 09:20:00.000',
    '6e1e9867-4670-4520-8b85-7c195e72bd6c'::uuid,
    '12611b1e-b277-4e64-8ff3-243a5d6fbc2d'::uuid,
    125.93,
    'UAH'),
    (
        '018a3a83-92ba-4e31-9306-52236ee08f64'::uuid,
        '2022-04-04 09:20:00.000',
        'f57e0ffe-6118-44a8-b39d-b2da86b65aff'::uuid,
        'c3e47148-adcf-4ee3-81f6-6b79b83a41ca'::uuid,
        700.00,
        'UAH'),
    (
        'be5ed718-0497-496e-83f0-cb90487c5376'::uuid,
        '2022-08-08 09:20:00.000',
        '210dd67b-7810-4edf-98be-e9a2cffe6290'::uuid,
        'd87a90ba-1237-419a-b199-19dc389b4bbf'::uuid,
        50.00,
        'USD'),
    (
        '8188b34c-123b-4f5a-94eb-1aaa4825e641'::uuid,
        '2022-06-06 09:20:00.000',
        'b24d4f8d-f32f-4f88-a219-ebeb30568a1b'::uuid,
        'd87a90ba-1237-419a-b199-19dc389b4bbf'::uuid,
        340.40,
        'USD');