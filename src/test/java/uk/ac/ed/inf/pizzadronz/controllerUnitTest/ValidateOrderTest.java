package uk.ac.ed.inf.pizzadronz.controllerUnitTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import uk.ac.ed.inf.pizzadronz.controller.ValidateOrderController;
import uk.ac.ed.inf.pizzadronz.model.Order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ValidateOrderController.class)
public class ValidateOrderTest {

    @Autowired
    private MockMvc mockMvc;

    private final String fetchUrl = "https://ilp-rest-2024.azurewebsites.net/orders";

    @Test
    public void testValidateOrdersFromUrl() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // Fetch orders from the provided URL
        Order[] orders = restTemplate.getForObject(fetchUrl, Order[].class);

        if (orders != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Handle LocalDate serialization

            for (Order order : orders) {
                // Convert order to JSON string
                String orderJson = objectMapper.writeValueAsString(order);

                // Debugging: Print expected values for each order
                System.out.println("Testing Order No: " + order.getOrderNo());
                System.out.println("Expected Status: " + order.getOrderStatus());
                System.out.println("Expected Validation Code: " + order.getOrderValidationCode());

                // Perform POST request and validate response
                mockMvc.perform(post("/validateOrder")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(orderJson))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.orderStatus").value(order.getOrderStatus().toString()))
                        .andExpect(jsonPath("$.orderValidationCode").value(order.getOrderValidationCode().toString()));
            }
        } else {
            throw new RuntimeException("Failed to fetch orders from the provided URL.");
        }
    }
}
