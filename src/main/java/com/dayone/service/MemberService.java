package com.dayone.service;

import com.dayone.model.Auth;
import com.dayone.model.constants.MemberEntity;
import com.dayone.persist.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    //스프링 시큐리티에서 지원하는 기능을 활용하기 위한 implements

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException("Could not find user -> " + username)
        );
    }


    public MemberEntity register(Auth.SignUp member){
        boolean exist = this.memberRepository.existsByUsername(member.getUsername());
        if(exist){
            throw new RuntimeException("이미 사용 중인 아이디입니다.");
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        return this.memberRepository.save(member.toEntity());
    }


    public MemberEntity authenticate(Auth.SignIn member){
        var user = this.memberRepository.findByUsername(member.getUsername()).orElseThrow(
            () -> new RuntimeException("존재하지 않는 ID 입니다.")
        );

        if(!this.passwordEncoder.matches(member.getPassword(), user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }


}

















