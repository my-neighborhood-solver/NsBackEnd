package com.zerobase.nsbackend.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.global.fileUpload.StoreFileToAWS;
import com.zerobase.nsbackend.global.fileUpload.UploadFile;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.MemberAddress;
import com.zerobase.nsbackend.member.domain.MemberHashtag;
import com.zerobase.nsbackend.member.dto.GetUserResponse;
import com.zerobase.nsbackend.member.dto.HashtagResponse;
import com.zerobase.nsbackend.member.dto.PutUserAddressRequest;
import com.zerobase.nsbackend.member.dto.PutUserNicknameRequest;
import com.zerobase.nsbackend.member.repository.MemberAddressRepository;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    MemberAddressRepository memberAddressRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    StoreFileToAWS storeFileToAWS;
    private final MemberAddress memberAddress = MemberAddress.builder()
        .latitude(0f)
        .longitude(1f)
        .streetNameAddress("street").build();
    private final Member member = Member.builder()
        .nickname("test")
        .email("aa@naver.com")
        .password("encodePassword")
        .memberAddress(memberAddress)
        .profileImage(null)
        .isSocialLogin(false)
        .build();

    @Test
    void getUserInfo() {
        //given
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(member));
        //when
        GetUserResponse userInfo = memberService.getUserInfo("aa@naver.com");
        //then
        assertEquals("aa@naver.com",userInfo.getEmail());
        assertEquals(1,userInfo.getLongitude());
        assertEquals(0,userInfo.getLatitude());
        assertEquals("street",userInfo.getStreetNameAddress());
    }

    @Test
    void updateUserNickname() {
        //given
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(member));
        PutUserNicknameRequest request = new PutUserNicknameRequest();
        request.setNickname("update");
        //when
        Member updated = memberService.updateUserNickname(request, "aa@naver.com");
        //then
        assertEquals("update",updated.getNickname());
        assert member != null;
        assertEquals(member.getEmail(),updated.getEmail());
    }

    @Test
    void updateUserImg(){
        //given
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(member));
        given(storeFileToAWS.storeFile(any()))
            .willReturn(UploadFile.of("testUploadName","testStoreName"));
        MockMultipartFile request
            = makeMultipartFile();
        given(storeFileToAWS.responseFileUrl(any()))
            .willReturn("testUrl");
        //when
        assert member != null;
        Member updated = memberService.updateUserImg(request, member.getEmail());
        //then
        assertEquals("testUrl",updated.getProfileImage());
        assertEquals(member.getEmail(),updated.getEmail());
    }
    private static MockMultipartFile makeMultipartFile() {
        return new MockMultipartFile(
            "file",
            "hello.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Hello, World!".getBytes()
        );
    }

    @Test
    void updateUserAddress() {
        //given
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(member));
        PutUserAddressRequest request = PutUserAddressRequest.builder()
            .latitude(12.123f)
            .longitude(32.123f)
            .streetNameAddress("updateStreet").build();
        //when
        assert member != null;
        MemberAddress updated = memberService.updateUserAddress(request, member.getEmail());
        //then
        assertEquals(12.123f, updated.getLatitude());
        assertEquals(32.123f, updated.getLongitude());
        assertEquals("updateStreet",updated.getStreetNameAddress());
    }

    @Test
    void deleteUser() {
        //given
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(member));
        //when
        assert member != null;
        Member deleteUser = memberService.deleteUser(member.getEmail());
        //then
        assertTrue(deleteUser.isDeleted());
    }

    @Test
    void getHashtag(){
        //given
        HashSet<MemberHashtag> hashSet = new HashSet<>();
        hashSet.add(MemberHashtag.of("test"));
        Member build = Member.builder()
            .nickname("test")
            .email("aa@naver.com")
            .hashtags(hashSet)
            .build();
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(build));

        //when
        assert build != null;
        HashtagResponse hashtag = memberService.getHashtag(build.getEmail());

        //then
        assertEquals("test",hashtag.getHashtag().get(0));
    }

    @Test
    void updateHashtag(){
        //given
        Member build = Member.builder()
            .nickname("test")
            .email("aa@naver.com")
            .hashtags(new HashSet<>())
            .build();
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(build));
        //when
        assert build != null;
        HashtagResponse hashtag = memberService.updateHashtag(build.getEmail(), "test");
        //then
        assertEquals("test",hashtag.getHashtag().get(0));
    }
    @DisplayName("해시태그 추가 실패 - 추가가능한 해시태그가 없음")
    @Test
    void updateHashtag_full(){
        //given
        HashSet<MemberHashtag> hashSet = new HashSet<>();
        hashSet.add(MemberHashtag.of("test"));
        hashSet.add(MemberHashtag.of("test2"));
        hashSet.add(MemberHashtag.of("test3"));
        Member build = Member.builder()
            .nickname("test")
            .email("aa@naver.com")
            .hashtags(hashSet)
            .build();
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(build));
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> {
                assert build != null;
                memberService.updateHashtag(build.getEmail(), "test4");
            });
        //then
        assertEquals(ErrorCode.HASHTAG_IS_FULL.getDescription(),exception.getMessage());
    }
    @Test
    void deleteHashtag(){
        //given
        HashSet<MemberHashtag> hashSet = new HashSet<>();
        hashSet.add(MemberHashtag.of("test"));
        Member build = Member.builder()
            .nickname("test")
            .email("aa@naver.com")
            .hashtags(hashSet)
            .build();
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(build));
        //when
        assert build != null;
        HashtagResponse hashtag = memberService.deleteHashtag(build.getEmail(), "test");
        //then
        assertEquals(0,hashtag.getHashtag().size());
    }

    @DisplayName("해시태그 삭제 실패 - 일치하는 해시태그가 없음")
    @Test
    void deleteHashtag_noExist(){
        //given
        HashSet<MemberHashtag> hashSet = new HashSet<>();
        hashSet.add(MemberHashtag.of("test"));
        Member build = Member.builder()
            .nickname("test")
            .email("aa@naver.com")
            .hashtags(hashSet)
            .build();
        given(memberRepository.findByEmail(any()))
            .willReturn(Optional.ofNullable(build));
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> {
                assert build != null;
                memberService.deleteHashtag(build.getEmail(), "test2");
            });
        //then
        assertEquals(ErrorCode.NO_EXIST_HASHTAG.getDescription(),exception.getMessage());
    }


}