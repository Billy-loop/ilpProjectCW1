package uk.ac.ed.inf.pizzadronz.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Node {
        private Position position;
        private double g, f;

        public Node(Position position, double g, double f) {
            this.position = position;
            this.g = g;
            this.f = f;
        }

}

