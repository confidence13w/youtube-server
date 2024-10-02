package com.server.youtube.service;

import com.server.youtube.domain.Member;
import com.server.youtube.repo.MemberDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberDAO dao;

    @Autowired
    private PasswordEncoder bcpe;

    public void signup(Member vo) {
        vo.setPassword(bcpe.encode(vo.getPassword()));
        dao.save(vo);
    }

    public Member login(String id, String password) {
        Member member = dao.findById(id).orElseThrow(() -> new UsernameNotFoundException("Member Not Found"));
        if(bcpe.matches(password, member.getPassword())) {
            return member;
        }
        return null;
    }
}