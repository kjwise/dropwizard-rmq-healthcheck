# Dropwizard RMQ Healthcheck Bundle [![Travis build status](https://travis-ci.org/phaneesh/dropwizard-rmq-healthcheck.svg?branch=master)](https://travis-ci.org/phaneesh/dropwizard-rmq-healthcheck)

This bundle adds a RabbitMQ healthcheck
This bundle compiles only on Java 8.
 
## Usage
This makes it easier perform healthchecks on RabbitMQ
 
### Build instructions
  - Clone the source:

        git clone github.com/phaneesh/dropwizard-rmq-healthcheck

  - Build

        mvn install

### Maven Dependency
* Use the following maven dependency:
```
<dependency>
    <groupId>io.dropwizard</groupId>
    <artifactId>dropwizard-rmq-healthcheck</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Using RMQ Healthcheck Bundle

#### Bootstrap
```java
    @Override
    public void initialize(final Bootstrap...) {
        bootstrap.addBundle(new RabbitHealthCheckBundle() {
            
            public RabbitConfig withConnection() {
                return <rmq connetion>;
            }
            
            public List<String> withQueues() {
                return <list of queues>;
            }
        });
    }
```