# 🛒 Sistema de Supermercado

Aplicação desktop em Java com interface gráfica Swing para gerenciamento de produtos e compras em supermercado. O sistema oferece dois perfis de acesso — **administrador** e **cliente** — com fluxos distintos para cada um.

---

## 📋 Funcionalidades

### Administrador
- Cadastro, edição e remoção de produtos
- Visualização de estoque em tabela interativa

### Cliente
- Visualização de produtos disponíveis em estoque
- Adição e remoção de itens no carrinho
- Finalização de compra com emissão de **nota fiscal** em tela
- Atualização automática do estoque após a compra

### Geral
- Cadastro de usuários (nome, CPF, perfil de administrador)
- Login via nome + CPF
- Navegação entre telas com `CardLayout`
- Banco de dados inicializado automaticamente na primeira execução

---

## 🗂️ Estrutura do Projeto

```
src/
├── Main/
│   └── Main.java                   # Ponto de entrada da aplicação
├── Vision/
│   ├── Janela.java                 # JFrame principal com CardLayout
│   ├── TelaLogin.java              # Tela de login
│   ├── TelaCadastroUsuarios.java   # Tela de cadastro de usuário
│   ├── TelaCadastroProdutos.java   # Tela de gerenciamento de produtos (admin)
│   └── TelaCompra.java             # Tela de compra (cliente)
├── Controller/
│   ├── ProdutoController.java      # Regras de negócio para produtos
│   └── UsuarioController.java      # Regras de negócio para usuários
├── DAO/
│   ├── ProdutoDAO.java             # Acesso ao banco — tabela produtos
│   └── UsuarioDAO.java             # Acesso ao banco — tabela usuarios
├── Model/
│   ├── Produto.java                # Entidade Produto
│   ├── Usuario.java                # Entidade Usuario
│   └── Supermercado.java          # Singleton que gerencia o carrinho
└── DB/
    └── ConexaoBD.java              # Conexão JDBC e criação das tabelas
```

---

## 🏛️ Arquitetura

O projeto segue o padrão **MVC (Model-View-Controller)** com camada DAO:

| Camada | Responsabilidade |
|--------|-----------------|
| **Model** | Entidades do domínio (`Produto`, `Usuario`) e lógica do carrinho (`Supermercado`) |
| **View** | Telas Swing (`Vision`) — exibição e captura de eventos do usuário |
| **Controller** | Validação e orquestração entre View e DAO |
| **DAO** | Operações SQL diretas com o banco de dados via JDBC |
| **DB** | Gerenciamento da conexão e criação automática das tabelas |

O `Supermercado` é implementado como **Singleton**, garantindo um único carrinho por sessão da aplicação.

---

## 🛠️ Tecnologias e Dependências

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 11+ | Linguagem principal |
| Swing | (JDK) | Interface gráfica |
| MigLayout | 11.4.2 | Layout das telas Swing |
| MySQL Connector/J | 9.5.0 | Driver JDBC para MySQL |
| MySQL | 8.x | Banco de dados relacional |

Os JARs necessários estão incluídos no repositório:
- `com.miglayout.core_11.4.2.jar`
- `com.miglayout.swing_11.4.2.jar`
- `lib/mysql-connector-j-9.5.0/mysql-connector-j-9.5.0.jar`

---

## ⚙️ Pré-requisitos

- **JDK 11** ou superior
- **MySQL 8.x** em execução local
- IDE Eclipse (recomendada — o projeto inclui `.classpath` e `.project`)

---

## 🚀 Como Executar

### 1. Configure o banco de dados

Crie o banco no MySQL (as tabelas são criadas automaticamente pela aplicação):

```sql
CREATE DATABASE supermercado;
```

### 2. Verifique as credenciais

As configurações de conexão estão em `src/DB/ConexaoBD.java`:

```java
private static final String HOST    = "localhost";
private static final String PORTA   = "3306";
private static final String BANCO   = "supermercado";
private static final String USUARIO = "root";
private static final String SENHA   = "admin";
```

Altere conforme o seu ambiente, se necessário.

### 3. Importe no Eclipse

1. `File` → `Import` → `Existing Projects into Workspace`
2. Selecione a pasta raiz do projeto
3. Certifique-se de que os JARs estão no Build Path

### 4. Execute

Rode a classe `Main/Main.java`. A aplicação iniciará na tela de login.

---

## 🗄️ Estrutura do Banco de Dados

As tabelas são criadas automaticamente na primeira execução via `ConexaoBD.inicializarBanco()`.

```sql
CREATE TABLE IF NOT EXISTS usuarios (
    id            INT          AUTO_INCREMENT PRIMARY KEY,
    nome          VARCHAR(100) NOT NULL,
    cpf           VARCHAR(14)  NOT NULL UNIQUE,
    administrador TINYINT(1)   NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS produtos (
    id         INT           AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100)  NOT NULL,
    descricao  VARCHAR(255),
    preco      DECIMAL(10,2) NOT NULL,
    quantidade INT           NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## 🔄 Fluxo de Uso

```
Iniciar aplicação
      │
      ▼
  Tela de Login ──────────────► Tela de Cadastro de Usuário
      │
      ├── Login como ADMINISTRADOR ──► Tela de Cadastro de Produtos
      │                                 (CRUD completo de produtos)
      │
      └── Login como CLIENTE ──────► Tela de Compra
                                       (Carrinho + Nota Fiscal)
```

---

## ⚠️ Observações

- A conexão com o banco é um **Singleton** — apenas uma conexão JDBC fica aberta por sessão.
- O carrinho (`Supermercado`) também é Singleton; ao deslogar, o carrinho é limpo automaticamente.
- O estoque é decrementado no banco no momento da finalização da compra.
- Não há criptografia de senha — a autenticação é feita apenas por nome + CPF. Recomenda-se adicionar hashing para ambientes de produção.
