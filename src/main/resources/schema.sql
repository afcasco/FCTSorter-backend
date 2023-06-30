/*
create table IF NOT EXISTS company
(
    id       bigint auto_increment
        primary key,
    address  varchar(255)                                     null,
    cif      varchar(255)                                     null,
    city     varchar(255)                                     null,
    name     varchar(255)                                     null,
    phone    varchar(255)                                     null,
    zip_code varchar(255)                                     null,
    status   enum ('ACTIVE', 'INACTIVE', 'MARKED_FOR_REVIEW') null
);

create table IF NOT EXISTS  refreshtoken
(
    id          bigint       not null
        primary key,
    expiry_date datetime(6)  not null,
    token       varchar(255) not null,
    user_id     bigint       null,
    constraint UK_81otwtvdhcw7y3ipoijtlb1g3
        unique (user_id),
    constraint UK_or156wbneyk8noo4jstv55ii3
        unique (token),
    constraint FKa652xrdji49m4isx38pp4p80p
        foreign key (user_id) references users (id)
);

create table  IF NOT EXISTS refreshtoken_seq
(
    next_val bigint null
);

create table IF NOT EXISTS  roles
(
    id   int auto_increment
        primary key,
    name enum ('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER') null
);

create table IF NOT EXISTS  user_roles
(
    user_id bigint not null,
    role_id int    not null,
    primary key (user_id, role_id),
    constraint FKh8ciramu9cc9q3qcqiv4ue8a6
        foreign key (role_id) references roles (id),
    constraint FKhfh9dx7w3ubf1co1vdev94g3f
        foreign key (user_id) references users (id)
);

create table IF NOT EXISTS  users
(
    id       bigint auto_increment
        primary key,
    email    varchar(50)  null,
    password varchar(120) null,
    username varchar(20)  null,
    constraint UK6dotkott2kjsp8vw4d0m25fb7
        unique (email),
    constraint UKr43af9ap4edm43mmtq01oddj6
        unique (username)
);

*/
