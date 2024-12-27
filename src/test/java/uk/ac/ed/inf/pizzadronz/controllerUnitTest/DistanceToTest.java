package uk.ac.ed.inf.pizzadronz.controllerUnitTest;

// Import statements
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import uk.ac.ed.inf.pizzadronz.controller.DistanceToController;

@WebMvcTest(DistanceToController.class)
public class DistanceToTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // For JSON serialization/deserialization

    @Test
    public void testCorrectCall() throws Exception {
        // Prepare the correct JSON body
        String json = """
                        {
                        "position1": {
                        "lng": -3.192473,
                        "lat": 55.946233
                        },
                        "position2": {
                        "lng": -3.192473,
                        "lat": 55.942617
                        }
                        }
        """;

        // Expected distance calculation
        double expectedDistance = Math.sqrt(Math.pow(55.942617 - 55.946233, 2) + Math.pow(-3.192473 - -3.192473, 2));

        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedDistance)));
    }

    @Test
    public void testSemanticErrorCall() throws Exception {
        // Semantic error: invalid latitude and longitude values
        String json = """
                        {
                        "position1": {
                        "lng": -200.0,
                        "lat": 100.0
                        },
                        "position2": {
                        "lng": -3.192473,
                        "lat": 55.942617
                        }
                        }
        """;

        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSyntacticalErrorCall() throws Exception {
        // Syntactical error: malformed JSON (missing commas and braces)
        String json = """
                        {
                        "position1": {
                        "lng": -3.0
                        "lat": 1.0
                        },
                        "position2": {
                        "lng": -3.192473,
                        "lat": 55.942617
                        }
                        }
        """; //Missing comma

        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEmptyBodyCall() throws Exception {
        // Empty body
        mockMvc.perform(post("/distanceTo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }
 }



