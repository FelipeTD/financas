# Financas
Projeto para controle de finanças

Será colocado todo o passo a passo que foi utilizado para criar e melhorar o projeto até sua versão final

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