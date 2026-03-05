package ug.daes.onboarding.model;

import jakarta.persistence.*;

@Entity
@Table(name = "prefered_titles")
public class PreferedTitlesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "prefered_titles")
    private String preferedTitles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPreferedTitles() {
        return preferedTitles;
    }

    public void setPreferedTitles(String preferedTitles) {
        this.preferedTitles = preferedTitles;
    }


    @Override
    public String toString() {
        return "PreferedTitle{" +
                "id=" + id +
                ", preferedTitles='" + preferedTitles + '\'' +
                '}';
    }
}
