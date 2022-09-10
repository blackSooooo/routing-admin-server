package worksmobile.intern.restapiserver.domain;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UpdatedInformation {
    RoutingInformation prevRoutingInformation;
    RoutingInformation updatedRoutingInformation;
}
