# FairCom Bottle Shop Database-as-a-(Micro)Service

The FairCom Bottle Shop is a microservice with a GraphQL endpoint. It uses an embedded database, FairCom DB, accessed via a public API rather than SQL. In addition, it consumes messages from an MQTT broker.

## Prerequisites

The FairCom Bottle Shop microservice requires a local MQTT broker running on port 8080.

It has been tested with FairCom Edge. To get started:

1. Download https://www.faircom.com/download-faircomedge
2. Unzip (extract all)
3. Run (server/faircom.exe | server/faircom.sh)

Windows
```shell script
start server/faircom.exe
```

Linux
```shell script
./server/faircom.sh
```

## Starting

```shell script
./mvnw compile quarkus:dev
```

Open the GraphiQL web UI http://localhost:8090/q/graphql-ui/

> **_NOTE:_** The microservice runs on port 8090 rather than 8080 because it requires an MQTT broker running on port 8080.

> **_NOTE:_** The data files are stored in the target/data directory. If it is missing, the embedded database schema will be created and sample data loaded.

## Validating

Open the GraphiQL web UI http://localhost:8090/q/graphql-ui/

Get all breweries

```gql
{
  getBreweries {
    id
    name
    city
    state
  }
}
```

Get all beers in coolers

```gql
{
  getCoolerBeers {
    coolerId
    beerId
    quantity
  }
}
```

Publish an MQTT message

FairCom Edge has been tested with MQTT X, a desktop application.

MQTT X can be downloaded here: https://mqttx.app/

```json
{
  "coolerId": 1,
  "beerId": 1,
  "change": "+"
}
```

Get the beers in coolers (again)

```gql
{
  getCoolerBeers {
    coolerId
    beerId
    quantity
  }
}
```