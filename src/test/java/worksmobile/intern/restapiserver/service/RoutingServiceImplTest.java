package worksmobile.intern.restapiserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import worksmobile.intern.restapiserver.domain.RoutingInformation;
import worksmobile.intern.restapiserver.dto.RoutingInformationDto;
import worksmobile.intern.restapiserver.error.ErrorResponse;
import worksmobile.intern.restapiserver.exception.ResponseException;
import worksmobile.intern.restapiserver.repository.RoutingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoutingServiceImplTest {
    @Mock
    RoutingRepository routingRepository;
    @Mock
    RedisPublisher redisPublisher;
    @Spy
    ModelMapper modelMapper;
    @InjectMocks
    RoutingServiceImpl routingService;

    RoutingInformationDto routingInformationDto;
    RoutingInformation routingInformation;
    String id = "1";

    @BeforeEach
    void setup() {
        routingInformationDto = RoutingInformationDto.builder()
                .id(id)
                .path("/blog.json")
                .method("GET")
                .rateLimit(5)
                .baseUrl("https://openapi.naver.com")
                .domain("blog-service")
                .build();

        routingInformation = RoutingInformation.builder()
                .id(id)
                .path("/blog.json")
                .method("GET")
                .rateLimit(5)
                .baseUrl("https://openapi.naver.com")
                .domain("blog-service")
                .build();
    }

    @Test
    void 라우팅정보_등록_테스트() {
        when(routingRepository.save(any())).thenReturn(routingInformation);

        RoutingInformationDto registeredRoutingInformation = routingService.register(routingInformationDto);

        String routingId = registeredRoutingInformation.getId();
        String clientMethod = registeredRoutingInformation.getMethod();
        String clientPath = registeredRoutingInformation.getPath();

        String path = "/blog.json";
        String method = "GET";

        assertThat(routingId).isEqualTo(id);
        assertThat(clientPath).isEqualTo(path);
        assertThat(clientMethod).isEqualTo(method);

    }

    @Test
    void 모든_라우팅정보_조회_테스트() {
        List<RoutingInformation> routings = new ArrayList<>();
        routings.add(new RoutingInformation());
        routings.add(new RoutingInformation());

        when(routingRepository.findAll()).thenReturn(routings);

        List<RoutingInformationDto> routingResult = routingService.findAll();

        assertThat(routingResult).hasSize(2);
    }

    @Test
    void 라우팅정보_업데이트_테스트() {
        RoutingInformationDto updatedRoutingInformationDto = RoutingInformationDto.builder()
                .id(id)
                .path("/news.json")
                .method("GET")
                .baseUrl("https://openapi.naver.com")
                .rateLimit(5)
                .domain("news-service")
                .build();

        RoutingInformation updatedRoutingInformation = RoutingInformation.builder()
                .id(id)
                .path("/news.json")
                .method("GET")
                .baseUrl("https://openapi.naver.com")
                .rateLimit(5)
                .domain("news-service")
                .build();

        when(routingRepository.findById(id)).thenReturn(Optional.of(routingInformation));
        when(routingRepository.save(any(RoutingInformation.class))).thenReturn(updatedRoutingInformation);

        routingService.updateById(updatedRoutingInformationDto, id);

        verify(routingRepository).findById(id);
        verify(routingRepository).save(any(RoutingInformation.class));

        assertThat(updatedRoutingInformation.getMethod()).isEqualTo(updatedRoutingInformationDto.getMethod());
        assertThat(updatedRoutingInformation.getPath()).isEqualTo(updatedRoutingInformationDto.getPath());
        assertThat(updatedRoutingInformation.getId()).isEqualTo(id);
    }

    @Test
    void 유효한_라우팅정보_삭제_테스트() {
        when(routingRepository.findById(id)).thenReturn(Optional.of(routingInformation));

        routingService.deleteById(id);

        verify(routingRepository).findById(id);
        verify(routingRepository).deleteById(id);
    }

    @Test
    void 잘못된_라우팅정보_삭제_테스트() {
        when(routingRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(null));
        try {
            routingService.deleteById(id);
        } catch (ResponseException e) {
            ErrorResponse errorResponse = e.getErrorResponse();
            assertThat(errorResponse.getCode()).isEqualTo(404);
            assertThat(errorResponse.getErrorMessage()).isEqualTo("라우팅 정보 리소스를 찾을 수 없습니다.");
        }
    }

    @Test
    void 중복_클라이언트_API_테스트() {
        when(routingRepository.findByPathAndMethodAndDomain(any(String.class), any(String.class), any(String.class))).thenReturn(routingInformation);
        try {
            routingService.validateDuplicateClientApi(routingInformationDto);
        } catch (ResponseException e) {
            ErrorResponse errorResponse = e.getErrorResponse();
            assertThat(errorResponse.getCode()).isEqualTo(409);
            assertThat(errorResponse.getErrorMessage()).isEqualTo("이미 존재하는 클라이언트 API입니다.");
        }
    }
}