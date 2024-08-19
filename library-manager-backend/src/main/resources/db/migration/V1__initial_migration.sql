CREATE SEQUENCE IF NOT EXISTS books_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS leases_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE books
(
    id              BIGINT       NOT NULL,
    titulo          VARCHAR(255) NOT NULL,
    autor           VARCHAR(255) NOT NULL,
    isbn            VARCHAR(255) NOT NULL,
    data_publicacao date         NOT NULL,
    categoria       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_books PRIMARY KEY (id)
);

CREATE TABLE leases
(
    id              BIGINT       NOT NULL,
    usuario_id      BIGINT       NOT NULL,
    livro_id        BIGINT       NOT NULL,
    data_emprestimo date         NOT NULL,
    data_devolucao  date         NOT NULL,
    status          VARCHAR(255) NOT NULL,
    CONSTRAINT pk_leases PRIMARY KEY (id)
);

CREATE TABLE users
(
    id            BIGINT       NOT NULL,
    nome          VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    data_cadastro date         NOT NULL,
    telefone      VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE leases
    ADD CONSTRAINT FK_LEASE_BOOK FOREIGN KEY (livro_id) REFERENCES books (id) ON DELETE CASCADE;

ALTER TABLE leases
    ADD CONSTRAINT FK_LEASE_USER FOREIGN KEY (usuario_id) REFERENCES users (id) ON DELETE CASCADE;
