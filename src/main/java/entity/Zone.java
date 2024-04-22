package entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;


@NamedQueries({
        @NamedQuery(name = "findByCenter", query = "SELECT z FROM Zone z WHERE z.centerLat = :lat AND z.centerLon = :lon")
})

@Entity
public class Zone {

//    @Id
//    @OneToOne(cascade = CascadeType.ALL)
//    private Coordinate center;

    // mannually added center, don't use coordinate as it will difficult queries
    @Id
    @Column(precision=6, scale=4)
    private BigDecimal centerLat;

    @Id
    @Column(precision=6, scale=4)
    private BigDecimal centerLon;

    private int radius;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Coordinate> polygons;

    public Zone() {
    }

    public int getRadius() {
        return radius;
    }

    public List<Coordinate> getPolygons() {
        return polygons;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setPolygons(List<Coordinate> polygon) {
        this.polygons = polygon;
    }

    public BigDecimal getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(BigDecimal lat) {
        this.centerLat = lat;
    }

    public BigDecimal getCenterLon() {
        return centerLon;
    }

    public void setCenterLon(BigDecimal lon) {
        this.centerLon = lon;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "lat=" + centerLat +
                ", lon=" + centerLon +
                ", radius=" + radius +
                ", polygons=" + polygons +
                '}';
    }
}
