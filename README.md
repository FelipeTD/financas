# Financas
Projeto para controle de finanças

Será colocado todo o passo a passo que foi utilizado para criar e melhorar o projeto até a sua versão final

## Objetivos

### Tecnologias que precisam ser utilizadas:

- Formato de documentos: JSON, XML.
- Framework: Spring MVC, Spring Boot.
- Metodologias: POO, Clean Code, SOLID, Design Patterns.
- Servidor de aplicação: Tomcat.
- Testes: Teste Unitário/integrado (Junit).
- Banco de dados: UML, modelagem de dados, MySQL, SQL, JPA, Hibernate.

### Primeiro objetivo: Construir uma aplicação inicial com Hello World
Abaixo todos os passos feitos para criar a aplicação inicial
- Criação do repositorio no GitHub no link https://github.com/FelipeTD/financas
- Criação do projeto Spring utilizando o Spring Initializr pelo link https://start.spring.io/
- Configurações utilizadas para o projeto inicial:
  - Maven
  - Spring Boot 3.2.1
  - Java 17
  - Dependências
    - Spring Web
      - Build web, including RESTful, applications using Spring MVC. Uses Apache Tomcat as the default embedded container.
    - Thymeleaf
      - A modern server-side Java template engine for both web and standalone environments.
      - Allows HTML to be correctly displayed in browsers and as static prototypes.
    - Spring Boot DevTools
      - Provides fast application restarts, LiveReload, and configurations for enhanced development experience.

#### Links Utilizados
- Instalação do maven no computador
  - https://coderanch.com/t/753681/maven/build-tools/Intellij-warning-Maven-wrapper-correct
- Projeto Spring MVC do zero
  - https://spring.io/guides/gs/serving-web-content/
- Download dependencia do maven para Junit
  - https://mvnrepository.com/artifact/junit/junit/4.13.2

### Segundo Objetivo: Adicionar JPA ao projeto
Abaixo todos os passos feitos para adicionar o JPA ao projeto:
- Foram adicionadas duas dependências ao pom.xml.
- Banco de dados h2 para armazenar os dados e JPA para acessar os dados.
- Foi criada uma interface CustomerRepository que extende CrudRepository.
- Essa ‘interface’ foi utilizada para chamar os métodos findByLastName e findById do CrudRepository.
- Na classe FinancasApplication fizemos um método demo() para testar o código.
- Gerando um arquivo JAR com o comando 'mvn clean package'.
- Teste realizado com o JAR gerado para garantir que continua tudo funcionando.

#### Links Utilizados
- Acessing Data with JPA
  - https://spring.io/guides/gs/accessing-data-jpa/

### Terceiro Objetivo: Adicionar o MySQL ao projeto
Abaixo todos os passos feitos para adicionar o MySQL ao projeto:
- Instalação do docker para subir uma imagem do MySQL.
- Lembrando que para conseguir utilizar o Docker é necessário baixar o ubuntu para windows caso você utilizar WSL2.
- Processo para configurar um banco MySQL no Docker
  - Comando para criar container "docker run -p 3306:3306 --name=seu-container -d mysql/mysql-server"
  - Comando para pegar senha gerada "docker logs seu-container 2>&1 | grep GENERATED"
    - Recomendo executar comando utilizando o PowerShell
  - Execute o comando "docker exec -it seu-container mysql -uroot -p"
    - Cole a senha gerada no comando anterior
    - Esse comando é utilizado para entrar no banco de dados
  - Execute o comando "ALTER USER 'root'@'localhost' IDENTIFIED BY '12345';"
    - Esse comando irá mudar a senha para '12345'.
  - Execute o comando "update mysql.user set host = '%' where user='root';"
    - Esse comando permite que realize a conexão pelo Workbench.
  - Aperte Control + D para sair do banco e reinicie o container no docker.
- Comando para executar dentro do MySQl para criação de usuário e banco de dados:
  - "CREATE DATABASE FINANCAS;"
    - Cria o banco de dados FINANCAS.
  - create user 'tortora'@'%' identified by 'ftd38427689';
    - Cria o usuário tortora com a senha ftd38427689.
  - grant all on FINANCAS.* to 'tortora'@'%';
    - Garante todas as permissões para o usuário tortora.
- Configuração do arquivo application.properties
  - O comando "pring.jpa.hibernate.ddl-auto" deve começar com create e depois update
  - O comando "spring.jpa.show-sql" ajuda quando é necessário ver o SQL que é gerado.
- Em relação ao código:
  - Foi adicionado uma classe model User para guardar as informações do usuário.
    - Essa classe também cria a entidade no banco de dados.
  - Foi adicionado uma classe controller UserController para criar os endpoints do usuário.
  - Foi adicionado uma interface UserRepository que extende o CrudRepository.
    - Para ter acesso aos métodos do CrudRepository.
  - No arquivo pom.xml foi necessário adicionar a dependencia do driver do MySQL

#### Links Utilizados
- Acessing Data with MySQL
  - https://spring.io/guides/gs/accessing-data-mysql/
- Download do Docker para subir uma imagem do MySQL
  - https://www.docker.com/products/docker-desktop/
- Imagem oficial do MySQL para Docker
  - https://hub.docker.com/_/mysql
- Download do MySQL Workbench
  - https://dev.mysql.com/downloads/workbench/
- Configuração do banco de dados para acessar pelo workbench
  - https://dev.to/nfo94/como-criar-um-container-com-mysql-server-com-docker-e-conecta-lo-no-workbench-linux-1blf
- Página com a dependência correta do MySQL
  - https://stackoverflow.com/questions/33123985/cannot-load-driver-class-com-mysql-jdbc-driver-spring

### Quarto Objetivo: Adicionar uma API RestFull
- A API tem que possuir os quatro métodos: GET, POST, PUT e DELETE
- Uma anotação importante é que as caracteristicas abaixo não fazem de um projeto Restful:
  - URL's como '/employees/3' não são REST.
  - Apenas usar GET, POST, PUT, DELETE não torna o seu projeto Rest.
  - Ter um projeto com todas as operações CRUD não torna o seu projeto um Rest.
  - O nome desse projeto é RPC (Remote Procedure Call)
- Como transformar o projeto num projeto Restful?
  - Precisamos trabalhar na parte de consumo do cliente.
  - Para isso foi adicionado a dependencia Spring HATEOAS que facilita o consumo.
  - Vantagens em utilizar HATEOAS:
    - Ao adicionar o HATEOAS retornamos também um objeto com os links que podemos consumir.
    - Continuamos retornando o objeto que será consumido.
    - Conseguimos modificar serviços sem alterar serviços antigos.
    - O exemplo que foi dado é de alterar o modelo Employee para ter primeiro nome e ultimo nome.
    - Podemos continuar usando o nome completo que continua funcionando.
  - Desvantagens em utilizar HATEOAS: 
    - Necessário atualizar o banco de dados para preencher os campos primeiro e último nome.
    - Se não for atualizado no banco de dados será retornado todos os campos nulos para dados antigos.
    - Existe uma maior complexidade nas classes Assembler

#### Links Utilizados
- Building REST services with Spring
  - https://spring.io/guides/tutorials/rest/

























