package cn.zenghome.api;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FirmwareRepository extends R2dbcRepository<Firmware, String> {

    Mono<Firmware> findFirstByProductOrderByVersionDesc(String product);
}
