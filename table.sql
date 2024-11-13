CREATE TABLE products(
id INT AUTO_INCREMENT PRIMARY KEY,
product_id VARCHAR(255) NOT NULL UNIQUE,
product_name VARCHAR(255) NOT NULL,
short_name VARCHAR(255),
data_grouping BOOLEAN NOT null
);

CREATE TABLE prices(
id INT AUTO_INCREMENT PRIMARY KEY,
product_id VARCHAR(255) NOT NULL,
price_date_time Datetime  NULL,
price decimal (10,2) NOT NULL,
FOREIGN KEY (product_id) REFERENCES products(product_id)
)

