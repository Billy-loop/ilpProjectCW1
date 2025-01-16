package uk.ac.ed.inf.pizzadronz.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.ed.inf.pizzadronz.controller.CalcDeliveryPathAsGeoJsonController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CalcDeliveryPathAsGeoJsonController.class)
public class CalcDeliveryPathAsGeoJsonTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCalcDeliveryPathAsGeoJson_ValidOrder() throws Exception {
        String validOrderRequest = """
                {
                     "orderNo": "1BC383AD",
                     "orderDate": "2025-01-12",
                     "orderStatus": "VALID",
                     "orderValidationCode": "NO_ERROR",
                     "priceTotalInPence": 2500,
                     "pizzasInOrder": [
                       {
                         "name": "R1: Margarita",
                         "priceInPence": 1000
                       },
                       {
                         "name": "R1: Calzone",
                         "priceInPence": 1400
                       }
                     ],
                     "creditCardInformation": {
                       "creditCardNumber": "4723891202843637",
                       "creditCardExpiry": "02/26",
                       "cvv": "981"
                     }
                   }
        """;

        mockMvc.perform(post("/calcDeliveryPathAsGeoJson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validOrderRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("FeatureCollection"))
                .andExpect(jsonPath("$.features[0].type").value("Feature"))
                .andExpect(jsonPath("$.features[0].geometry.type").value("LineString"))
                .andExpect(jsonPath("$.features[0].geometry.coordinates").isArray())
                .andExpect(jsonPath("$.features[0].geometry.coordinates[0]").isArray())
                .andExpect(jsonPath("$.features[0].geometry.coordinates[0][0]").isNumber())
                .andExpect(jsonPath("$.features[0].geometry.coordinates[0][1]").isNumber())
                .andExpect(jsonPath("$.features[0].properties.description").value("Delivery flight path"));
    }

    @Test
    public void testCalcDeliveryPathAsGeoJson_InvalidOrder() throws Exception {
        String invalidOrderRequest = """
                {
                     "orderNo": "3B850618",
                     "orderDate": "2025-01-09",
                     "orderStatus": "INVALID",
                     "orderValidationCode": "CARD_NUMBER_INVALID",
                     "priceTotalInPence": 2600,
                     "pizzasInOrder": [
                       {
                         "name": "R2: Meat Lover",
                         "priceInPence": 1400
                       },
                       {
                         "name": "R2: Vegan Delight",
                         "priceInPence": 1100
                       }
                     ],
                     "creditCardInformation": {
                       "creditCardNumber": "48195560323551",
                       "creditCardExpiry": "05/25",
                       "cvv": "816"
                     }
                   }
        """;

        mockMvc.perform(post("/calcDeliveryPathAsGeoJson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidOrderRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCalcDeliveryPathAsGeoJson_EmptyRequestBody() throws Exception {
        mockMvc.perform(post("/calcDeliveryPathAsGeoJson")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCalcDeliveryPathAsGeoJson_NullRequestBody() throws Exception {
        mockMvc.perform(post("/calcDeliveryPathAsGeoJson")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

