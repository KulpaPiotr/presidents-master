package com.presidents.service.president;

import com.presidents.model.dto.PresidentDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface PresidentService {

    List<PresidentDto> getAllPresidents();

    Page<PresidentDto> getAllPresidentsPaginated(Integer pageNumber, Integer pageSize);


    Set<PresidentDto> findPresidentsByName(String name);

    Set<PresidentDto> findPresidentsByPoliticalParty(String party);

    PresidentDto savePresident(PresidentDto presidentDto);

    PresidentDto updatePresident(PresidentDto presidentDto);

    PresidentDto updatePresidentPartial(PresidentDto presidentDto);

    void deletePresident(Long id);
}
