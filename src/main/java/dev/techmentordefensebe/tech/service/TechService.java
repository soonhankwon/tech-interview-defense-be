package dev.techmentordefensebe.tech.service;

import dev.techmentordefensebe.tech.domain.Tech;
import dev.techmentordefensebe.tech.dto.TechDTO;
import dev.techmentordefensebe.tech.dto.response.TechsGetResponse;
import dev.techmentordefensebe.tech.repository.TechRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TechService {

    private static final int PAGE_SIZE = 10;
    private final TechRepository techRepository;

    public TechsGetResponse findTechs(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<Tech> page = techRepository.findAll(pageRequest);

        List<TechDTO> techDTOS = page.getContent().stream()
                .map(TechDTO::from)
                .collect(Collectors.toList());

        return TechsGetResponse.of(page.getTotalPages(), techDTOS);
    }
}
