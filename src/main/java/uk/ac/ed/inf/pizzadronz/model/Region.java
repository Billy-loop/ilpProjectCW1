package uk.ac.ed.inf.pizzadronz.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Region {
    private String name;
    private List<Position> vertices;

    public Region(String name, List<Position> vertices) {
        this.name = name;
        this.vertices = vertices;
    }

//    public String getName() {
//        return name;
//    }
//    public List<Position> getVertices() {
//        return vertices;
//    }


}
