package worksmobile.intern.restapiserver.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "routings")
@Setter
public class RoutingInformation {
    @Id
    private String id;
    private String path;
    private String method;
    private String domain;
    private int rateLimit;
    private String baseUrl;
    private Map<String, List<Map<String, Object>>> query;
    private Map<String, List<Map<String, Object>>> body;
    private List<Map<String, Object>> pathVariable;
}