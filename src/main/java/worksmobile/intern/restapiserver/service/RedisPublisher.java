package worksmobile.intern.restapiserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import worksmobile.intern.restapiserver.domain.DeletedInformation;
import worksmobile.intern.restapiserver.domain.RoutingInformation;
import worksmobile.intern.restapiserver.domain.UpdatedInformation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String TOPIC_1 = "routing-register";
    private static final String TOPIC_2 = "routing-update";
    private static final String TOPIC_3 = "routing-delete";

    public void publish(RoutingInformation routingInformation) {
        redisTemplate.convertAndSend(TOPIC_1, routingInformation);
    }

    public void publish(UpdatedInformation updatedInformation) {
        redisTemplate.convertAndSend(TOPIC_2, updatedInformation);
    }

    public void publish(DeletedInformation deletedInformation) {
        redisTemplate.convertAndSend(TOPIC_3, deletedInformation);
    }
}
