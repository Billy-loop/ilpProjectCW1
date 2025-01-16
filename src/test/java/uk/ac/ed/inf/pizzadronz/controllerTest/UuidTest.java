package uk.ac.ed.inf.pizzadronz.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.ed.inf.pizzadronz.controller.UuidController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UuidController.class)
public class  UuidTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUuid() throws Exception {
        mockMvc.perform(get("/uuid"))
                .andExpect(status().isOk()) // Check if the status is 200
                .andExpect(content().string("s2292976")); // Check if the response matches the expected UUID
    }
}

