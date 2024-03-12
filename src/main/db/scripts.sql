DROP TABLE IF EXISTS interactions;
DROP TABLE IF EXISTS contacts_info;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers(
customer_id SERIAL PRIMARY KEY,
organisation VARCHAR(255) NOT NULL,
city VARCHAR(255),
industry VARCHAR(255),
created_at TIMESTAMP,
updated_at TIMESTAMP
);
/*
* Description: Represents the individuals or entities that interact with your business
*/

TRUNCATE TABLE customers RESTART IDENTITY;

CREATE TABLE contacts_info(
contact_id SERIAL PRIMARY KEY,
customer_id INT,
name VARCHAR(255),
type VARCHAR(255),
details VARCHAR(255),
preferred BOOLEAN,
CONSTRAINT fk_customer
     FOREIGN KEY (customer_id)
            REFERENCES customers(customer_id)
);
/*
 * Description: Contact details for a customer are stored, allowing for multiple contact methods.
 */

TRUNCATE TABLE contacts_info RESTART IDENTITY;

CREATE TABLE interactions(
interaction_id SERIAL PRIMARY KEY,
customer_id INT,
contact_id INT,
date DATE,
type VARCHAR(255),
notes text,
CONSTRAINT fk_customer
    FOREIGN KEY (customer_id)
        REFERENCES customers(customer_id),
CONSTRAINT fk_contact
    FOREIGN KEY (contact_id)
        REFERENCES contacts_info(contact_id)
);
/*
 * Description: Interactions with the customer are logged, such as support calls, emails, or sales visits.
 */

TRUNCATE TABLE interactions RESTART IDENTITY;