package worksmobile.intern.restapiserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import worksmobile.intern.restapiserver.domain.RoutingInformation;

public interface RoutingRepository extends MongoRepository<RoutingInformation, String> {
    @Query("{'path': ?0, 'method': ?1, 'domain': ?2}")
    RoutingInformation findByPathAndMethodAndDomain(String path, String method, String domain);
}
