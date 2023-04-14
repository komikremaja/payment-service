CREATE TABLE payment_data(
	id_payment INT AUTO_INCREMENT,
    id_transaction INT not null,
    va_number varchar(255) not null,
    total_amount DOUBLE NOT NULL,
    source_account varchar(255),
    currency varchar(255) NOT NULL,
    destination_account varchar(255) NOT NULL,
    bank_payment varchar(255),
    nic varchar(255) NOT NULL,
    payment_status varchar(10) NOT NULL,
    created_date timestamp not null,
    last_update timestamp not null,
    PRIMARY KEY(id_payment)
);