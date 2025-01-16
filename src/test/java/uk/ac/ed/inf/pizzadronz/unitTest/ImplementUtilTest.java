package uk.ac.ed.inf.pizzadronz.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.ac.ed.inf.pizzadronz.model.*;
import uk.ac.ed.inf.pizzadronz.service.DataRetrive;
import uk.ac.ed.inf.pizzadronz.util.ImplementUtil;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ImplementUtilTest {

    @Mock
    private DataRetrive dataRetrive;

    @InjectMocks
    private ImplementUtil implementUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDistanceTo() {
        Position p1 = new Position(-3.186, 55.944);
        Position p2 = new Position(-3.187, 55.945);
        LngLatPairRequest request = new LngLatPairRequest(p1, p2);

        double distance = ImplementUtil.distanceTo(request);
        assertEquals(0.001414, distance, 0.000001); // sqrt(0.0001^2 + 0.0001^2)
    }

    @Test
    void testIsCloseTo() {
        Position p1 = new Position(-3.186, 55.944);
        Position p2 = new Position(-3.18601, 55.94401);
        LngLatPairRequest request = new LngLatPairRequest(p1, p2);

        assertTrue(ImplementUtil.isCloseTo(request));

        Position p3 = new Position(-3.19, 55.95);
        request = new LngLatPairRequest(p1, p3);
        assertFalse(ImplementUtil.isCloseTo(request));
    }

    @Test
    void testNextPosition() {
        Position start = new Position(-3.186, 55.944);
        NextPositionRequest request = new NextPositionRequest(start, 90.0); // Move east

        Position result = ImplementUtil.nextPosition(request);

        // Use a delta for floating-point precision
        assertEquals(-3.186, result.getLng(), 0.00001); // Longitude increases
        assertEquals(55.94415, result.getLat(), 0.00001);   // Latitude stays the same
    }



    @Test
    void testIsOnLine() {
        Position p = new Position(1.0, 1.0);
        Position v1 = new Position(0.0, 0.0);
        Position v2 = new Position(2.0, 2.0);

        assertTrue(ImplementUtil.isOnLine(p, v1, v2));

        Position outside = new Position(1.0, 2.0);
        assertFalse(ImplementUtil.isOnLine(outside, v1, v2));
    }

    @Test
    void testIsInPolygon() {
        List<Position> vertices = List.of(
                new Position(0.0, 0.0),
                new Position(4.0, 0.0),
                new Position(4.0, 4.0),
                new Position(0.0, 4.0)
        );
        Position inside = new Position(2.0, 2.0);
        Position outside = new Position(5.0, 5.0);
        Position onEdge = new Position(0.0, 2.0);

        assertTrue(ImplementUtil.isInPolygon(inside, vertices));
        assertFalse(ImplementUtil.isInPolygon(outside, vertices));
        assertTrue(ImplementUtil.isInPolygon(onEdge, vertices));
    }


}
