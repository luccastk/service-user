# 🔐 service-user

Microserviço responsável pela **autenticação e gestão de usuários**, utilizando JWT para autenticação segura via `Spring Security`.

---

## 📌 Objetivo

Centralizar a autenticação e autorização da plataforma, com endpoints protegidos, emissão de tokens JWT e gerenciamento de perfis de usuários.

---

## ⚙️ Stack Tecnológica

- **Linguagem:** Java
- **Framework:** Spring Boot
- **Banco de dados:** PostgreSQL
- **Mensageria:** Kafka
- **Segurança:** Spring Security + JWT
- **Descoberta de serviços:** Eureka
- **Gateway de entrada:** Spring Gateway

---

## 📁 Arquitetura

Arquitetura MVC (Controller → Service → Repository), com camadas bem definidas para segurança e autenticação.

---

## 🔗 Principais Endpoints

### 👤 User

- `POST /login` – Login do usuário e emissão de JWT
- `PUT /password/change` – Alteração de senha autenticado

### 🛠️ Admin (Gestão de Funcionários)

- `POST /v1/admin/create` – Criação de funcionário
- `GET /v1/admin` – Listagem de funcionários
- `DELETE /v1/admin/delete/{employeeId}` – Remoção de funcionário

### 🩺 Healthcheck

- `GET /` – Verificação de disponibilidade do serviço

---

## 🧪 Testes

- ✅ **Unitários:**
    - Serviços de autenticação
    - Geração e validação de JWT
    - Listeners Kafka

- ✅ **Integração:**
    - Controllers via `MockMvc`

Para executar os testes:
```bash
./mvnw test
