package worksmobile.intern.restapiserver.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import worksmobile.intern.restapiserver.domain.DeletedInformation;
import worksmobile.intern.restapiserver.domain.RoutingInformation;
import worksmobile.intern.restapiserver.domain.UpdatedInformation;
import worksmobile.intern.restapiserver.dto.RoutingInformationDto;
import worksmobile.intern.restapiserver.error.Error;
import worksmobile.intern.restapiserver.error.ErrorResponse;
import worksmobile.intern.restapiserver.exception.ResponseException;
import worksmobile.intern.restapiserver.repository.RoutingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutingServiceImpl implements RoutingService {

    private final RoutingRepository routingRepository;
    private final ModelMapper modelMapper;
    private final RedisPublisher redisPublisher;
    @Override
    public RoutingInformationDto register(RoutingInformationDto routingInformationDto) {
        RoutingInformation routingInformation = modelMapper.map(routingInformationDto, RoutingInformation.class);

        RoutingInformation savedRoutingInformation = routingRepository.save(routingInformation);

        redisPublisher.publish(savedRoutingInformation);

        RoutingInformationDto savedRoutingInformationDto = modelMapper.map(savedRoutingInformation, RoutingInformationDto.class);

        return savedRoutingInformationDto;
    }

    @Override
    public void validateDuplicateClientApi(RoutingInformationDto routingInformationDto) {
        String path = routingInformationDto.getPath();
        String method = routingInformationDto.getMethod();
        String domain = routingInformationDto.getDomain();
        RoutingInformation foundRoutingInformation = routingRepository.findByPathAndMethodAndDomain(path, method, domain);
        if (foundRoutingInformation != null) {
            throw new ResponseException(new ErrorResponse(Error.RESOURCE_CONFLICT));
        }
    }

    @Override
    public List<RoutingInformationDto> findAll() {
        List<RoutingInformation> foundRoutingInformation = routingRepository.findAll();
        return foundRoutingInformation.stream()
                .map(routingInformation -> modelMapper.map(routingInformation, RoutingInformationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoutingInformationDto updateById(RoutingInformationDto routingInformationDto, String id) {
        UpdatedInformation updatedInformation = new UpdatedInformation();
        RoutingInformation savedRoutingInformation = routingRepository.findById(id)
                .map(routing -> {
                    updatedInformation.setPrevRoutingInformation(routing);
                    routingInformationDto.setId(routing.getId());
                    RoutingInformation routingInformation = modelMapper.map(routingInformationDto, RoutingInformation.class);
                    return routingRepository.save(routingInformation);
                }).orElseGet(() -> {
                    RoutingInformation newRoutingInformation = modelMapper.map(routingInformationDto, RoutingInformation.class);
                    return routingRepository.save(newRoutingInformation);
                });
        RoutingInformationDto savedRoutingInformationDto = modelMapper.map(savedRoutingInformation, RoutingInformationDto.class);
        updatedInformation.setUpdatedRoutingInformation(savedRoutingInformation);
        redisPublisher.publish(updatedInformation);
        return savedRoutingInformationDto;
    }

    @Override
    public RoutingInformationDto patchById(RoutingInformationDto routingInformationDto, String id) {
        UpdatedInformation updatedInformation = new UpdatedInformation();
        Optional<RoutingInformation> foundedRoutingInformation = routingRepository.findById(id);
        if (foundedRoutingInformation.isPresent()) {
            RoutingInformation prevRoutingInformation = foundedRoutingInformation.get();
            updatedInformation.setPrevRoutingInformation(prevRoutingInformation);

            if (routingInformationDto.getPath() == null) {
                routingInformationDto.setPath(prevRoutingInformation.getPath());
            }
            if (routingInformationDto.getMethod() == null) {
                routingInformationDto.setMethod(prevRoutingInformation.getMethod());
            }
            if (routingInformationDto.getDomain() == null) {
                routingInformationDto.setDomain(prevRoutingInformation.getDomain());
            }
            if (routingInformationDto.getRateLimit() == 0) {
                routingInformationDto.setRateLimit(prevRoutingInformation.getRateLimit());
            }
            if (routingInformationDto.getBaseUrl() == null) {
                routingInformationDto.setBaseUrl(prevRoutingInformation.getBaseUrl());
            }
            if (routingInformationDto.getQuery() == null) {
                routingInformationDto.setQuery(prevRoutingInformation.getQuery());
            }
            if (routingInformationDto.getBody() == null) {
                routingInformationDto.setBody(prevRoutingInformation.getBody());
            }
            if (routingInformationDto.getPathVariable() == null) {
                routingInformationDto.setPathVariable(prevRoutingInformation.getPathVariable());
            }

            routingInformationDto.setId(prevRoutingInformation.getId());

            RoutingInformation routingInformation = modelMapper.map(routingInformationDto, RoutingInformation.class);
            RoutingInformation updatedRoutingInformation = routingRepository.save(routingInformation);
            updatedInformation.setUpdatedRoutingInformation(updatedRoutingInformation);

            RoutingInformationDto savedRoutingInformationDto = modelMapper.map(updatedRoutingInformation, RoutingInformationDto.class);

            redisPublisher.publish(updatedInformation);

            return savedRoutingInformationDto;
        } else {
            throw new ResponseException(new ErrorResponse(Error.NOT_FOUND));
        }
    }

    @Override
    public void deleteById(String id) {
        Optional<RoutingInformation> deletedRoutingInformation = routingRepository.findById(id);
        if (deletedRoutingInformation.isPresent()) {
            routingRepository.deleteById(id);
            RoutingInformation routingInformation = deletedRoutingInformation.get();
            DeletedInformation deletedInformation = DeletedInformation.builder()
                    .path(routingInformation.getPath())
                    .domain(routingInformation.getDomain())
                    .method(routingInformation.getMethod())
                    .build();
            redisPublisher.publish(deletedInformation);
        } else {
            throw new ResponseException(new ErrorResponse(Error.NOT_FOUND));
        }
    }
}
