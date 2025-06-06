
# 💻 Desafio Full Stack - Sistema de Dashboard de Vendas

Este projeto é um sistema web full stack que apresenta um dashboard para visualização e gerenciamento de dados de vendas. 

O sistema conta com um **backend em Java com Spring Boot** que expõe uma API RESTful para operações de CRUD em vendas e autenticação de usuários. O **frontend é uma aplicação React** que consome essa API, exibe os dados em gráficos e tabelas, e permite a interação do usuário com as funcionalidades do sistema.

---


## ✨ Principais Funcionalidades

- **Autenticação de Usuário**: Acesso seguro à aplicação utilizando um token JWT.
- **Dashboard Interativo**: Gráficos de barras e de linhas que apresentam os dados de vendas de forma clara e intuitiva.
- **Visualização de Vendas**: Tabela completa com todas as vendas registradas, permitindo uma visão detalhada dos dados.
- **Filtragem de Dados**: Filtre as vendas por um intervalo de datas para análises específicas.
- **Gerenciamento de Vendas (CRUD)**:
  - Criação de novas vendas.
  - Consulta de uma venda específica por seu ID.
  - Exclusão de vendas existentes.
- **Documentação da API**: A API do backend é documentada com Swagger (OpenAPI), facilitando o entendimento e teste dos endpoints.

---

## 🛠️ Tecnologias Utilizadas

### Backend

- Java 17  
- Spring Boot 3.3.0  
- Spring Data JPA (persistência de dados)  
- Spring Security (controle de acesso e autenticação)  
- H2 Database (banco de dados em memória)  
- JSON Web Token (JWT)  
- Maven (gerenciador de dependências)  
- SpringDoc OpenAPI (Swagger)

### Frontend

- React 19  
- Axios (requisições HTTP)  
- Chart.js (criação de gráficos)  
- date-fns (manipulação e formatação de datas)  
- CSS3 (estilização dos componentes)

---

## 📋 Pré-requisitos

Antes de começar, certifique-se de que você tem as seguintes ferramentas instaladas:

- **Java Development Kit (JDK)**: Versão 17 ou superior  
- **Maven**: Versão 3.9 ou superior  
- **Node.js**: Versão 14.0.0 ou superior  
- **Navegador Web**: Chrome, Firefox, ou outro de sua preferência  

---

## 🚀 Como Executar o Projeto

O projeto é dividido em backend e frontend, e ambos precisam estar em execução simultaneamente.

### 1. Backend (API de Vendas)

O backend é responsável por toda a lógica de negócio e pela exposição dos dados através de uma API REST.

```bash
# 1. Clone o repositório
git clone https://github.com/juillerms/desafio-fullstack.git

# 2. Navegue até o diretório do backend
cd desafio-fullstack/backend

# 3. Compile e execute o projeto com o Maven Wrapper
./mvnw spring-boot:run
```

- A API estará disponível em: http://localhost:8080


- O banco H2 será populado com dados de exemplo automaticamente na primeira execução.

- 🔐 Credenciais de Acesso da API:
   * Usuário: admin

    * Senha: senha123

- 📚 Documentação da API (Swagger)
    * Acesse: http://localhost:8080/swagger-ui.html

Use o botão "Authorize" para autenticar endpoints protegidos com seu token JWT.

**Obs:** Para conseguir a credencial para testar os endpoints será necessário fazer a autenticação:
* Clicar em *Try it out*.
* Usar as mesmas credenciais (admin/senha123) e executar.
* Copiar o token gerado na resposta (*Response Body*)


### 2. Frontend (Dashboard)
A aplicação React consome a API do backend para exibir o dashboard.
```bash
# 1. Abra um novo terminal e navegue até o diretório do frontend
cd desafio-fullstack/frontend

# 2. Instale as dependências do projeto
npm install

# 3. Inicie a aplicação React
npm start
```


A aplicação será aberta automaticamente no navegador em: http://localhost:3000

Faça login utilizando as mesmas credenciais do backend (admin / senha123)

✅ Projeto pronto para testes e demonstrações! Sinta-se à vontade para explorar, modificar e aprimorar.


