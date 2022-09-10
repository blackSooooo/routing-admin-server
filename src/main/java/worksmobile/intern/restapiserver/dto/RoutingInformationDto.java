package worksmobile.intern.restapiserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class RoutingInformationDto {
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
