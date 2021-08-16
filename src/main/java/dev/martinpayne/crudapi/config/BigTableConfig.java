package dev.martinpayne.crudapi.config;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class BigTableConfig {

    @Bean
    public String tableId(@Value("${tableId}") String tableId) {
        return tableId;
    }

    @Bean
    public BigtableDataClient bigtableDataClient(
            @Value("${projectId}") String projectId,
            @Value("${instanceId}") String instanceId
    ) throws IOException {
        BigtableDataSettings settings = BigtableDataSettings.newBuilder()
                .setProjectId(projectId)
                .setInstanceId(instanceId)
                .build();

        return BigtableDataClient.create(settings);
    }
}
