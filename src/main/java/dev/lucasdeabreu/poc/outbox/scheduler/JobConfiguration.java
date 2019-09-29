package dev.lucasdeabreu.poc.outbox.scheduler;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration {

    @Bean
    public JobDetail outboxJobDetails() {
        return JobBuilder.newJob(OutboxJob.class)
                .withIdentity("outboxJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger outboxTrigger(JobDetail outboxJobDetails) {
        return TriggerBuilder.newTrigger()
                .forJob(outboxJobDetails)
                .withIdentity("outboxTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/1 * * * * ? *"))
                .build();
    }

}
