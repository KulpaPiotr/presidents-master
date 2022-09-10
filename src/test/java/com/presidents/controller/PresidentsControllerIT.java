package com.presidents.controller;

import org.hamcrest.Matchers;
import com.presidents.model.dto.PresidentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;

import static com.presidents.util.TestUtils.toJson;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PresidentsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPostRequestForPresidentSave_thenCorrectResponse() throws Exception {
        //given
        var president = PresidentDto.builder().name("TestPresident").surname("TestPresident")
                .termFrom(Timestamp.from(Instant.ofEpochMilli(15000000)))
                .termTo(Timestamp.from(Instant.ofEpochMilli(16000000)))
                .politicalParty("TestParty").build();

        //when && then
        mockMvc.perform(post("/presidents/save").content(toJson(president))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.equalTo("TestPresident")))
                .andExpect(jsonPath("$.surname", Matchers.equalTo("TestPresident")));

    }

    @Test
    void whenPostPresidentAndIncorrectName_thenIncorrectResponse() throws Exception {
        // given
        PresidentDto presidentDto = PresidentDto.builder()
                .name("")
                .build();

        // when && then
        mockMvc.perform(post("/presidents/save")
                        .content(toJson(presidentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        containsInAnyOrder("Name must be between 1 and 255 characters")));
    }
}
