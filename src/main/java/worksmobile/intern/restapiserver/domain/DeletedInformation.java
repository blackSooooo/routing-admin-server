package worksmobile.intern.restapiserver.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class DeletedInformation {
    private String path;
    private String domain;
    private String method;
}
