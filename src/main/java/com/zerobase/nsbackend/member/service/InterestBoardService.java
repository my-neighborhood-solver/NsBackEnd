package com.zerobase.nsbackend.member.service;


import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.repository.ErrandRepository;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.member.domain.InterestBoard;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.dto.Interests.interestBoardResponse;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestBoardService {
    private final ErrandRepository errandRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public List<interestBoardResponse> addInterestBoard(Long errandId, Member member){
        List<InterestBoard> interestBoardList = member.getInterestBoards();
        Errand errand = this.errandRepository.findById(errandId)
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ERRAND_NOT_FOUND.getDescription()));
        if(interestBoardList.contains(new InterestBoard(member,errand))){
            throw new IllegalArgumentException(ErrorCode.EXIST_INTEREST_BOARD.getDescription());
        }
        InterestBoard interestBoard = new InterestBoard(member,errand);
        interestBoardList.add(interestBoard);
        member.updateInterestBoard(interestBoardList);
        memberRepository.save(member);
        return convertInterestBoardResponse(member.getInterestBoards());
    }
    public List<interestBoardResponse> getAllInterestBoard(Member member){
        return convertInterestBoardResponse(member.getInterestBoards());
    }

    @Transactional
    public List<interestBoardResponse> deleteInterestBoard(Long errandId, Member member){
        List<InterestBoard> interestBoardList = member.getInterestBoards();
        Errand errand = this.errandRepository.findById(errandId)
            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ERRAND_NOT_FOUND.getDescription()));
        if(!interestBoardList.contains(new InterestBoard(member,errand))){
            throw new IllegalArgumentException(ErrorCode.NO_EXIST_INTEREST_BOARD.getDescription());
        }
        member.deleteInterestBoard(member,errand);
        memberRepository.save(member);
        return convertInterestBoardResponse(member.getInterestBoards());
    }

    private List<interestBoardResponse> convertInterestBoardResponse(List<InterestBoard> interestBoards){
        List<interestBoardResponse> interestBoardResponseList = new ArrayList<>();
        for(InterestBoard interestBoard : interestBoards){
            interestBoardResponse build = interestBoardResponse.builder()
                .errandId(interestBoard.getErrand().getId())
                .errandTitle(interestBoard.getErrand().getTitle())
                .build();
            interestBoardResponseList.add(build);
        }
        return interestBoardResponseList;
    }
}
