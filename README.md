# ⚙💻 OSFR System

OSFR System é um sistema desktop multiplataforma (Windows, Linux e macOS) para gestão de ordens de serviço em empresas de assistência técnica.
Permite o registo e acompanhamento de reparações, administração de clientes, emissão de relatórios e controlo de técnicos, proporcionando uma 
solução robusta para otimizar o fluxo operacional das estações de trabalho.

## Autor
Simão Ferro Rodrigues

## Funcionalidades

- Registo de clientes
- Gestão de ordens de serviço
- Relatórios gerenciais
- Controlo de utilizadores com perfis diferentes

## Requisitos

- Java 8 ou superior
- Banco de dados MySQL ou compatível

## Instalação

1. **Configurar Banco de Dados:**
   - Iniciar os serviços **Apache** e **MySQL** (caso esteja a utilizar o XAMPP).
   - Aceder ao **phpMyAdmin** e crie a base de dados **db_osfr**.
   - Executar o seguinte script SQL:

```sql
CREATE TABLE tbusuarios (
  iduser INT PRIMARY KEY,
  usuario VARCHAR(15) NOT NULL,
  fone VARCHAR(15),
  login VARCHAR(15) NOT NULL UNIQUE,
  senha VARCHAR(250) NOT NULL,
  perfil VARCHAR(20) NOT NULL
);

INSERT INTO tbusuarios (iduser, usuario, login, senha, perfil)
VALUES (1, 'Administrador', 'admin', MD5('admin'), 'admin');

CREATE TABLE tbclientes (
  idcli INT PRIMARY KEY AUTO_INCREMENT,
  nomecli VARCHAR(50) NOT NULL,
  endcli VARCHAR(100),
  fonecli VARCHAR(15) NOT NULL,
  emailcli VARCHAR(50) UNIQUE
);

CREATE TABLE tbos (
  os INT PRIMARY KEY AUTO_INCREMENT,
  data_os TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  tipo VARCHAR(15) NOT NULL,
  situacao VARCHAR(20) NOT NULL,
  equipamento VARCHAR(150) NOT NULL,
  defeito VARCHAR(150),
  servico VARCHAR(150),
  tecnico VARCHAR(30),
  valor DECIMAL(10,2),
  idcli INT NOT NULL,
  FOREIGN KEY (idcli) REFERENCES tbclientes(idcli)
);
```

2. **Instalar o Aplicativo:**
   - Fazer o download da versão mais recente a partir da secção **Releases**.
   - Extrair o conteúdo e executar o ficheiro **OSFR-System.jar**.

## Tecnologias Utilizadas

- Sistema de Login funcional com atribuição de cargo
- Criação de banco de dados e tabelas no MySQL
- CRUD (Create Read Update e Delete)
- IDE Netbeans
- Java SE
- JDBC (Java Database Connectivity)
- Validação de dados
- Uso do framework Jaspersoft para gerar relatórios

### Ferramentas
[openJDK 8 (LTS)](https://adoptopenjdk.net/)

[Apache NetBeans IDE 24](https://filehippo.com/download_netbeans/8.2/)

[Jaspersoft Studio 7.0.1]([https://community.jaspersoft.com/download-jaspersoft/download-jaspersoft/])

[Inno Setup](https://jrsoftware.org/isinfo.php)


## Contribuição

Contribuições são bem-vindas! Sinta-se livre para abrir issues e submeter pull requests.

## Agradecimentos
 - Obrigado ao professor José de Assis pelo curso passo a passo
 - Obrigado por utilizar o OSFR System! 😊

