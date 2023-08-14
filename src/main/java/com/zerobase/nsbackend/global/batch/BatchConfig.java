package com.zerobase.nsbackend.global.batch;

import com.zerobase.nsbackend.global.batch.tasklet.DailyPointTasklet;
import com.zerobase.nsbackend.global.batch.tasklet.WeeklyPointTasklet;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;


@RequiredArgsConstructor
@Configuration
public class BatchConfig{

    private final WeeklyPointTasklet weeklyPointTasklet;
    private final DailyPointTasklet dailyPointTasklet;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    //Batch Job 생성
    @Bean
    public Job weeklyJob(){
        return jobBuilderFactory.get("weeklyJob")
            .start(weeklyStep())
            .build();
    }

    //Batch Step 생성
    @Bean
    @JobScope //Job 실행시점에 Bean이 생성됨
    public Step weeklyStep() {
        return stepBuilderFactory.get("weeklyStep")
            .tasklet(weeklyPointTasklet)
            .build();
    }

    //Batch Job 생성
    @Bean
    public Job dailyJob(){
        return jobBuilderFactory.get("dailyJob")
            .start(dailyStep())
            .build();
    }

    //Batch Step 생성
    @Bean
    @JobScope //Job 실행시점에 Bean이 생성됨
    public Step dailyStep() {
        return stepBuilderFactory.get("dailyStep")
            .tasklet(dailyPointTasklet)
            .build();
    }
}
