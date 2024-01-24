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
- Gerando um arquivo JAR com o comando `mvn clean package`.
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
    - Esse comando irá mudar a senha para `12345`.
  - Execute o comando `update mysql.user set host = '%' where user='root';`
    - Esse comando permite que realize a conexão pelo Workbench.
  - Aperte Control + D para sair do banco e reinicie o container no docker.
- Comando para executar dentro do MySQL para criação de utilizador e banco de dados:
  - "CREATE DATABASE FINANCAS;"
    - Cria o banco de dados FINANCAS.
  - create user 'tortora'@'%' identified by 'ftd38427689';
    - Cria o utilizador tortora com a senha ftd38427689.
  - grant all on FINANCAS.* to 'tortora'@'%';
    - Garante todas as permissões para o utilizador tortora.
- Configuração do arquivo application.properties
  - O comando "spring.jpa.hibernate.ddl-auto" deve começar com create e depois ‘update’
  - O comando `spring.jpa.show-sql` ajuda quando é necessário ver o SQL que é gerado.
- Em relação ao código:
  - Foi adicionado uma classe model User para guardar as informações do utilizador.
    - Essa classe também cria a entidade no banco de dados.
  - Foi adicionado uma classe controller UserController para criar os endpoints do usuário.
  - Foi adicionado uma interface UserRepository que extende o CrudRepository.
    - Para ter acesso aos métodos do CrudRepository.
  - No arquivo pom.xml foi necessário adicionar a dependencia do `driver do MySQL`

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

### Quinto Objetivo: Adicionar Testes Unitários
- Será utilizado JUnit5 para os testes unitários
- Explicação sobre o JUnit5
  - Composto por JUnit Platform, JUnit Jupiter e JUnit Vintage
    - JUnit Platform
      - Define a API TestEngine para desenvolver novas estruturas de teste executados na plataforma.
    - JUnit Jupiter
      - Possui todas as novas anotações junit e a implementação TestEngine para executar testes escritos com essas anotações.
    - JUnit Vintage
      - Oferece suporte a execução de testes escritos em JUnit3 e JUnit4 na plataforma JUnit5.
- Senti a necessidade de colocar um módulo service 
  - Na minha opinião fica mais organizado os controladores chamarem o serviço.
  - Dessa forma o serviço utiliza o repositório para pegar o que precisa no banco de dados.
  - Quando for feita a validação nos testes unitários basta chamar o controlador
  - O controlador vai chamar o serviço e o serviço vai chamar o repositorio.
- Os testes na classe Employee foi necessário colocar um Spy para conseguir retornar o valor do 'ModelAssembler'.
  - Algumas anotações do JUnit ajudam no momento do teste.
  - A parte má é que precisa saber como elas funcionam.
  - A parte boa é que consegue encontrar muitos exemplos na ‘internet’.
- Centralizando o método stringAsJson dentro de uma classe Utils.
  - Como era utilizado em vários lugares centralizei ela

#### Links Utilizados
- Testando uma API Rest Spring Boot 2 com JUnit5 e MockMVC
  - https://medium.com/@gcbrandao/testando-uma-api-rest-spring-boot-2-com-junit5-e-mockmvc-db603c65a306
- CRUD JUnit Tests for Spring Data JPA - Testing Repository Layer
  - https://www.javaguides.net/2021/07/crud-junit-tests-for-spring-data-jpa.html
- Testing the Web Layer
  - https://spring.io/guides/gs/testing-web/
- Spring Boot MockMvc Example with @WebMvcTest
  - https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/

### Sexto Objetivo: Adicionar Coverage no projeto
- Para adicionar um coverage basta adicionar o plugin do JaCoCo.
- É possivel configurar as classes que quer excluir do relatório.
- Atualmente o coverage está em 62%.
- Deixar coverage acima de 80%.
  - Analisar os cenários e criar testes para cobrir esses cenários
  - A parte boa de aumentar a cobertura de testes é que precisa ajustar a lógica do método para cobrir alguns casos que não foram pensandos.
  - Isso permite que aumente a robustez do projeto contra erros antes de seguir com o desenvolvimento.
  - Coloquei uma regra dentro do JaCoCo para dar falha no build do projeto se o coverage estiver abaixo de 60%.
  - Para testar hashCode que utiliza outra classe para construir o objeto utilizei null
    - Exemplo: Order o = new Order("Minha ordem", null);
    - Se utilizar o objeto `Status` em cada execução de mvn clean install vai produzir um novo hashCode.
  - Adicionado estrutura MVC para testar o customer.
  - A ordem que realizo os testes é:
    - Controller
      - Testar caso positivo
      - Testar exceções
    - Model
      - Testar métodos especificos como hashCode, toString e equals
    - Services
      - Testar caso positivo
      - Testar exceções
  - No final consegui deixar o projeto com 100% de coverage.

#### Links Utilizados
- Completude de testes com JaCoCo em aplicações Spring Boot
  - https://mmarcosab.medium.com/completude-de-testes-com-jacoco-em-aplica%C3%A7%C3%B5es-spring-boot-74055773cc37
- Exemplo de configuração JaCoCo com coverage de 60%
  - https://www.eclemma.org/jacoco/trunk/doc/examples/build/pom.xml
- Exemplo de teste de exceções com MvcMock
  - https://www.baeldung.com/spring-mvc-test-exceptions

### Sétimo Objetivo: Adicionar testes integrados
- Testes integrados são testes para verificar se os componentes funcionam corretamente em conjunto.
- Um exemplo de teste integrado nesse projeto:
  - Cadastrar um ou mais funcionários.
  - Buscar todos os funcionários cadastrados.
  - Buscar o funcionário cadastrado por ID.
  - Atualizar um funcionário.
  - Deletar um funcionário.
- Lembrando que o banco deve ficar da mesma forma que estava antes de o teste ser realizado.
- Testes integrados também utilizam o JUnit.
- Utilização do TestContainers para os testes de integração.
  - O TestContainers utiliza o docker para criar uma imagem do MySQL e realizar os testes.
  - Depois que os testes são concluidos essa instância é deletada.
  - É uma excelente opção, pois não influência em nada no nosso banco de dados.


#### Links Utilizados
  - DevMedia (Utilizado como referência)
    - https://www.devmedia.com.br/testes-de-integracao-com-java-e-junit/25662
  - Obter o Size de um Iterable em Java
    - https://receitasdecodigo.com.br/java/obter-o-size-de-um-iterable-em-java
  - Spring Boot MySQL integration tests with Testcontainers
    - https://www.geekyhacker.com/spring-boot-mysql-integration-tests-with-testcontainers/
  - Getting started with Testcontainers in a Java Spring Boot Project
    - https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/

### Oitavo Objetivo: Modelagem de dados e UML
- Se utiliza o Intellij Community a melhor opção é o PlantUML.
  - É um plugin que gera alguns diagramas UML para você.
- Se utiliza o Intellij Ultimate ele já vem com uma opção de diagramas.
  - Pasta clicar com o botão direito no módulo ou classe que quer observar os diagramas.
- Quando um projeto é construido tem que considerar o UML para entender o que cada parte do código faz
- A modelagem de dados é tão importante quanto.
  - Ao utilizá-la você deve considerar:
    - Quais objetos precisam ser criados?
    - Como os objetos irão se relacionar?
    - Entre outras considerações.

#### Links Utilizados
- PlantUML Diagram Generator
  - https://plugins.jetbrains.com/plugin/15991-plantuml-diagram-generator
- UltimateUML class diagrams
  - https://www.jetbrains.com/help/idea/class-diagram.html

### Nono Objetivo: POO, SOLID, Clean Code e Design Patterns
- Já fizemos bastante codificação e o projeto está bem organizado.
- Podemos falar um pouco sobre boas práticas de programação.
- POO
  - Como utilizando o conceito de classes em Java aplicamos o conceito POO
  - Esses objetos possuem atributos e métodos.
  - No caso das entidades temos os atributos e métodos para acessar esses atributos.
- SOLID
  - S — Single Responsiblity Principle
    - Controller para gerenciar os endpoints
    - Service para executar a lógica de negócio
    - Repository para buscar os dados no banco
  - O — Open-Closed Principle
    - Repository extend a class JpaRepository e tem todos os métodos que precisa
  - L — Liskov Substitution Principle
    - Esse principio não foi utilizado
  - I — Interface Segregation Principle
    - Nenhuma classe é forçada a implementar um método que não irá utilizar
  - D — Dependency Inversion Principle
    - Um ótimo exemplo é o repository
    - Mesmo que eu troque o banco para Oracle os comando continuam funcionando corretamente
- Clean Code
  - Regras aplicadas
    - Manter uma nomenclatura de fácil entendimento para as variáveis, funções, parâmetros, classes e métodos
    - Deixe o seu código mais limpo do que quando você o pegou.
    - Crie funções pequenas. Se puder deixe mais pequenas ainda.
    - Não repita o código. Não deve existir duas partes do programa desempenhando a mesma função.
    - Comente apenas o necessário. Nada de comentários desnecessários.
    - Saber tratar os erros da aplicação
    - Testes limpos. Testes devem ser rápidos e testar o necessário.
- Design Patterns
- Já existem diversos `Design Patterns` que são utilizados pelo Spring Boot
- Abaixo listo os que foram utilizados nesse projeto:
  - Inversão de Controle (IoC) e Injeção de Dependência (DI)
  - Padrão 'MVC'
  - Padrão Repository
  - Padrão Singleton

#### Links Utilizados
- O que é SOLID: O guia completo para você entender os 5 princípios da POO
  - https://medium.com/desenvolvendo-com-paixao/o-que-%C3%A9-solid-o-guia-completo-para-voc%C3%AA-entender-os-5-princ%C3%ADpios-da-poo-2b937b3fc530
- Alguns dos principais Padrões de Projeto presentes no Spring Boot
  - https://www.dio.me/articles/alguns-dos-principais-padroes-de-projeto-presentes-no-spring-boot
- O que é Clean Code e Quais são Suas Regras Básicas?
  - https://blog.accurate.com.br/clean-code/

### Décimo Objetivo: Adicionar requisição HTTP e tratamento da resposta em JSON e XML
- Para não ser necessário criar outro projeto para a requisição HTTP utilizei o Mock API
- URL do JSON
  - https://65ae79a41dfbae409a74ebbc.mockapi.io/api/v1/employee
- O que foi feito nessa etapa?
  - Criamos o HttpUtils para centralizar as chamadas HTTP e leitura do retorno
  - Dividimos os métodos por requisição.
  - Como é um projeto de exemplo temos somente o `GET` implementado.
- Nova funcionalidade para migração de `employess` de outro sistema
  - Realizamos a chamada da `API` que criamos acima e convertemos para a classe `Employee`
  - Depois realizamos a chamada do `repository` para salvar todos os novos empregados.
  - Foi utilizado a biblioteca `gson` para realizar a conversão do objeto JSON para Employee.
- Criação de API que retorna um XML utilizando o Beeceptor
  - Para pegar o XML utilize https://mpe2202814057b237397.free.beeceptor.com/data
- Conversão do XML para o objeto Employee
  - Foram criados alguns objetos POJO para receber o XML
  - Como agora temos dois objetos (JSON e XML) centralizei a conversão deles na classe ConvertUtils
  - Lembrando que precisa atualizar os testes unitários e de integração adicionando e modificando os testes

#### Links Utilizados
- Mock API
  - https://mockapi.io/
- Mockando APIs REST facilmente com o MockAPI
  - https://medium.com/code-prestige/mockando-apis-rest-facilmente-com-o-mockapi-48f03a78aaca
- Beeceptor
  - https://beeceptor.com/
- UnMarshalling- How to convert XML to Java Objects using JAXB
  - https://qaautomation.expert/2021/12/22/unmarshalling-how-to-convert-xml-to-java-objects-using-jaxb/
- JAXB – Marshal and Unmarshal List or Set of Objects
  - https://howtodoinjava.com/jaxb/jaxb-exmaple-marshalling-and-unmarshalling-list-or-set-of-objects/

### Décimo Primeiro Objetivo: Adicionar configurações do Docker
- Deploy do banco de dados
  - Comando para atualizar a imagem do mysql
    - docker pull mysql
  - Comando para criar uma rede que será usada para o deploy do banco e da aplicação
    - docker network create financas-net
  - Comando para verificar se a rede foi criada
    - docker network ls 
  - Caso necessário, comando para deletar a `network`
    - docker network rm financas-net
  - Comando para rodar o mysql na mesma network que o projeto
    - docker run -p 3306:3306 --name financasdb --network financas-net -e MYSQL_ROOT_PASSWORD=ftd38427689 -e MYSQL_DATABASE=FINANCAS -e MYSQL_USER=tortora -e MYSQL_PASSWORD=ftd38427689 -e DATABASE_PORT=3306 -d mysql:latest
  - Comando para acessar o container
    - docker exec -it financasdb bash
    - Também pode acessar o container pelo `Docker Desktop`
      - Clique na instância criada
      - Entre na aba Exec
  - Comando para acessar o mysql
    - mysql -u root -p
    - Digite a sua senha
    - show databases;
    - Comando para verificar a permissão dos `usuários`
      - select host, user from mysql.user;
      - Verifique se o `usuário` criado e o root estão com `host` '%'
    - exit para sair do banco de dados e do container
- Deploy da aplicação
  - mvn clean package
  - Se o comando tiver funcionado corretamente irá aparecer uma pasta `target` com um arquivo.jar
  - Crie um arquivo DockerFile no diretorio raiz do projeto
    - Ele deve ficar no mesmo nível do pom.xml
     ```
     FROM eclipse-temurin:17
  
     LABEL mentainer="javaguides.net@gmail.com"
  
     WORKDIR /app
  
     COPY target/financas-0.0.1-SNAPSHOT.jar /app/financas.jar
  
     ENTRYPOINT ["java", "-jar", "financas.jar"]
     ```
  - Adicione um arquivo chamado `application-docker.properties` 
    - Esse arquivo deve estar no mesmo nível do arquivo `application.properties`
    ```
    spring.datasource.url=jdbc:mysql://financasdb:3306/FINANCAS
    spring.datasource.username=tortora
    spring.datasource.password=ftd38427689

    #spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
    spring.jpa.hibernate.ddl-auto=update
    ```
  - Adicione uma propriedade dentro do `application.properties`
    - `spring.profiles.active=docker`
    - Essa propriedade deve ficar ativa somente quando for fazer deploy do projeto
    - Caso realize testes locais comente essa propriedade
  - Na pasta test. Crie uma pasta resources
    - Na pasta resources. Adicione um arquivo `application.properties`
    - Esse arquivo terá o seguinte conteúdo:
    ```
    spring.jpa.hibernate.ddl-auto=create

    spring.datasource.url=
    spring.datasource.username=
    spring.datasource.password=
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
    ```
    - Esse arquivo serve para executar os testes sem quebrar a aplicação
  - Execute o comando `mvn clean package` para criar o `jar` do projeto
- Criando a imagem do docker do projeto
  - Execute o comando `docker build -t financas .` para criar a imagem
  - Execute o comando `docker images` para verificar se a imagem foi criada
- Executando a imagem criada na mesma `network` que o banco de dados
  - Comando para iniciar a imagem
    - `docker run --network financas-net --name financas-container -p 8080:8080 financas`
    - A explicação do comando seria:
      - docker run --network <nome_da_network>
      - --name <nome_do_container>
      - -p <porta_que_vai_rodar:porta_no_container>
      - nome da imagem gerada anteriormente
- Lembrando que executamos dois projetos separados na mesma network
- Caso a conexão não seja estabelicida entre o banco de dados e o projeto
  - Delete a imagem criada e os containers.
  - Execute o comando docker compose up
    - Esse comando irá subir a sua aplicação a partir do arquivo docker-compose.yml
    - Ele irá dar erros no começo da execução porque o banco de dados não iniciou
    - Após executado o comando os containers irão ficar disponíveis no docker
  

#### Links Utilizados
- Deploy Spring Boot MySQL Application to Docker
  - https://www.javaguides.net/2022/12/deploy-spring-boot-mysql-application-to-docker.html
- How to Dockerize Spring boot with MySql | Implementation | Live Demo | Docker Compose | Code Decode
  - https://www.youtube.com/watch?v=DwWkjEwsI4Q
			























