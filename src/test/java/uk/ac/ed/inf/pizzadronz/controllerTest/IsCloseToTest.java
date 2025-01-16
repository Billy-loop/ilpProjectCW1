package uk.ac.ed.inf.pizzadronz.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.ed.inf.pizzadronz.controller.IsCloseToController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(IsCloseToController.class)
public class IsCloseToTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIsCloseTo_ValidRequestClosePositions() throws Exception {
        String requestBody = """
        {
          \"position1\": {\"lng\": -3.192473, \"lat\": 55.946233},
          \"position2\": {\"lng\": -3.192473, \"lat\": 55.946220}
        }
        """;

        mockMvc.perform(post("/isCloseTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testIsCloseTo_ValidRequestFarPositions() throws Exception {
        String requestBody = """
        {
          \"position1\": {\"lng\": -3.192473, \"lat\": 55.946233},
          \"position2\": {\"lng\": -3.192473, \"lat\": 55.942617}
        }
        """;

        mockMvc.perform(post("/isCloseTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    public void testIsCloseTo_InvalidRequest_MissingFields() throws Exception {
        String requestBody = """
        {
          \"position1\": {\"lng\": -3.192473}
        }
        """;

        mockMvc.perform(post("/isCloseTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testIsCloseTo_InvalidRequest_NullBody() throws Exception {
        mockMvc.perform(post("/isCloseTo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testIsCloseTo_InvalidRequest_InvalidCoordinates() throws Exception {
        String requestBody = """
        {
          \"position1\": {\"lng\": 200, \"lat\": 95},
          \"position2\": {\"lng\": -3.192473, \"lat\": 55.942617}
        }
        """;

        mockMvc.perform(post("/isCloseTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
