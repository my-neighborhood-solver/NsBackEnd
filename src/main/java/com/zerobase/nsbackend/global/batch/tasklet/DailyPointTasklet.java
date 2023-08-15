package com.zerobase.nsbackend.global.batch.tasklet;

import static com.zerobase.nsbackend.member.type.NotificationType.InterestBoard_DeadLine;

import com.zerobase.nsbackend.errand.domain.repository.ErrandRepository;
import com.zerobase.nsbackend.errand.domain.vo.ErrandStatus;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.repository.InterestBoardRepository;
import com.zerobase.nsbackend.member.service.SubscribeService;
import java.time.LocalDate;
import java.util.List;
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
public class DailyPointTasklet implements Tasklet {

    private final ErrandRepository errandRepository;
    private final InterestBoardRepository interestBoardRepository;
    private final SubscribeService subscribeService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext){

        log.info(contribution.toString());
        log.info(chunkContext.toString());
        log.info(">>>>> Errand Deadline");

        LocalDate now = LocalDate.now();

        List<Member> memberList = interestBoardRepository.findMemberByErrandDeadLineAndStatus(
            now.plusDays(1), ErrandStatus.REQUEST);

        for(Member member : memberList){
            subscribeService.send(member, InterestBoard_DeadLine
                , InterestBoard_DeadLine.getDescription());
        }

        return RepeatStatus.FINISHED;
    }
}
