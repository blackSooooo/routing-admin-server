package worksmobile.intern.restapiserver.service;

import worksmobile.intern.restapiserver.dto.RoutingInformationDto;

import java.util.List;

public interface RoutingService {
    public RoutingInformationDto register(RoutingInformationDto routingInformationDto);

    public void validateDuplicateClientApi(RoutingInformationDto routingInformationDto);

    public List<RoutingInformationDto> findAll();

    public RoutingInformationDto updateById(RoutingInformationDto routingInformationDto, String id);

    public RoutingInformationDto patchById(RoutingInformationDto routingInformationDto, String id);

    public void deleteById(String id);
}
