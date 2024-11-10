package uk.ac.ed.inf.pizzadronz.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uk.ac.ed.inf.pizzadronz.model.Order;
import uk.ac.ed.inf.pizzadronz.model.Position;
import java.util.List;

public class CalcDeliveryPathController {
    @PostMapping("calcDeliveryPath")
    public List<Position> calcDeliveryPath(@RequestBody Order order){
        return null;
    }
}
