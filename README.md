# Desafio Técnico BTG Pactual - Processador de Pedidos

Este projeto é a solução para o Desafio Técnico do BTG Pactual, focado no processamento de pedidos e geração de relatórios usando uma arquitetura de microserviços.

A aplicação utiliza **Java com Spring Boot** para criar um consumidor que processa mensagens de uma fila do **RabbitMQ** e as persiste em um banco de dados **MongoDB**. Além disso, expõe uma **API REST** para consultar os dados processados.

> **Contexto:** Este repositório contém a minha solução para o Desafio Técnico Back-end do BTG Pactual.
> O descritivo original do problema pode ser consultado [neste link](https://github.com/buildrun-tech/buildrun-desafio-backend-btg-pactual/blob/main/problem.md).

## 🚀 Funcionalidades

* **Consumidor de Mensageria:** Escuta uma fila no RabbitMQ, processa os pedidos e salva no banco de dados.
* **API REST:** Expõe endpoints para consultar informações sobre os pedidos, incluindo:
    * Valor total de um pedido específico.
    * Quantidade de pedidos realizados por um cliente.
    * Lista de pedidos de um cliente.

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 17
* **Framework:** Spring Boot 3
* **Banco de Dados:** MongoDB (utilizando Spring Data MongoDB)
* **Mensageria:** RabbitMQ (utilizando Spring AMQP)
* **Containerização:** Docker e Docker Compose

## 🏛️ Arquitetura

A aplicação é um único serviço Spring Boot que possui duas responsabilidades principais:

1.  **Consumer (Listener):** Um componente (`@RabbitListener`) fica ouvindo a fila de pedidos. Ao receber uma mensagem (como a do exemplo abaixo), ele calcula o valor total do pedido e salva a entidade `Pedido` no MongoDB.
2.  **API REST (Controller):** Um `@RestController` expõe endpoints HTTP que consultam o MongoDB para retornar as informações solicitadas.

### Exemplo da Mensagem (Payload do RabbitMQ)
```json
{
    "codigoPedido": 1001,
    "codigoCliente":1,
    "itens": [
        {
            "produto": "lápis",
            "quantidade": 100,
            "preco": 1.10
        },
        {
            "produto": "caderno",
            "quantidade": 10,
            "preco": 1.00
        }
    ]
}
```
## ⚙️ Como Executar

Este projeto é totalmente containerizado.

O `docker-compose` irá baixar automaticamente a imagem da aplicação (publicada no Docker Hub) e subir todos os serviços necessários (Aplicação, RabbitMQ e MongoDB).

### Pré-requisitos

* [Git](https://git-scm.com/)
* [Docker](https://www.docker.com/products/docker-desktop/) (que já inclui o Docker Compose)

### Passos

1.  Clone o repositório:
    ```bash
    git clone https://github.com/TiagoNarita/btg-pactual-chanlenger
    cd orderms
    cd local
    ```

2.  Suba todo o ambiente:
    ```bash
    docker compose up -d
    ```
    *(O `-d` executa em modo "detached", em segundo plano). O Docker irá baixar (pull) a imagem `tiagonarita/orderms-app` do Docker Hub e iniciar todos os containers.*

3.  É isso! Os containers estarão rodando.
    * A aplicação estará disponível em: `http://localhost:8080`
    * O painel do RabbitMQ estará em: `http://localhost:15672` (login: guest/guest)
    * O MongoDB estará acessível em: `mongodb://admin:123@localhost:27017`

### Como Parar o Ambiente
Para derrubar todos os containers, rode na mesma pasta:
```bash
docker compose down
```

## 🧪 Como Testar o Fluxo

Para testar o sistema de ponta a ponta, você precisa **publicar uma mensagem** no RabbitMQ para que o consumidor possa processá-la.

1.  Acesse o painel de gerenciamento do RabbitMQ (normalmente em `http://localhost:15672`).
    * **Login:** `guest`
    * **Senha:** `guest`

2.  Vá para a aba **Queues** e clique na fila de pedidos (o nome estará no seu `application.properties`, ex: `pedidos.fila`).

3.  Encontre a seção **Publish message**.

4.  No campo **Payload**, cole o JSON do pedido (veja o exemplo acima no README).

5.  Clique em **Publish message**.

Seu serviço Spring Boot irá consumir esta mensagem, e você já poderá consultar os dados via API.

## 📋 Endpoints da API

* **URL Base:** `http://localhost:8080`

---

### 1. Consultar Valor Total do Pedido

Retorna o valor total de um pedido específico.

* **Endpoint:** `GET /orders/{orderId}/total`
* **Exemplo:** `GET {{baseURL}}/orders/1001/total`
* **Resposta Esperada:**
    ```json
    {
        "orderTotal": 120.00
    }
    ```

---

### 2. Consultar Quantidade de Pedidos por Cliente

Retorna o número total de pedidos já realizados por um cliente.

* **Endpoint:** `GET /orders/customers/{customerId}/count`
* **Exemplo:** `GET {{baseURL}}/orders/customers/1/count`
* **Resposta Esperada:**
    ```json
    {
        "count": 3
    }
    ```

---

### 3. Listar Pedidos por Cliente

Retorna uma lista paginada de todos os pedidos feitos por um cliente.

* **Endpoint:** `GET /orders/customers/{customerId}`
* **Exemplo:** `GET {{baseURL}}/orders/customers/1`
* **Resposta Esperada:**
    ```json
    {
        "content": [
            {
                "orderId": 1001,
                "customerId": 1,
                "total": 120.00,
                "items": [
                    { "product": "lápis", "quantity": 100, "price": 1.10 },
                    { "product": "caderno", "quantity": 10, "price": 1.00 }
                ]
            },
            {
                "orderId": 1002,
                "customerId": 1,
                "total": 120.00,
                "items": [
                    { "product": "lápis", "quantity": 100, "price": 1.10 },
                    { "product": "caderno", "quantity": 10, "price": 1.00 }
                ]
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 20,
            "sort": { "empty": true, "sorted": false, "unsorted": true },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "last": true,
        "totalPages": 1,
        "totalElements": 2,
        "size": 20,
        "number": 0,
        "first": true,
        "numberOfElements": 2,
        "sort": { "empty": true, "sorted": false, "unsorted": true },
        "empty": false
    }
    ```
