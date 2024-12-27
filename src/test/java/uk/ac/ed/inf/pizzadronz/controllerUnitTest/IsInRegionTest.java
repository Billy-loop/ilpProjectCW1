package uk.ac.ed.inf.pizzadronz.controllerUnitTest;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.ed.inf.pizzadronz.controller.IsInRegionController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(IsInRegionController.class)
public class IsInRegionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIsInRegion_ValidRequest_InsideRegion() throws Exception {
        String requestBody = """
        {
          \"position\": {\"lng\": -3.192, \"lat\": 55.945},
          \"region\": {
            \"name\": \"central\",
            \"vertices\": [
              {\"lng\": -3.192473, \"lat\": 55.946233},
              {\"lng\": -3.192473, \"lat\": 55.942617},
              {\"lng\": -3.184319, \"lat\": 55.942617},
              {\"lng\": -3.184319, \"lat\": 55.946233},
              {\"lng\": -3.192473, \"lat\": 55.946233}
            ]
          }
        }
        """;

        mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    public void testIsInRegion_ValidRequest_OutsideRegion() throws Exception {
        String requestBody = """
        {
          \"position\": {\"lng\": -3.180, \"lat\": 55.945},
          \"region\": {
            \"name\": \"central\",
            \"vertices\": [
              {\"lng\": -3.192473, \"lat\": 55.946233},
              {\"lng\": -3.192473, \"lat\": 55.942617},
              {\"lng\": -3.184319, \"lat\": 55.942617},
              {\"lng\": -3.184319, \"lat\": 55.946233},
              {\"lng\": -3.192473, \"lat\": 55.946233}
            ]
          }
        }
        """;

        mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    public void testIsInRegion_InvalidRequest_OpenRegion() throws Exception {
        String requestBody = """
        {
          \"position\": {\"lng\": -3.192, \"lat\": 55.945},
          \"region\": {
            \"name\": \"central\",
            \"vertices\": [
              {\"lng\": -3.192473, \"lat\": 55.946233},
              {\"lng\": -3.192473, \"lat\": 55.942617},
              {\"lng\": -3.184319, \"lat\": 55.942617},
              {\"lng\": -3.184319, \"lat\": 55.946233}
            ]
          }
        }
        """;

        mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testIsInRegion_InvalidRequest_MissingFields() throws Exception {
        String requestBody = """
        {
          \"position\": {\"lng\": -3.192},
          \"region\": {
            \"name\": \"central\",
            \"vertices\": [
              {\"lng\": -3.192473, \"lat\": 55.946233},
              {\"lng\": -3.192473, \"lat\": 55.942617},
              {\"lng\": -3.184319, \"lat\": 55.942617},
              {\"lng\": -3.184319, \"lat\": 55.946233},
              {\"lng\": -3.192473, \"lat\": 55.946233}
            ]
          }
        }
        """;

        mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testIsInRegion_InvalidRequest_NullBody() throws Exception {
        mockMvc.perform(post("/isInRegion")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
