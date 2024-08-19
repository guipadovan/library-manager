# Library Manager

## Funcionalidades

- **Operações CRUD**: Gerencia livros e usuários com funcionalidades de criar, ler, atualizar e deletar.
- **Operações de Empréstimo**: Empréstimo de livros para usuários e devolução.
- **Sistema de Recomendação**: Sugere livros para os usuários com base no histórico de leitura.
- **Integração com a API do Google Books**: Busca informações de livros na Google Books e adiciona ao banco de dados da biblioteca.

## Tecnologias utilizadas

### Backend

- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Flyway
- Lombok
- Jakarta Validation

### Frontend

- React
- TypeScript
- Vite
- Mantine
- React Router DOM

## Configuração do Backend

### Pré-requisitos

- JDK 21
- PostgreSQL
- Maven

### Configuração

1. **Configuração do Banco de Dados**: Configure a conexão do banco de dados na `application.yml`:

    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/library-manager
        username: postgres
        password: postgres

    google:
      books:
        api-key: GOOGLE_BOOKS_API_KEY
    ```

   Substitua a `GOOGLE_BOOKS_API_KEY` pela chave da API do Google Books.

2. **Executar Migrações**: Você pode rodar as migrações manualmente ou deixar que o projeto execute-as ao iniciar:

    ```bash
    ./mvnw flyway:migrate
    ```

### Executando o Backend

1. **Compilar o Projeto**:

    ```bash
    ./mvnw clean install
    ```

2. **Rodar a Aplicação**:

    ```bash
    ./mvnw spring-boot:run
    ```

O servidor backend estará rodando em `http://localhost:8080`.

## Configuração do Frontend

### Pré-requisitos

- Node.js (20.9.0 usado neste projeto)
- npm

### Configuração

1. **Instalar Dependências**:

    ```bash
    npm install
    ```

### Executando o Frontend

1. **Iniciar o Servidor de Desenvolvimento**:

    ```bash
    npm run dev
    ```

O servidor frontend estará rodando em `http://localhost:5173`.

## Testes

### Backend

1. **Rodar Testes Unitários**:

    ```bash
    ./mvnw surefire:test
    ```

Os testes do backend utilizam JUnit e Mockito para testar as diversas funcionalidades.
