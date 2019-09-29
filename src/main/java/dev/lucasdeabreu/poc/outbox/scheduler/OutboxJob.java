package dev.lucasdeabreu.poc.outbox.scheduler;

import dev.lucasdeabreu.poc.outbox.outbox.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
@RequiredArgsConstructor
public class OutboxJob implements Job {

    private final OutboxService outboxService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Executing an outbox job");
        outboxService.sendOutboxMessagesToBroker();
    }

}
