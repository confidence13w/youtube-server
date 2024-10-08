package com.server.youtube.repo;

import com.server.youtube.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscribeDAO extends JpaRepository<Subscribe, Integer> {

    // 해당 채널의 구독자 수
    // SELECT count(*) FROM subscribe WHERE channel_code = :channelCode
    @Query(value="SELECT count(*) FROM subscribe WHERE channel_code = :channelCode", nativeQuery = true)
    int count(@Param("channelCode") int channelCode);

    // 로그인한 사람의 해당 채널의 구독 체크 여부
    @Query(value="SELECT * FROM subscribe WHERE id = :id AND channel_code = :channelCode", nativeQuery = true)
    Subscribe check(@Param("id") String id, @Param("channelCode") int channelCode);
}