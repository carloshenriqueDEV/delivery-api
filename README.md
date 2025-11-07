<h1 align="center">🚀 DeliveryTech API</h1>

## Sobre
<p >
    &emsp;Este projeto tem como objetivo principal a implementação de uma <u>API REST</u> para gestão de pedidos, restaurantes, produtos e clientes.
    <br>
    &emsp;Como objetivo secundário, busca materializar o aprendizado de conceitos importantes de arquitetura de software voltada para aplicações web, tais como a construção de APIs REST (seguindo padrões de mercado), testes (unitários e de integração), documentação, monitoramento, observabilidade, segurança, empacotamento e orquestração (com Docker), além de práticas de CI/CD.

</p>

 ## 🏃 Como executar
 1. Pré-requisitos
    - Java 21
    - Maven
    - Docker 

2. Clone o respositório
3. Via Docker
    `docker-compose up --build`

4. Acesse (endpoints públicos): 

|  Método  | Endpoint               | Descrição                                                                           |
| :------: | ---------------------- | ----------------------------------------------------------------------------------- |
| **POST** | `/api/auth/login`      | 🔐 Realiza autenticação e retorna um token **JWT** para acesso às rotas protegidas. (user admin para testes email: carlos@delivery.com.br, senha: fhal123@$ )|
| **POST** | `/api/auth/register`   | 🧾 Realiza o cadastro de um **novo cliente** no sistema.                            |
|  **GET** | `/swagger-ui.html`     | 📚 Abre a **documentação interativa** da API (Swagger UI).                          |
|  **GET** | `/actuator/prometheus` | 📊 Expõe métricas da aplicação no formato **Prometheus** para monitoramento.        |
|  **GET** | `/actuator/metrics`    | 📈 Lista métricas de **performance, saúde e uso** da aplicação.                     |
|  **GET** | `/actuator/info`       | ℹ️ Retorna informações gerais da aplicação (ex: versão, nome, dados configurados).  |


<details>
  <summary><strong>🚀 Melhorias Futuras</strong></summary>

Este projeto ainda possui espaço para evolução, especialmente em aspectos relacionados a desempenho, observabilidade e robustez operacional. Entre as próximas implementações planejadas, destacam-se:

- **Testes de Carga e Estresse**  
  Avaliar o comportamento da aplicação sob alto volume de requisições, identificando gargalos de performance e garantindo estabilidade em cenários reais de uso.

- **Integração com Grafana para Observabilidade**  
  Configurar dashboards visuais utilizando **Prometheus + Grafana**, permitindo acompanhar métricas em tempo real, como tempo de resposta, throughput, consumo de recursos e taxas de erros.

- **Otimizações de Performance**  
  Revisar consultas, índices de banco e pontos de latência no fluxo de operações, buscando reduzir tempo de resposta e melhorar escalabilidade.

Essas melhorias fazem parte da evolução contínua do projeto, aproximando-o de cenários de produção e ambientes distribuídos de alta disponibilidade.

</details>


## 📝 Tecnologias
<p align="center">
  <img src="https://img.shields.io/badge/Java-21%20LTS-007396?style=for-the-badge&logo=openjdk" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.7-6DB33F?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/Spring%20Web-6DB33F?style=for-the-badge&logo=spring" />
  <img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring" />
  <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven" />
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens" />
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger" />
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql" />
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis" />
  <img src="https://img.shields.io/badge/Actuator-6DB33F?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/Micrometer-6DB33F?style=for-the-badge&logo=spring" />
  <img src="https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus" />
  <img src="https://img.shields.io/badge/JUnit%205-25A162?style=for-the-badge&logo=junit5" />
  <img src="https://img.shields.io/badge/Mockito-2A9D8F?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Spring%20Boot%20Test-6DB33F?style=for-the-badge&logo=springboot" />
  <img src="https://img.shields.io/badge/TestRestTemplate-000000?style=for-the-badge" />
   <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker%20Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white" />
</p>


## 👨‍💻 Desenvolvedor
Carlos - Turma 03362
Desenvolvido com JDK 21 e Spring Boot 3.5.7