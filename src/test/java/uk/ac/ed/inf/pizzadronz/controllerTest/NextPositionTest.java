package uk.ac.ed.inf.pizzadronz.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.ed.inf.pizzadronz.controller.NextPositionController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(NextPositionController.class)
public class NextPositionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testNextPosition_ValidRequest() throws Exception {
        String requestBody = """
        {
          \"start\": {\"lng\": -3.192473, \"lat\": 55.946233},
          \"angle\": 45
        }
        """;

        // Correcting expected values based on the actual movement calculation (distance = 0.00015)
        double radian = Math.toRadians(45);
        double expectedLng = -3.192473 + Math.cos(radian) * 0.00015;
        double expectedLat = 55.946233 + Math.sin(radian) * 0.00015;

        mockMvc.perform(post("/nextPosition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lng").value(expectedLng))
                .andExpect(jsonPath("$.lat").value(expectedLat));
    }

    @Test
    public void testNextPosition_InvalidRequest_MissingFields() throws Exception {
        String requestBody = """
        {
          \"start\": {\"lng\": -3.192473}
        }
        """;

        mockMvc.perform(post("/nextPosition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNextPosition_InvalidRequest_NullBody() throws Exception {
        mockMvc.perform(post("/nextPosition")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNextPosition_InvalidRequest_InvalidAngle() throws Exception {
        String requestBody = """
        {
          \"start\": {\"lng\": -3.192473, \"lat\": 55.946233},
          \"angle\": 400
        }
        """;

        mockMvc.perform(post("/nextPosition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testNextPosition_InvalidRequest_InvalidCoordinates() throws Exception {
        String requestBody = """
        {
          \"start\": {\"lng\": 200, \"lat\": 95},
          \"angle\": 45
        }
        """;

        mockMvc.perform(post("/nextPosition")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}

