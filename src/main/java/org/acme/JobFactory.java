package org.acme;

import io.quarkus.logging.Log;
import jakarta.batch.api.AbstractBatchlet;
import jakarta.batch.runtime.BatchStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.jberet.job.model.Job;
import org.jberet.job.model.JobBuilder;
import org.jberet.job.model.StepBuilder;

import java.util.List;
import java.util.Properties;

@ApplicationScoped
public class JobFactory {
    @Produces
    @Named
    public Job job() {
        return new JobBuilder("job")
                .step(new StepBuilder("step").batchlet("batchlet", new Properties()).build())
                .build();
    }

    @Named("batchlet")
    @ApplicationScoped
    public static class Batchy extends AbstractBatchlet {
        @Override
        @Transactional
        public String process() throws Exception {
            List<MyEntity> list = MyEntity.listAll();
            Log.info("List: " + list);
            return BatchStatus.COMPLETED.name();
        }
    }
}
