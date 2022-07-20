CREATE TABLE credit
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    amount numeric(19,2),
    credit_status jsonb,
    is_insurance_enabled boolean,
    is_salary_client boolean,
    monthly_payment numeric(19,2),
    payment_schedule jsonb,
    psk numeric(19,2),
    rate numeric(19,2),
    term integer,
    PRIMARY KEY (id)
    );