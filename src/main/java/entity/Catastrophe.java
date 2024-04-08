package entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Catastrophe {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Event event;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate lastValidDate;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Zone> zone;


    public Catastrophe() {}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getLastValidDate() {
        return lastValidDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setLastValidDate(LocalDate lastValidDate) {
        this.lastValidDate = lastValidDate;
    }


    public List<Zone> getZone() {
        return zone;
    }

    public void setZone(List<Zone> zone) {
        this.zone = zone;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "Catastrophe{" +
                "id=" + id +
                ", event=" + event +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", lastValidDate=" + lastValidDate +
                ", zone=" + zone +
                '}';
    }
}
