/*
 * Copyright (c) 2016 Phaneesh Nagaraja <phaneesh.n@gmail.com>.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package io.dropwizard.rmq.healthcheck.healtcheck;

import com.codahale.metrics.health.HealthCheck;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.List;

/**
 * @author phaneesh
 */
@Singleton
@Data
@Slf4j
public class RabbitHealthCheck extends HealthCheck {

    public Connection connection;

    private List<String> queues;

    @Builder
    public RabbitHealthCheck(Connection connection, @Singular List<String> queues) {
        this.connection = connection;
        this.queues = queues;
    }

    @Override
    protected Result check() throws Exception {
        if (connection == null) {
            return Result.unhealthy("No RMQ connection available");
        }
        if (!connection.isOpen()) {
            return Result.unhealthy("RMQ connection is not open");
        }
        if (queues != null && !queues.isEmpty()) {
            Channel channel = null;
            try {
                channel = connection.createChannel();
                if(!channel.isOpen()) {
                    return Result.unhealthy("Cannot open channel with RabbitMQ");
                }
                boolean isOk = true;
                for(String q : queues) {
                    isOk = channel.consumerCount(q) > 0;
                }
                if(!isOk) {
                    return Result.unhealthy("RMQ is having issues! Please attend to it now!");
                }
            } catch (Exception e) {
                log.error("Rabbit health check Error: ", e.getMessage());
                return Result.unhealthy(e.getMessage());
            } finally {
                if(channel != null) {
                    channel.close();
                }
            }
        }
        return Result.healthy();
    }
}
