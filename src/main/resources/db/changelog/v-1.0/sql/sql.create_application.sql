CREATE TABLE application
(
    id bigint NOT NULL,
    applied_offer jsonb,
    creation_date date,
    ses_code integer,
    sign_date date,
    status jsonb,
    status_history jsonb,
    client_id bigint,
    credit_id bigint,
    PRIMARY KEY (id),

    FOREIGN KEY (client_id) REFERENCES client (id)
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,

    FOREIGN KEY (credit_id) REFERENCES credit (id) ON UPDATE NO ACTION
    ON DELETE NO ACTION
    );