
# 📌 Banco Digital

Esse projeto é uma API REST de um banco Digital, desenvolvido em Java. O objetivo é aplicar conceitos de orientação a objetos, frameworks e persistência de dados.


# 🚀 Funcionalidades

✅ Cadastro e gerenciamento de clientes  
✅ Abertura, consulta, atualização e exclusão de contas bancárias  
✅ Emissão e administração de cartões  
✅ Realização de pagamentos e simulação de saldo  
✅ Tipos de clientes com vantagens  
✅ Tratamento de exceções personalizado  
✅ Saldo de conta em diferentes moedas (BRL, USD, EUR)  
✅ Conversão monetária automática ao realizar ações  
✅ Persistência em banco de dados (H2 em memória)  
✅ Arquitetura baseada em camadas (Controller, Service, Repository, etc.)

# 🛠️ Tecnologias Utilizadas
- **Java 21** - Versão do Java utilizada para o projeto
- **Spring Boot** – Framework para facilitar o desenvolvimento e configuração  
- **Spring Data JPA** – Alternativa para persistência de dados com JPA  
- **H2 Database** – Banco de dados em memória para testes e desenvolvimento  
- **Bean Validation (Jakarta Validation)** – Validação de dados via anotações  
- **Lombok** – Geração automática de getters, setters, constructors, etc.  
- **Maven** – Gerenciador de dependências e build  
- **Postman** – Para testes dos endpoints REST
- **SpringSecurity** - Segurança do Projeto com token JWT

# 💡 Diferenciais
O projeto faz integração com duas API externas que potencializam o uso da APIREST  

### 📍 Integração com API de Endereço via CEP  
Ao cadastrar um cliente, o sistema utiliza a API BrasilAPI para preencher automaticamente o endereço com base no CEP informado.

**✅Benefícios:**
- Preenchimento automático de **rua**, **bairro**, **cidade** e **estado**
- Redução de erros de digitação
- Experiência de cadastro mais rápida e intuitiva
- Otimização de tempo tanto pro sistema quanto pro usuário
---

### 💱 Suporte a Múltiplas Moedas (Multiwallet)

Simula uma carteira multimoeda, permitindo o gerenciamento de saldos em diversas moedas (BRL, USD, EUR). Isso é possível graças à integração com uma API de câmbio em tempo real, como a [AwesomeAPI](https://docs.awesomeapi.com.br/api-de-moedas).

**✅Funcionalidades:**
- Consulta de saldo em **BRL** (Real), **USD** (Dólar) e **EUR** (Euro)
- Conversão automática de valores durante transferências
- Suporte a operações internacionais, simulando uma verdadeira fintech
- O usuário tem o poder de depositar seu dinheiro em outra moeda, fazendo a conversão automática
---

- # 📂 Estrutura do Projeto

```plaintext
banco-digital
┣ 📂 src
┃ ┣ 📂 controller    # Endpoints da API 
┃ ┣ 📂 dto           # Objetos de transferência de dados 
┃ ┣ 📂 entity        # Entidades JPA que representam as tabelas do banco 
┃ ┣ 📂 repository    # Interfaces para acesso ao banco de dados com Spring Data JPA
┃ ┣ 📂 service       # Regras de negócio e lógica de aplicação
┃ ┣ 📂 exceptions    # Exceções personalizadas para regras de negócio
┃ ┣ 📂 handler       # Manipulação global de erros com @ControllerAdvice
┃ ┣ 📂 security      # Relação do SpringSecurity com o projeto
┣ 📄 README.md       # Documentação do projeto
┣ 📄 pom.xml         # Arquivo de build com dependências
```
# 🔧 Como Executar o Projeto
1️⃣ Clone o repositório:
```bash
git clone https://github.com/GustavoMD07/BancoDigitalSpring
cd BancoDigitalSpring
```

2️⃣ Execute a aplicação  
Opção 1: via terminal com Maven Wrapper
No Linux/macOS:
```bash
./mvnw spring-boot:run
```
No Windows:
```bash
mvnw spring-boot:run
```

Opção 2: via IDE (IntelliJ, Eclipse, VS Code)  

Abra o projeto na sua IDE favorita e execute o método main() da classe principal **BancoDigitalSpringApplication.java**.


3️⃣ Acesse o banco H2 (opcional):
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Usuário: sa
Senha: (deixe em branco)
```

# 🛡️ Tratamento de Exceções

O projeto possui um sistema de tratamento de erros personalizado usando:

- **Exceções customizadas na pasta exceptions**

- **Classe global de handler na pasta handler, utilizando @ControllerAdvice**

Dessa forma, Qualquer erro de negócio ou validação retorna uma resposta clara e padronizada pro cliente da API.

# 📈 Melhorias Futuras

🔹 Persistência com banco de dados real (PostgreSQL ou MySQL)  
🔹 Testes unitários com JUnit e Mockito  
🔹 Documentação de API com Swagger  
🔹 Interface frontend (React ou Angular) para visualização dos dados  

# 📌 Autor
👨‍💻 Gustavo Matachun Domingues
🔗 [LinkedIn](https://www.linkedin.com/in/gustavo-matachun/) | 📧 gustavomatachun.domingues@gmail.com

