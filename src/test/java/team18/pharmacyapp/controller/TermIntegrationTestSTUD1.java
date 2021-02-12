package team18.pharmacyapp.controller;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import team18.pharmacyapp.model.dtos.CancelTermDTO;
import team18.pharmacyapp.model.dtos.ScheduleCheckupDTO;
import team18.pharmacyapp.model.dtos.TermPaginationSortingDTO;
import team18.pharmacyapp.util.TestUtil;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static team18.pharmacyapp.constants.TermConstants.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class TermIntegrationTestSTUD1 {
    private static final String URL_PREFIX_TERM = "/api/terms";
    private static final String URL_PREFIX_COUNSELINGS = "/api/counseling";
    private static final String URL_PREFIX_CHECKUPS = "/api/checkups";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() { this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); }

    @Test
    @WithMockUser(username = "slobodankailic@gmail.com", password = "nekipass123", roles = {"PATIENT"})
    public void test1_ScheduleCheckup() throws Exception {
        ScheduleCheckupDTO schedule = new ScheduleCheckupDTO(ID_CHECKUP1, ID_SLOBODANKA);
        String requestBody = TestUtil.json(schedule);

        mockMvc.perform(put(URL_PREFIX_CHECKUPS + "/schedule").content(requestBody).contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "slobodankailic@gmail.com", password = "nekipass123", roles = {"PATIENT"})
    public void test2_ScheduleCheckupAtTheSameTime() throws Exception {
        ScheduleCheckupDTO schedule2 = new ScheduleCheckupDTO(ID_CHECKUP2, ID_SLOBODANKA);
        String requestBody2 = TestUtil.json(schedule2);

        mockMvc.perform(put(URL_PREFIX_CHECKUPS + "/schedule").content(requestBody2).contentType(contentType))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "slobodankailic@gmail.com", password = "nekipass123", roles = {"PATIENT"})
    public void test3_CancelCheckup() throws Exception {
        CancelTermDTO cancelTermDTO = new CancelTermDTO(ID_CHECKUP1, ID_SLOBODANKA);
        String requestBody2 = TestUtil.json(cancelTermDTO);

        mockMvc.perform(put(URL_PREFIX_CHECKUPS + "/cancel").content(requestBody2).contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "slobodankailic@gmail.com", password = "nekipass123", roles = {"PATIENT"})
    public void test4_GetUpcomingCheckups() throws Exception {
        TermPaginationSortingDTO tpsDTO = new TermPaginationSortingDTO(ID_SLOBODANKA, "startTime Desc.", "checkup", 1);
        String requestBody = TestUtil.json(tpsDTO);

        mockMvc.perform(post(URL_PREFIX_CHECKUPS + "/upcoming").content(requestBody).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.terms", hasSize(2)))
                .andExpect(jsonPath("$.terms.[*].type").value(hasItem("checkup")))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    @WithMockUser(username = "slobodankailic@gmail.com", password = "nekipass123", roles = {"PATIENT"})
    public void test5_GetPastCounselings() throws Exception {
        TermPaginationSortingDTO tpsDTO = new TermPaginationSortingDTO(ID_SLOBODANKA, "startTime Desc.", "counseling", 1);
        String requestBody = TestUtil.json(tpsDTO);

        mockMvc.perform(post(URL_PREFIX_COUNSELINGS + "/past").content(requestBody).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.terms", hasSize(1)))
                .andExpect(jsonPath("$.terms.[0].doctor.name").value("Milan"))
                .andExpect(jsonPath("$.terms.[0].doctor.surname").value("Milovanovic"))
                .andExpect(jsonPath("$.terms.[0].doctor.email").value("milanmilovanovic@gmail.com"))
                .andExpect(jsonPath("$.terms.[*].type").value("counseling"))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

}
