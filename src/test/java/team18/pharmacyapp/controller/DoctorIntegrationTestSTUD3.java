package team18.pharmacyapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import team18.pharmacyapp.model.dtos.HandleReservationDTO;
import team18.pharmacyapp.model.dtos.security.UserTokenDTO;
import team18.pharmacyapp.util.TestUtil;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static team18.pharmacyapp.constants.DoctorConstants.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DoctorIntegrationTestSTUD3 {

    private static final String URL_PREFIX = "/api/doctors";
    private static final String URL_PREFIX_TERM = "/api/terms";
    private static final String URL_PREFIX_MED = "/api/medicines";

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() { this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); }

    @Test
    public void testGetDoctorById() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/" + ID_PERA)).andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(ID_PERA.toString()))
                .andExpect(jsonPath("$.name").value(NAME_PERA))
                .andExpect(jsonPath("$.surname").value(SURNAME_PERA));
    }

    @Test
    public void testGetPharmacistPharmacy() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/pharmPharmacy/" + ID_PHARM)).andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(ID_PHARMACY.toString()))
                .andExpect(jsonPath("$.name").value(PHARMACY_NAME));
    }

    @Test
    @WithMockUser(username = "peraperic@gmail.com" ,password = "nekipass123", roles = {"DERMATOLOGIST"})
    public void testGetAllDermPharmacies() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/pharmacyList/" + ID_PERA)).andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.[0].id").value(ID_PHARMACY1.toString()))
                .andExpect(jsonPath("$.[0].name").value(PHARMACY_NAME1))
                .andExpect(jsonPath("$.[1].id").value(ID_PHARMACY.toString()))
                .andExpect(jsonPath("$.[1].name").value(PHARMACY_NAME));
    }

    @Test
    @WithMockUser(username = "peraperic@gmail.com" ,password = "nekipass123", roles = {"DERMATOLOGIST"})
    public void testGetAllDoctorTerms() throws Exception {
        mockMvc.perform(get(URL_PREFIX_TERM + "/doctorAll/" + ID_PERA)).andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(8)))
                .andExpect(jsonPath("$.[*].patient.name").value(hasItem("Slobodanka")))
                .andExpect(jsonPath("$.[*].patient.surname").value(hasItem("Ilic")))
                .andExpect(jsonPath("$.[*].patient.mail").value(hasItem("slobodankailic@gmail.com")));

    }

    @Test
    @WithMockUser(username = "jovanajovic@gmail.com" ,password = "nekipass123", roles = {"PHARMACIST"})
    public void handleReservation() throws Exception {
        HandleReservationDTO dto = new HandleReservationDTO();
        dto.setId(RESERVATION_ID);
        dto.setEmail("nekimail@gmail.com");
        dto.setMedicine("neki lek");
        String json = TestUtil.json(dto);
        MvcResult result=mockMvc.perform(put(URL_PREFIX_MED + "/handleReservation").contentType(contentType).content(json)).andExpect(status().isOk())
                .andExpect(content().contentType(contentType)).andReturn();
        assert(result.getResponse().getContentAsString()).equals("true");

    }
}
