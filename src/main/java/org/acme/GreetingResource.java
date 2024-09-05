package org.acme;

import io.quarkiverse.jberet.runtime.QuarkusJobOperator;
import jakarta.batch.runtime.JobInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jberet.runtime.JobExecutionImpl;
import org.jberet.runtime.JobInstanceImpl;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Path("/hello")
public class GreetingResource {

    @Inject
    QuarkusJobOperator jobOperator;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        jobOperator.start("job",new Properties());
        return "Hello";
    }
    @GET
    @Path("/wait")
    @Produces(MediaType.TEXT_PLAIN)
    public String waitHello() throws InterruptedException {
        long id = jobOperator.start("job", new Properties());
        JobExecutionImpl jobExecution = (JobExecutionImpl) jobOperator.getJobExecution(id);
        jobExecution.awaitTermination(0, TimeUnit.MINUTES);
        return "Hello "+jobExecution.getBatchStatus().name();
    }
}
