package worksmobile.intern.restapiserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import worksmobile.intern.restapiserver.dto.RoutingInformationDto;
import worksmobile.intern.restapiserver.service.RoutingService;

import java.util.List;

@RestController
@RequestMapping("/routings")
@RequiredArgsConstructor
public class RoutingController {

    private final RoutingService routingService;

    @PostMapping
    public RoutingInformationDto register(@RequestBody RoutingInformationDto routingInformationDto) {
        routingService.validateDuplicateClientApi(routingInformationDto);
        return routingService.register(routingInformationDto);
    }

    @GetMapping
    public List<RoutingInformationDto> list() {
        return routingService.findAll();
    }

    @PutMapping("/{routingId}")
    public RoutingInformationDto update(@RequestBody RoutingInformationDto routingInformationDto, @PathVariable String routingId) {
        routingService.validateDuplicateClientApi(routingInformationDto);
        return routingService.updateById(routingInformationDto, routingId);
    }

    @PatchMapping("/{routingId}")
    public RoutingInformationDto patch(@RequestBody RoutingInformationDto routingInformationDto, @PathVariable String routingId) {
        return routingService.patchById(routingInformationDto, routingId);
    }

    @DeleteMapping("/{routingId}")
    public void delete(@PathVariable String routingId) {
        routingService.deleteById(routingId);
    }
}
