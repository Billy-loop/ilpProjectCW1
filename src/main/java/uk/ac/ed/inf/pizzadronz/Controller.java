package uk.ac.ed.inf.pizzadronz;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Controller {

    @GetMapping("/uuid")
    public String getUUid(){
        return "s" + "1234567";
    }
    @PostMapping("/distanceTo")
   public double distanceTo(@RequestBody LngLatPairRequest lnglat1){
        double lng1 = lnglat1.getPosition1().getLng();
        double lng2 = lnglat1.getPosition2().getLng();

        double lat1 = lnglat1.getPosition1().getLat();
        double lat2 = lnglat1.getPosition2().getLat();

        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2));
   }
}
