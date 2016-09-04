/*
 * Copyright 2016 Phaneesh Nagaraja <phaneesh.n@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.dropwizard.rmq.healthcheck;

import com.rabbitmq.client.Connection;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.rmq.healthcheck.healtcheck.RabbitHealthCheck;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author phaneesh
 */
@Slf4j
public abstract class RabbitHealthCheckBundle<T extends Configuration> implements ConfiguredBundle<T> {

    public abstract Connection withConnection();

    public abstract List<String> withQueues();

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        environment.healthChecks().register("rmq",
                RabbitHealthCheck.builder()
                        .connection(withConnection())
                        .queues(withQueues())
                .build()
        );
    }
}
