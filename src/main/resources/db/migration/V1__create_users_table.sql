create table users (
    id bigint auto_increment primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    created_at timestamp default current_timestamp
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci;

-- Insert initial admin user with password: 'password'
insert into users (username, password) values 
('admin', '$2a$10$qhR.nWk.VX3Hs/JB8.8douM1GKoqPGGVfYwNqmVJQMG7UJnG0Rvuy'); 