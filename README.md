# routing-admin-server
**```routing information```** admin server with spring mvc, mongoDB, redis.

## Index

- [API](#API)
- [Model](#Model)
- [Redis](#Redis)

## API

this server provides routing information CRUD REST API.

### ```create``` 

POST /routings

**HTTP Request**

```
POST http://localhost:8080/routings
```

**Header Parameters**

| Header | Type | Description |
| --- | --- | --- |
| Content-Type | string | application/json |

**Request Body**

| Property     | Type              | Description                                               |
|--------------|-------------------|-----------------------------------------------------------|
| path         | string            |backend openAPI path                                      |
| method       | string            |backend openAPI method                                    |
| baseUrl      | string            |backend openAPI baseUrl                                   |
| query        | object(Parameter) |backend openAPI query parameters                          |
| body         | object(Parameter) |backend openAPI body parameters                           |
| pathVariable | array(object)     |backend openAPI path parameters                           |
| domain       | string            |client domain that bundle each backend openAPI components |
| rateLimit    | integer           |req/sec rateLimit                                         |

**Parameter**

| Property | Type | Description         |
| --- | --- |---------------------|
| required | array (object) | required parameters |
| optional | array (object) | optional parameters |

**Response**

HTTP 201 Created

| Property     | Type              | Description                                               |
|--------------|-------------------|-----------------------------------------------------------|
| id           | objectId          | routing id                                                |
| path         | string            | backend openAPI path                                      |
| method       | string            | backend openAPI method                                    |
| baseUrl      | string            | backend openAPI baseUrl                                   |
| query        | object(Parameter) | backend openAPI query parameters                          |
| body         | object(Parameter) | backend openAPI body parameters                           |
| pathVariable | array(object)     | backend openAPI path parameters                           |
| domain       | string            | client domain that bundle each backend openAPI components |
| rateLimit    | integer           | req/sec rateLimit                                         |

**Error code**

| Status code | Error message               | Description                             |
|-------------|-----------------------------|-----------------------------------------|
| 400         | Incorrect parameter request | there are errors in request parameters. |
| 409         | Invalid client value        | client api is already existed.          |
| 500         | System Error                | error is occurred in server.            |

### ```read```

GET /routings

**HTTP Request**

```
GET http://localhost:8080/routings
```

**Response**

HTTP 200 OK

| Property | Type            | Description |
| --- |-----------------| --- |
| routings | array (Routing) | - |

**Routing**

| Property     | Type              | Description                                               |
|--------------|-------------------|-----------------------------------------------------------|
| id           | objectId          | routing id                                                |
| path         | string            | backend openAPI path                                      |
| method       | string            | backend openAPI method                                    |
| baseUrl      | string            | backend openAPI baseUrl                                   |
| query        | object(Parameter) | backend openAPI query parameters                          |
| body         | object(Parameter) | backend openAPI body parameters                           |
| pathVariable | array(object)     | backend openAPI path parameters                           |
| domain       | string            | client domain that bundle each backend openAPI components |
| rateLimit    | integer           | req/sec rateLimit                                         |


**Error code**

| Status code | Error message               | Description                             |
|-------------|-----------------------------|-----------------------------------------|
| 500         | System Error                | error is occurred in server.            |

### ```update```

PUT /routings/{routingId}

**HTTP Request**

```
PUT http://localhost:8080/routings/{routingId}
```

**Path parameters**

| Parameter | Type | Description |
| --- | --- |-------------|
| routingId | string | routing id  |

**Header Parameters**

| Header | Type | Description |
| --- | --- | --- |
| Content-Type | string | application/json |

**Response**

HTTP 200 OK

| Property | Type | Description |
| --- | --- | --- |
| id           | objectId          | routing id                                                |
| path         | string            | backend openAPI path                                      |
| method       | string            | backend openAPI method                                    |
| baseUrl      | string            | backend openAPI baseUrl                                   |
| query        | object(Parameter) | backend openAPI query parameters                          |
| body         | object(Parameter) | backend openAPI body parameters                           |
| pathVariable | array(object)     | backend openAPI path parameters                           |
| domain       | string            | client domain that bundle each backend openAPI components |
| rateLimit    | integer           | req/sec rateLimit                                         |

**Error code**

| Status code | Error message               | Description                             |
|-------------|-----------------------------|-----------------------------------------|
| 400         | Incorrect parameter request | there are errors in request parameters. |
| 404         | Unfound resources           | parameter(id) is invalid.               |
| 500         | System Error                | error is occurred in server.            |

## ```delete```

DELETE /routings/{routingId}

**HTTP Request**

```
DELETE http://localhost:8080/routings/{routingId}
```

**Path parameters**

| Parameter | Type | Description |
| --- | --- |-------------|
| routingId | string | routing id  |

**Response**

HTTP 204 No Content

**Error code**

| Status code | Error message               | Description                             |
|-------------|-----------------------------|-----------------------------------------|
| 404         | Unfound resources           | parameter(id) is invalid.               |
| 500         | System Error                | error is occurred in server.            |

## Model

routing data is stored in ```mongodb``` repository.

| Name         | Description                                               |
|--------------|-----------------------------------------------------------|
| id           | mongodb ObjectId                                          |
| path         | backend openAPI path                                      |
| method       | backend openAPI method                                    |
| baseUrl      | backend openAPI baseUrl                                   |
| query        | backend openAPI query parameters                          |
| body         | backend openAPI body parameters                           |
| pathVariable | backend openAPI path parameters                           |
| domain       | client domain that bundle each backend openAPI components |
| rateLimit    | req/sec rateLimit                                         |

- required
  - path, method, baseUrl, domain, rateLimit
- optional
  - query, body, pathVariable
    - query, body is divided into **required**, **optional**

example
```
{
    "path": "/boards",
    "method": "GET",
    "domain": "board-service",
    "rateLimit": 100000,
    "baseUrl": "https://www.worksapis.com/v1.0",
    "body": {
        "required": [
            {
                "type": "string",
                "name": "boardName",
                "validateType": "length",
                "minLength": 1,
                "maxLength": 60
            }
        ],
        "optional": [
            {
                "type": "string",
                "name": "description",
                "validType": "length",
                "minLength": 0,
                "maxLength": 300
            }
        ]
    }
}
```

## Redis

real-time update is applied with redis pub/sub.

There are three channels that is ready to receive messages.

- TOPIC_1 : **```routing-register```**

    new routing information is sent via this channel when register event is occurred.

- TOPIC_2 : **```routing-update```**
    
    prev routing information, updated routing information is sent via this channel when update event (PUT, PATCH method) is occurred.

- TOPIC_3 : **```routing-delete```**
    
    path, domain, method is sent via this channel when delete event is occurred.

