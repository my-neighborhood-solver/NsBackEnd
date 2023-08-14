package com.zerobase.nsbackend.global.batch.tasklet;

import com.zerobase.nsbackend.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@StepScope // Step 시점에 Bean이 생성
@Slf4j
public class WeeklyPointTasklet implements Tasklet {

    private final MemberRepository memberRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){

        log.info(contribution.toString());
        log.info(chunkContext.toString());
        log.info(">>>>> Delete User");

        memberRepository.deleteAllByIdInQuery();

        return RepeatStatus.FINISHED;
    }
}
