CREATE TABLE client
(
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    account character varying(255),
    birth_date date,
    dependent_amount integer,
    email character varying(255),
    employment jsonb,
    first_name character varying(255),
    gender jsonb,
    last_name character varying(255),
    marital_status jsonb,
    middle_name character varying(255),
    passport jsonb,
    PRIMARY KEY (id)
    );