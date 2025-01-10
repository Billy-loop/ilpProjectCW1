package uk.ac.ed.inf.pizzadronz.controllerUnitTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.ac.ed.inf.pizzadronz.controller.CalcDeliveryPathController;
import uk.ac.ed.inf.pizzadronz.model.LngLatPairRequest;
import uk.ac.ed.inf.pizzadronz.model.Position;
import uk.ac.ed.inf.pizzadronz.util.ImplementUtil;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CalcDeliveryPathController.class)
public class CalcDeliveryPathTest  {

    private static final Position APPLETON_TOWER = new Position(-3.186874, 55.944494);

    @Autowired
    private MockMvc mockMvc;

    private void performAndValidate(String validOrderRequest) throws Exception {
        mockMvc.perform(post("/calcDeliveryPath")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validOrderRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNumber()) // Verifies that a list is returned
                .andExpect(jsonPath("$[0].lng").isNumber())
                .andExpect(jsonPath("$[0].lat").isNumber())
                .andExpect(result -> {
                    // Parse response JSON into a generic list of maps
                    String responseBody = result.getResponse().getContentAsString();
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<Map<String, Double>> positions = objectMapper.readValue(responseBody, new TypeReference<List<Map<String, Double>>>() {});

                    // Validate each position
                    for (Map<String, Double> positionMap : positions) {
                        double lng = positionMap.get("lng");
                        double lat = positionMap.get("lat");
                        Position position = new Position(lng, lat);

                        // Assert the position is outside all no-fly zones
                        boolean isInNoFlyZone = CalcDeliveryPathController.noFlyZones.stream()
                                .anyMatch(zone -> ImplementUtil.isInPolygon(position, zone.getVertices()));
                        assertFalse(isInNoFlyZone, "A position in the path is inside a no-fly zone.");
                    }

                    // Validate the last position is close to Appleton Tower
                    Map<String, Double> lastPositionMap = positions.get(positions.size() - 1);
                    Position lastPosition = new Position(lastPositionMap.get("lng"), lastPositionMap.get("lat"));
                    boolean isCloseToAppletonTower = ImplementUtil.isCloseTo(new LngLatPairRequest(lastPosition, APPLETON_TOWER));
                    assertTrue(isCloseToAppletonTower, "The last position is not close to Appleton Tower.");
                });
    }

    @Test
    public void testCalcDeliveryPath_ValidOrder_R1() throws Exception {
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
       \s""";

       performAndValidate(validOrderRequest);
    }

    @Test
    public void testCalcDeliveryPath_ValidOrder_R2() throws Exception {
        String validOrderRequest = """
                {
                      "orderNo": "5A9FD4FA",
                      "orderDate": "2025-01-13",
                      "orderStatus": "VALID",
                      "orderValidationCode": "NO_ERROR",
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
                        "creditCardNumber": "5157921783529552",
                        "creditCardExpiry": "01/26",
                        "cvv": "948"
                      }
                    }
               \s""";

        performAndValidate(validOrderRequest);
    }

    @Test
    public void testCalcDeliveryPath_ValidOrder_R3() throws Exception {
        String validOrderRequest = """
                {
                     "orderNo": "6B0F8768",
                     "orderDate": "2025-01-12",
                     "orderStatus": "VALID",
                     "orderValidationCode": "NO_ERROR",
                     "priceTotalInPence": 2400,
                     "pizzasInOrder": [
                       {
                         "name": "R3: Super Cheese",
                         "priceInPence": 1400
                       },
                       {
                         "name": "R3: All Shrooms",
                         "priceInPence": 900
                       }
                     ],
                     "creditCardInformation": {
                       "creditCardNumber": "4916939853026189",
                       "creditCardExpiry": "02/25",
                       "cvv": "703"
                     }
                   }
       \s""";

        performAndValidate(validOrderRequest);
    }

    @Test
    public void testCalcDeliveryPath_ValidOrder_R4() throws Exception {
        String validOrderRequest = """
                {
                     "orderNo": "333C4248",
                     "orderDate": "2025-01-14",
                     "orderStatus": "VALID",
                     "orderValidationCode": "NO_ERROR",
                     "priceTotalInPence": 2400,
                     "pizzasInOrder": [
                       {
                         "name": "R4: Proper Pizza",
                         "priceInPence": 1400
                       },
                       {
                         "name": "R4: Pineapple & Ham & Cheese",
                         "priceInPence": 900
                       }
                     ],
                     "creditCardInformation": {
                       "creditCardNumber": "4520579135467435",
                       "creditCardExpiry": "10/25",
                       "cvv": "659"
                     }
                   }
       \s""";

        performAndValidate(validOrderRequest);
    }

    @Test
    public void testCalcDeliveryPath_ValidOrder_R5() throws Exception {
        String validOrderRequest = """
                {
                     "orderNo": "7D6409B0",
                     "orderDate": "2025-01-13",
                     "orderStatus": "VALID",
                     "orderValidationCode": "NO_ERROR",
                     "priceTotalInPence": 2400,
                     "pizzasInOrder": [
                       {
                         "name": "R5: Pizza Dream",
                         "priceInPence": 1400
                       },
                       {
                         "name": "R5: My kind of pizza",
                         "priceInPence": 900
                       }
                     ],
                     "creditCardInformation": {
                       "creditCardNumber": "4635334609224447",
                       "creditCardExpiry": "07/25",
                       "cvv": "856"
                     }
                   }
       \s""";

        performAndValidate(validOrderRequest);
    }

    @Test
    public void testCalcDeliveryPath_ValidOrder_R6() throws Exception {
        String validOrderRequest = """
                {
                     "orderNo": "6C20BF28",
                     "orderDate": "2025-01-14",
                     "orderStatus": "VALID",
                     "orderValidationCode": "NO_ERROR",
                     "priceTotalInPence": 2400,
                     "pizzasInOrder": [
                       {
                         "name": "R6: Sucuk delight",
                         "priceInPence": 1400
                       },
                       {
                         "name": "R6: Dreams of Syria",
                         "priceInPence": 900
                       }
                     ],
                     "creditCardInformation": {
                       "creditCardNumber": "4742045931653299",
                       "creditCardExpiry": "08/25",
                       "cvv": "417"
                     }
                   }
       \s""";

        performAndValidate(validOrderRequest);
    }

    @Test
    public void testCalcDeliveryPath_ValidOrder_R7() throws Exception {
        String validOrderRequest = """
                {
                     "orderNo": "553398CB",
                     "orderDate": "2025-01-14",
                     "orderStatus": "VALID",
                     "orderValidationCode": "NO_ERROR",
                     "priceTotalInPence": 2400,
                     "pizzasInOrder": [
                       {
                         "name": "R7: Hot, hotter, the hottest",
                         "priceInPence": 1400
                       },
                       {
                         "name": "R7: All you ever wanted",
                         "priceInPence": 900
                       }
                     ],
                     "creditCardInformation": {
                       "creditCardNumber": "4163950434249752",
                       "creditCardExpiry": "03/26",
                       "cvv": "102"
                     }
                   }
       \s""";

        performAndValidate(validOrderRequest);
    }


    @Test
    public void testCalcDeliveryPath_InvalidOrder() throws Exception {
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

        mockMvc.perform(post("/calcDeliveryPath")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidOrderRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCalcDeliveryPath_EmptyRequestBody() throws Exception {
        mockMvc.perform(post("/calcDeliveryPath")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCalcDeliveryPath_NullRequestBody() throws Exception {
        mockMvc.perform(post("/calcDeliveryPath")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

