package com.server.youtube.service;

import com.server.youtube.domain.Member;
import com.server.youtube.domain.Subscribe;
import com.server.youtube.repo.SubscribeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService {

    @Autowired
    private SubscribeDAO dao;

    // 사용자 정보 가져오기 - ID
    private String getId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null && auth.isAuthenticated()) {
            Member member = (Member) auth.getPrincipal();
            return member.getId();
        }
        return null;
    }

    // 구독 추가
    public void create(Subscribe vo) {
        vo.setId(getId());
        dao.save(vo);
    }

    // 구독 취소
    public void remove(int subCode) {
        dao.deleteById(subCode);
    }

    // 해당 채널의 구독자수 가져오기
    public int count(int channelCode) {
        return dao.count(channelCode);
    }

    // 로그인한 사람의 해당 채널의 구독 체크 여부
    public Subscribe check(int channelCode) {
        return dao.check(getId(), channelCode);
    }

}