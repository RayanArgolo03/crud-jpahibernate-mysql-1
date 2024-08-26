# CRUD - Jpa e Hibernate - Relacionamento Unidirecional

Este projeto consiste em um crud com JPA e Hibernate. Foi criado para fins de estudo e aprendizado de novas tecnologias. Nesta branch, todos os relacionamentos feitos entre as classes são unidirecionais. 

# Tecnologias utilizadas
## Linguagens e convenções
- Java
- SQL 
- JPA
- JPQL 
## Banco de Dados
- MariaDB 
- H2 Database para testes integrados
## Frameworks
- Lombok
- Mapstruct
- Hibernate️
- Criteria API
# Testes
- Junit5
- Parameterised Tests
- Mockito 
- SystemStubs (Testes inputs System.in)
# Build
- Maven
- Docker (Container MariaDb)
# Log
- Sl4j
- Log4j2
# Design Patterns utilizados
- Builder
- DTO
- MVC
- Repository
- Singleton no acesso ao banco de dados
- Utility Classes

## Organização das classes
<img src="https://github.com/user-attachments/assets/ee68a04c-4c9b-4d16-ba88-748520d8a0bd"></img>

# Como usar o aplicativo?
## Inicialização com Docker Compose

0. Faça um fork, baixe ou clone o programa

1. Certifique-se de ter o Docker instalado em sua máquina e inicie-o

2. Abra o terminal de sua IDE

3. Execute o seguinte comando no terminal para iniciar o MariaDB e seu database consumidos pelo app:

   ```
   Inicializa database direto no container
   docker-compose up -d
    ```
4. Utilize o app pela IDE e divirta-se :)

## Diagrama Entidade Relacional

<img src="https://github.com/user-attachments/assets/36e56d7a-5547-4373-9b85-d250bae4410c"></img>

## Workflow do projeto

<img src="https://github.com/user-attachments/assets/aad255ce-cf01-4138-b164-b6ddf87f863f"></img>



## Contribuição
Contribuições são bem-vindas! Sinta-se à vontade para abrir problemas (issues) ou enviar solicitações de pull (pull requests) com melhorias, correções de bugs ou novas funcionalidades.

```
  Somos o que fazemos repetidas vezes, então, a excelência não é um ato, mas sim um hábito
  - Aristóteles, Ética a Nicômaco
```



