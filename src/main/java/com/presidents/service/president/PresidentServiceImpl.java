package com.presidents.service.president;

import com.presidents.exception.exceptions.EntityNotFoundException;
import com.presidents.exception.messages.PresidentsControllerExceptionMessages;
import com.presidents.model.dto.PresidentDto;
import com.presidents.model.entity.President;
import com.presidents.model.mapper.PresidentMapper;
import com.presidents.repository.PresidentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
@Transactional
@RequiredArgsConstructor
public class PresidentServiceImpl implements PresidentService {

    private final PresidentsRepository presidentsRepository;

    @Override
    public List<PresidentDto> getAllPresidents() {
        return presidentsRepository.findAll().stream()
                .map(PresidentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<PresidentDto> getAllPresidentsPaginated(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return presidentsRepository.findAll(pageable).map(PresidentMapper::toDto);
    }

    @Override
    public Set<PresidentDto> findPresidentsByName(String name) {
        Set<President> presidents = presidentsRepository.findPresidentsByName(name);
        if (presidents.isEmpty()) {
            throw new EntityNotFoundException(PresidentsControllerExceptionMessages
                    .ENTITY_FOR_PROVIDED_PARAMETER_NOT_EXIST.getMessage());
        }
        return presidents.stream().map(PresidentMapper::toDto).collect(Collectors.toSet());
    }

    @Override
    public Set<PresidentDto> findPresidentsByPoliticalParty(String party) {
        return presidentsRepository.findPresidentsByPoliticalParty(party).stream()
                .map(PresidentMapper::toDto).collect(Collectors.toSet());
    }

    @Override
    public PresidentDto savePresident(PresidentDto presidentDto) {
        return PresidentMapper.toDto(presidentsRepository.save(PresidentMapper.toEntity(presidentDto)));
    }

    @Override
    public PresidentDto updatePresident(PresidentDto presidentDto) {
        var president =  presidentsRepository.findById(presidentDto.getId());
        if (president.isPresent()) {
            president.map(p -> {
                p.setName(presidentDto.getName());
                p.setSurname(presidentDto.getSurname());
                p.setPoliticalParty(presidentDto.getPoliticalParty());
                p.setTermFrom(presidentDto.getTermFrom());
                p.setTermTo(presidentDto.getTermTo());
                return PresidentMapper.toDto(p);
                });
        } else {
            return PresidentMapper.toDto(presidentsRepository.save(PresidentMapper.toEntity(presidentDto)));
        }
        return presidentDto;
    }

    public PresidentDto updatePresidentPartial(PresidentDto presidentDto) {
        return presidentsRepository.findById(presidentDto.getId()).map(president -> {
            if (nonNull(presidentDto.getName())) {
                president.setName(presidentDto.getName());
            }
            if (nonNull(presidentDto.getSurname())) {
                president.setSurname(presidentDto.getSurname());
            }
            if (nonNull(presidentDto.getTermFrom())) {
                president.setTermFrom(presidentDto.getTermFrom());
            }
            if (nonNull(presidentDto.getTermTo())) {
                president.setTermTo(presidentDto.getTermTo());
            }
            if (nonNull(presidentDto.getPoliticalParty())) {
                president.setPoliticalParty(presidentDto.getPoliticalParty());
            }
            return PresidentMapper.toDto(president);
        }).orElseThrow(() -> new EntityNotFoundException(PresidentsControllerExceptionMessages
                .ENTITY_FOR_PROVIDED_ID_NOT_EXIST.getMessage()));
    }

    @Override
    public void deletePresident(Long id) {

        presidentsRepository.deleteById(id);
    }
}
